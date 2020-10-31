package com.lada.tracker.controllers;

import com.lada.tracker.controllers.dto.CommentCreate;
import com.lada.tracker.controllers.dto.RequestChangeStatus;
import com.lada.tracker.controllers.dto.RequestDto;
import com.lada.tracker.controllers.dto.RequestDtoWithId;
import com.lada.tracker.controllers.utils.Converter;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestStatus;
import com.lada.tracker.entities.RequestType;
import com.lada.tracker.repositories.CommentRepository;
import com.lada.tracker.repositories.RequestRepository;
import com.lada.tracker.repositories.RequestStatusRepository;
import com.lada.tracker.repositories.RequestTypeRepository;
import com.lada.tracker.services.RequestService;
import com.lada.tracker.services.models.KanbanColumn;
import com.lada.tracker.utils.ResultWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("requests")
public class RequestController {

    private final RequestRepository requestRepository;
    private final RequestService requestService;
    private final CommentRepository commentRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final RequestStatusRepository requestStatusRepository;

    public RequestController(RequestRepository requestRepository,
                             RequestService requestService,
                             CommentRepository commentRepository,
                             RequestTypeRepository requestTypeRepository,
                             RequestStatusRepository requestStatusRepository) {
        this.requestRepository = requestRepository;
        this.requestService = requestService;
        this.commentRepository = commentRepository;
        this.requestTypeRepository = requestTypeRepository;
        this.requestStatusRepository = requestStatusRepository;
    }

    @GetMapping("/by_status")
    public List<Request> getRequestsFromWards(@RequestParam int status) {
        return requestRepository.findAllByStatus(status);
    }

    @GetMapping
    public List<KanbanColumn> getAllWardRequests() {
        return requestService.getKanbanBoard();
    }

    @PostMapping("/change_status")
    public ResponseEntity<String> changeRequestStatus(@RequestBody RequestChangeStatus requestChangeStatus) {
        ResultWrapper resultWrapper = requestService.changeRequestWardStatus(
                requestChangeStatus.getRequestId(), requestChangeStatus.getStatus());
        if (resultWrapper.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(resultWrapper.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeRequest(@RequestBody RequestDtoWithId requestChange) {
        ResultWrapper resultWrapper = requestService.changeRequest(requestChange);
        if (resultWrapper.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(resultWrapper.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Request> createRequest(@RequestBody RequestDto requestCreate) {
        Request requestFromWard = Converter.newRequestFromDto(requestCreate);
        return ResponseEntity.ok().body(requestRepository.save(requestFromWard));
    }

    @GetMapping("/messages")
    public List<Comment> getRequestComments(@RequestParam List<Long> messagesIds) {
        return commentRepository.findAllById(messagesIds);
    }

    @PostMapping("/messages")
    public ResponseEntity<Comment> createRequestComment(@RequestBody CommentCreate commentCreate) {
        try {
            return ResponseEntity.ok(requestService.addCommentToRequest(commentCreate));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/types")
    public List<RequestType> getAllRequestTypes() {
        return requestTypeRepository.findAll();
    }

}
