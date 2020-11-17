package com.lada.tracker.services;

import com.lada.tracker.controllers.dto.CommentCreate;
import com.lada.tracker.controllers.dto.RequestDto;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestStatus;
import com.lada.tracker.repositories.RequestStatusRepository;
import com.lada.tracker.services.models.Response;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class ModelsFactoryService {

    private final RequestStatusRepository requestStatusRepository;

    public ModelsFactoryService(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    /**
     * Получение стартового статуса для пайплайна реквестов
     * @param requestTypeId
     * @return
     */
    private Optional<RequestStatus> getStarterStatus(int requestTypeId) {
        return requestStatusRepository.findAll().stream()
                    .filter(status -> status.getRequestTypeIds().contains(requestTypeId) &&
                                      status.getStart())
                    .findFirst();
    }

    /**
     * Построение нового реквеста
     * @param createRequest реквеста на добавление
     * @return объект реквеста
     */
    public Response<Request> buildRequest(RequestDto createRequest) {
        Timestamp currentTime = currentTimestamp();
        Optional<RequestStatus> startRequestStatus = getStarterStatus(createRequest.getRequestType());
        if (startRequestStatus.isEmpty()) {
            return Response.BAD("Нет начального статуса у запросов типа %d", createRequest.getRequestType());
        } else {
            return Response.OK(Request.builder()
                    .status(startRequestStatus.get().getId())
                    .additionalInfo(createRequest.getAdditionalInfo())
                    .body(createRequest.getBody())
                    .registrationDate(currentTime)
                    .lastTransaction(currentTime)
                    .requestTypeId(createRequest.getRequestType())
                    .build());
        }
    }

    public Comment buildComment(CommentCreate createRequest) {
        return Comment.builder()
                .creation(currentTimestamp())
                .login(createRequest.getLogin())
                .message(createRequest.getMessage())
                .build();
    }

    private static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
