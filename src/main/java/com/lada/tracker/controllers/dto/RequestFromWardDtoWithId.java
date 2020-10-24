package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class RequestFromWardDtoWithId extends RequestFromWardDto {
    private Long id;
}
