package com.lada.tracker.controllers.dto;

import lombok.Data;

@Data
public class RequestFromWardDto {
    private String name;
    private String phone;
    private String email;
    private String body;
    private String trafic;
}
