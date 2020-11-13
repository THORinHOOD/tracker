package com.lada.tracker.services;

import com.lada.tracker.controllers.dto.CommentCreate;
import com.lada.tracker.controllers.dto.RequestDtoWithId;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestStatus;
import com.lada.tracker.entities.RequestTransaction;
import com.lada.tracker.repositories.CommentRepository;
import com.lada.tracker.repositories.RequestRepository;
import com.lada.tracker.repositories.RequestStatusRepository;
import com.lada.tracker.repositories.RequestTransactionRepository;
import com.lada.tracker.repositories.specifications.RequestSpecification;
import com.lada.tracker.services.models.KanbanColumn;
import com.lada.tracker.services.models.Response;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestTransactionRepository requestTransactionRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final CommentRepository commentRepository;
    private final ModelsFactoryService modelsFactoryService;

    public RequestService(RequestRepository requestRepository,
                          RequestTransactionRepository requestTransactionRepository,
                          RequestStatusRepository requestStatusRepository,
                          CommentRepository commentRepository,
                          ModelsFactoryService modelsFactoryService) {
        this.requestRepository = requestRepository;
        this.requestTransactionRepository = requestTransactionRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.commentRepository = commentRepository;
        this.modelsFactoryService = modelsFactoryService;
    }

    private Stream<RequestStatus> getRequestStatuses(Integer requestTypeId) {
        return requestStatusRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(status -> status.getRequestTypeIds().contains(requestTypeId));
    }

    public Response<List<KanbanColumn>> getKanbanBoardFiltered(Integer requestTypeId, String bodyFilter,
                                                               Timestamp after, Timestamp before) {
        if (requestTypeId == null) {
            return Response.BAD("Не передан тип запросов");
        }
        RequestSpecification requestSpecification = RequestSpecification.start()
                .eqRequestTypeId(requestTypeId)
                .bodyContains(bodyFilter)
                .registerAfter(after)
                .registerBefore(before);

        Map<Integer, List<Request>> requestsByStatus = requestRepository.findAll(requestSpecification.end())
                .stream()
                .collect(Collectors.groupingBy(Request::getStatus));

        return Response.EXECUTE(() -> getRequestStatuses(requestTypeId)
                .map(status -> KanbanColumn.builder()
                    .statusInfo(status)
                    .requests(requestsByStatus.get(status.getId()))
                    .build())
                .collect(Collectors.toList()));
    }

    public Response<Request> changeRequest(RequestDtoWithId newFields) {
        Optional<Request> requestWrapper = requestRepository.findById(newFields.getId());
        if (requestWrapper.isEmpty()) {
            return Response.BAD("Запрос с id = %d не найден", newFields.getId());
        }
        Request request = requestWrapper.get();
        updateValue(newFields.getBody(), request.getBody(), request::setBody);
        updateValue(newFields.getRequestType(), request.getRequestTypeId(), request::setRequestTypeId);

        if (newFields.getAdditionalInfo() != null) {
            if (request.getAdditionalInfo() != null) {
                Map<String, Object> oldAdditionalInfo = request.getAdditionalInfo();
                newFields.getAdditionalInfo().forEach((key, value) ->
                        updateValueNullable(value, oldAdditionalInfo.get(key),
                            newValue -> oldAdditionalInfo.put(key, newValue)));
                request.setAdditionalInfo(oldAdditionalInfo);
            } else {
                request.setAdditionalInfo(newFields.getAdditionalInfo());
            }
        }

        return Response.OK(requestRepository.save(request));
    }

    private <T> void updateValue(T newValue, T oldValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
        }
    }

    private <T> void updateValueNullable(T newValue, T oldValue, Consumer<T> setter) {
        if (!oldValue.equals(newValue)) {
            setter.accept(newValue);
        }
    }

    @Transactional
    public Response<Comment> addCommentToRequest(CommentCreate commentCreate) {
        Comment comment =  commentRepository.save(modelsFactoryService.buildComment(commentCreate));
        Optional<Request> requestWrapper = requestRepository.findById(commentCreate.getRequestId());
        if (requestWrapper.isEmpty()) {
            return Response.BAD(String.format("Запрос с id = %d не найден", commentCreate.getRequestId()));
        }
        Request request = requestWrapper.get();
        Long[] ids = new Long[request.getComments().length + 1];
        System.arraycopy(request.getComments(), 0, ids, 0, request.getComments().length);
        ids[ids.length - 1] = comment.getId();
        request.setComments(ids);
        requestRepository.save(request);
        return Response.OK(comment);
    }

    public Response<Request> changeRequestStatus(long requestId, int newStatusId) {
        Optional<Request> requestWrapper = requestRepository.findById(requestId);
        if (requestWrapper.isEmpty()) {
            return Response.BAD("Запрос с id = %d не найден", requestId);
        }

        List<RequestTransaction> transactionWrapper = requestTransactionRepository
                .findByFromAndTo(requestWrapper.get().getStatus(), newStatusId);
        if (transactionWrapper.isEmpty()) {
            return Response.BAD("Не найден возможный переход между статусами %d и %d для этого запроса",
                    requestWrapper.get().getStatus(), newStatusId);
        }

        // TODO : role check

        if (transactionWrapper.get(0).getRequestTypeIds().contains(requestWrapper.get().getRequestTypeId())) {
            Request request = requestWrapper.get();
            request.setStatus(newStatusId);
            return Response.OK(requestRepository.save(request));
        }

        return Response.BAD("Невозможно перевести запрос типа %d из статуса %d в статус %d  %d",
                requestWrapper.get().getRequestTypeId(), requestWrapper.get().getStatus(), newStatusId);
    }

}
