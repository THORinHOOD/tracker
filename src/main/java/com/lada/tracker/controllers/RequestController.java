package com.lada.tracker.controllers;

import com.lada.tracker.controllers.dto.*;
import com.lada.tracker.controllers.utils.Converter;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestType;
import com.lada.tracker.repositories.CommentRepository;
import com.lada.tracker.repositories.RequestRepository;
import com.lada.tracker.repositories.RequestStatusRepository;
import com.lada.tracker.repositories.RequestTypeRepository;
import com.lada.tracker.services.RequestService;
import com.lada.tracker.services.models.Response;
import com.lada.tracker.services.models.KanbanColumn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Response<List<Request>>> getRequests(@RequestParam int status) {
        return Response
                .EXECUTE(() -> requestRepository.findAllByStatus(status))
                .makeResponse();
    }

    @GetMapping
    public ResponseEntity<Response<Request>> getRequest(@RequestParam Long id) {
        return Response
                .EXECUTE_RAW(() -> {
                    Optional<Request> request = requestRepository.findById(id);
                    if (request.isPresent()) {
                        return Response.OK(request.get());
                    } else {
                        return Response.BAD("Не найден запрос с id = %d", id);
                    }
                })
                .makeResponse();
    }

    @GetMapping("/kanban")
    public ResponseEntity<Response<List<KanbanColumn>>> getAllWardRequests(@RequestParam Integer requestTypeId) {
        return requestService
                .getKanbanBoard(requestTypeId)
                .makeResponse();
    }

    @PostMapping("/change_status")
    public ResponseEntity<Response<Request>> changeRequestStatus(@RequestBody RequestChangeStatus requestChangeStatus) {
        return requestService
                .changeRequestStatus(requestChangeStatus.getRequestId(), requestChangeStatus.getStatus())
                .makeResponse();
    }

    @PostMapping("/change")
    public ResponseEntity<Response<Request>> changeRequest(@RequestBody RequestDtoWithId requestChange) {
        return requestService
                .changeRequest(requestChange)
                .makeResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<Response<Request>> createRequest(@RequestBody RequestDto requestCreate) {
        return Response
                .EXECUTE(() -> {
                    Request request = Converter.newRequestFromDto(requestCreate);
                    return requestRepository.save(request);
                })
                .makeResponse();
    }

    @GetMapping("/comments")
    public ResponseEntity<Response<List<Comment>>> getRequestComments(@RequestParam List<Long> commentsIds) {
        return Response
                .EXECUTE(() -> commentRepository.findAllById(commentsIds))
                .makeResponse();
    }

    @PostMapping("/comments")
    public ResponseEntity<Response<Comment>> createRequestComment(@RequestBody CommentCreate commentCreate) {
        return requestService
                .addCommentToRequest(commentCreate)
                .makeResponse();
    }

    @GetMapping("/types")
    public ResponseEntity<Response<List<RequestType>>> getAllRequestTypes() {
        return Response
                .EXECUTE(requestTypeRepository::findAll)
                .makeResponse();
    }

}
