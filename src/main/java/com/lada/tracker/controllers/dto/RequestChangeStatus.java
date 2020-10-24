package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class RequestChangeStatus {
    private long requestId;
    private int status;
}
