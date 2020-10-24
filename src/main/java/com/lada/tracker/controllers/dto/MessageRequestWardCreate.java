package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class MessageRequestWardCreate {
    private String message;
    private Long employeeId;
    private Long requestId;
}
