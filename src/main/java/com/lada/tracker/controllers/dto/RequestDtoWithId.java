package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class RequestDtoWithId extends RequestDto {
    private Long id;
}
