package com.lada.tracker.controllers.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RequestDto {
    private String body;
    private Integer requestType;
    private Map<String, Object> additionalInfo;
}
