package com.lada.tracker.services;

import com.lada.tracker.controllers.dto.RequestFromWardDtoWithId;
import com.lada.tracker.entities.RequestFromWard;
import com.lada.tracker.entities.RequestFromWardTransaction;
import com.lada.tracker.repositories.RequestFromWardRepository;
import com.lada.tracker.repositories.RequestFromWardTransactionRepository;
import com.lada.tracker.utils.ResultWrapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class RequestService {

    private RequestFromWardRepository requestFromWardRepository;
    private RequestFromWardTransactionRepository requestFromWardTransactionRepository;

    public RequestService(RequestFromWardRepository requestFromWardRepository,
                          RequestFromWardTransactionRepository requestFromWardTransactionRepository) {
        this.requestFromWardRepository = requestFromWardRepository;
        this.requestFromWardTransactionRepository = requestFromWardTransactionRepository;
    }

    public ResultWrapper changeRequestFromWard(RequestFromWardDtoWithId newFields) {
        Optional<RequestFromWard> requestWrapper = requestFromWardRepository.findById(newFields.getId());
        if (requestWrapper.isEmpty()) {
            return new ResultWrapper()
                    .setSuccess(false)
                    .setMessage(String.format("Request with id = %d not found", newFields.getId()));
        }
        RequestFromWard requestFromWard = requestWrapper.get();
        updateValue(newFields.getBody(), requestFromWard.getBody(), requestFromWard::setBody);
        updateValue(newFields.getEmail(), requestFromWard.getEmail(), requestFromWard::setEmail);
        updateValue(newFields.getName(), requestFromWard.getName(), requestFromWard::setName);
        updateValue(newFields.getPhone(), requestFromWard.getPhone(), requestFromWard::setPhone);
        updateValue(newFields.getTrafic(), requestFromWard.getTrafic(), requestFromWard::setTrafic);
        requestFromWardRepository.save(requestFromWard);
        return ResultWrapper.SUCCESSFUL;
    }

    private <T> void updateValue(T newValue, T oldValue, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(oldValue)) {
            setter.accept(newValue);
        }
    }

    public ResultWrapper addMessageToRequest(long requestId, long messageId) {
        Optional<RequestFromWard> requestWrapper = requestFromWardRepository.findById(requestId);
        if (requestWrapper.isEmpty()) {
            return new ResultWrapper()
                    .setSuccess(false)
                    .setMessage(String.format("Request with id = %d not found", requestId));
        }
        RequestFromWard request = requestWrapper.get();
        Long[] ids = new Long[request.getMessageIds().length + 1];
        System.arraycopy(request.getMessageIds(), 0, ids, 0, request.getMessageIds().length);
        ids[ids.length - 1] = messageId;
        request.setMessageIds(ids);
        requestFromWardRepository.save(request);
        return ResultWrapper.SUCCESSFUL;
    }

    public ResultWrapper changeRequestWardStatus(long requestId, int newStatusId) {
        Optional<RequestFromWard> requestWrapper = requestFromWardRepository.findById(requestId);
        if (requestWrapper.isEmpty()) {
            return new ResultWrapper()
                .setSuccess(false)
                .setMessage(String.format("Request with id = %d not found", requestId));
        }

        Optional<RequestFromWardTransaction> transactionWrapper = requestFromWardTransactionRepository
                .findByFromAndTo(requestWrapper.get().getStatus(), newStatusId);
        if (transactionWrapper.isEmpty()) {
            return new ResultWrapper()
                .setSuccess(false)
                .setMessage(String.format("Can't find status transaction from %d to %d",
                        requestWrapper.get().getStatus(), newStatusId));
        }

        // TODO : role check

        RequestFromWard request = requestWrapper.get();
        request.setStatus(newStatusId);
        requestFromWardRepository.save(request);

        return ResultWrapper.SUCCESSFUL;
    }

}
