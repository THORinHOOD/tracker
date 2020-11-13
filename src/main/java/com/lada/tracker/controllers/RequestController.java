package com.lada.tracker.controllers;

import com.lada.tracker.controllers.dto.*;
import com.lada.tracker.services.ModelsFactoryService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("requests")
public class RequestController {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");

    private final RequestRepository requestRepository;
    private final RequestService requestService;
    private final CommentRepository commentRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final ModelsFactoryService modelsFactoryService;

    public RequestController(RequestRepository requestRepository,
                             RequestService requestService,
                             CommentRepository commentRepository,
                             RequestTypeRepository requestTypeRepository,
                             RequestStatusRepository requestStatusRepository,
                             ModelsFactoryService modelsFactoryService) {
        this.requestRepository = requestRepository;
        this.requestService = requestService;
        this.commentRepository = commentRepository;
        this.requestTypeRepository = requestTypeRepository;
        this.modelsFactoryService = modelsFactoryService;
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
        return Response
                .EXECUTE_RAW(() -> requestService.getKanbanBoard(requestTypeId))
                .makeResponse();
    }

    @GetMapping("/v2/kanban")
    public ResponseEntity<Response<List<KanbanColumn>>> getAllWardRequestsFiltered(
            @RequestParam Integer requestTypeId,
            @RequestParam(required = false) String body,
            @RequestParam(required = false, name = "registerAfter") String after,
            @RequestParam(required = false, name = "registerBefore") String before) {
        return Response
                .EXECUTE_RAW(() -> requestService.getKanbanBoardFiltered(requestTypeId, body, parseDate(after),
                        parseDate(before)))
                .makeResponse();
    }

    @PostMapping("/change_status")
    public ResponseEntity<Response<Request>> changeRequestStatus(@RequestBody RequestChangeStatus requestChangeStatus) {
        return Response
                .EXECUTE_RAW(() -> requestService.changeRequestStatus(requestChangeStatus.getRequestId(),
                        requestChangeStatus.getStatus()))
                .makeResponse();
    }

    @PostMapping("/change")
    public ResponseEntity<Response<Request>> changeRequest(@RequestBody RequestDtoWithId requestChange) {
        return Response
                .EXECUTE_RAW(() -> requestService.changeRequest(requestChange))
                .makeResponse();
    }

    @PostMapping("/create")
    public ResponseEntity<Response<Request>> createRequest(@RequestBody RequestDto requestCreate) {
        return Response
                .EXECUTE_RAW(() -> {
                    Response<Request> request = modelsFactoryService.buildRequest(requestCreate);
                    if (!request.isSuccess()) {
                        return request;
                    }
                    return Response.EXECUTE(() -> requestRepository.save(request.getBody()));
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
        return Response
                .EXECUTE_RAW(() -> requestService.addCommentToRequest(commentCreate))
                .makeResponse();
    }

    @GetMapping("/types")
    public ResponseEntity<Response<List<RequestType>>> getAllRequestTypes() {
        return Response
                .EXECUTE(requestTypeRepository::findAll)
                .makeResponse();
    }

    private Timestamp parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            Date parsedDate = dateFormat.parse(date);
            return new java.sql.Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("Дата в неверном формате : " + date);
        }
    }
}
