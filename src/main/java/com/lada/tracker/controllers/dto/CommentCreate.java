package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class CommentCreate {
    private String message;
    private Long userId;
    private Long requestId;
}
