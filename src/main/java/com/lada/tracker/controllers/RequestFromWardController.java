package com.lada.tracker.controllers;

import com.lada.tracker.controllers.dto.MessageRequestWardCreate;
import com.lada.tracker.controllers.dto.RequestChangeStatus;
import com.lada.tracker.controllers.dto.RequestFromWardDto;
import com.lada.tracker.controllers.dto.RequestFromWardDtoWithId;
import com.lada.tracker.controllers.utils.Converter;
import com.lada.tracker.entities.Message;
import com.lada.tracker.entities.RequestFromWard;
import com.lada.tracker.repositories.MessageRepository;
import com.lada.tracker.repositories.RequestFromWardRepository;
import com.lada.tracker.services.RequestService;
import com.lada.tracker.services.models.KanbanColumn;
import com.lada.tracker.utils.ResultWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requests/ward")
public class RequestFromWardController {

    private final RequestFromWardRepository requestFromWardRepository;
    private final RequestService requestService;
    private final MessageRepository messageRepository;

    public RequestFromWardController(RequestFromWardRepository requestFromWardRepository,
                                     RequestService requestService,
                                     MessageRepository messageRepository) {
        this.requestFromWardRepository = requestFromWardRepository;
        this.requestService = requestService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/by_status")
    public List<RequestFromWard> getRequestsFromWards(@RequestParam int status) {
        return requestFromWardRepository.findAllByStatus(status);
    }

    @GetMapping
    public List<KanbanColumn> getAllWardRequests() {
        return requestService.getKanbanBoard();
    }


    @PostMapping("/change_status")
    public ResponseEntity<String> changeRequestWardStatus(@RequestBody RequestChangeStatus requestChangeStatus) {
        ResultWrapper resultWrapper = requestService.changeRequestWardStatus(
                requestChangeStatus.getRequestId(), requestChangeStatus.getStatus());
        if (resultWrapper.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(resultWrapper.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeRequestFromWard(@RequestBody RequestFromWardDtoWithId requestChange) {
        ResultWrapper resultWrapper = requestService.changeRequestFromWard(requestChange);
        if (resultWrapper.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(resultWrapper.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RequestFromWard> createRequestFromWard(@RequestBody RequestFromWardDto requestCreate) {
        RequestFromWard requestFromWard = Converter.newRequestFromWard(requestCreate);
        return ResponseEntity.ok().body(requestFromWardRepository.save(requestFromWard));
    }

    @GetMapping("/messages")
    public List<Message> getRequestFromWardMessages(@RequestParam List<Long> messagesIds) {
        return messageRepository.findAllById(messagesIds);
    }

    @PostMapping("/messages")
    @Transactional
    public ResponseEntity<Message> createRequestFromWardMessage(@RequestBody
                                                                MessageRequestWardCreate messageRequestWardCreate) {
        Message message = Converter.newMessage(messageRequestWardCreate);
        messageRepository.save(message);
        ResultWrapper resultWrapper = requestService.addMessageToRequest(messageRequestWardCreate.getRequestId(),
                message.getId());
        if (resultWrapper.isSuccess()) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
