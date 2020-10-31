package com.lada.tracker.services;

import com.lada.tracker.controllers.dto.CommentCreate;
import com.lada.tracker.controllers.dto.RequestDtoWithId;
import com.lada.tracker.controllers.utils.Converter;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestTransaction;
import com.lada.tracker.repositories.CommentRepository;
import com.lada.tracker.repositories.RequestRepository;
import com.lada.tracker.repositories.RequestStatusRepository;
import com.lada.tracker.repositories.RequestTransactionRepository;
import com.lada.tracker.services.models.KanbanColumn;
import com.lada.tracker.utils.ResultWrapper;
import org.json.JSONObject;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestTransactionRepository requestTransactionRepository;
    private final RequestStatusRepository requestStatusRepository;
    private final CommentRepository commentRepository;

    public RequestService(RequestRepository requestRepository,
                          RequestTransactionRepository requestTransactionRepository,
                          RequestStatusRepository requestStatusRepository,
                          CommentRepository commentRepository) {
        this.requestRepository = requestRepository;
        this.requestTransactionRepository = requestTransactionRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.commentRepository = commentRepository;
    }

    public List<KanbanColumn> getKanbanBoard() {
        return requestStatusRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(status ->
                    KanbanColumn.builder()
                        .statusInfo(status)
                        .requests(requestRepository.findAllByStatus(status.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public ResultWrapper changeRequest(RequestDtoWithId newFields) {
        Optional<Request> requestWrapper = requestRepository.findById(newFields.getId());
        if (requestWrapper.isEmpty()) {
            return ResultWrapper.BAD(String.format("Request with id = %d not found", newFields.getId()));
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

        requestRepository.save(request);
        return ResultWrapper.SUCCESSFUL;
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
    public Comment addCommentToRequest(CommentCreate commentCreate) {
        Comment comment =  commentRepository.save(Converter.newComment(commentCreate));
        Optional<Request> requestWrapper = requestRepository.findById(commentCreate.getRequestId());
        if (requestWrapper.isEmpty()) {
            throw new IllegalArgumentException(String.format("Request with id = %d not found",
                    commentCreate.getRequestId()));
        }
        Request request = requestWrapper.get();
        Long[] ids = new Long[request.getComments().length + 1];
        System.arraycopy(request.getComments(), 0, ids, 0, request.getComments().length);
        ids[ids.length - 1] = comment.getId();
        request.setComments(ids);
        requestRepository.save(request);
        return comment;
    }

    public ResultWrapper changeRequestWardStatus(long requestId, int newStatusId) {
        Optional<Request> requestWrapper = requestRepository.findById(requestId);
        if (requestWrapper.isEmpty()) {
            return new ResultWrapper()
                .setSuccess(false)
                .setMessage(String.format("Request with id = %d not found", requestId));
        }

        Optional<RequestTransaction> transactionWrapper = requestTransactionRepository
                .findByFromAndTo(requestWrapper.get().getStatus(), newStatusId);
        if (transactionWrapper.isEmpty()) {
            return new ResultWrapper()
                .setSuccess(false)
                .setMessage(String.format("Can't find status transaction from %d to %d",
                        requestWrapper.get().getStatus(), newStatusId));
        }

        // TODO : role check

        if (transactionWrapper.get().getRequestTypeIds().contains(requestWrapper.get().getRequestTypeId())) {
            Request request = requestWrapper.get();
            request.setStatus(newStatusId);
            requestRepository.save(request);
            return ResultWrapper.SUCCESSFUL;
        }

        return ResultWrapper.BAD(String.format("Can't change status from %d to %d for request of type %d",
                requestWrapper.get().getStatus(), newStatusId, requestWrapper.get().getRequestTypeId()));
    }

}
