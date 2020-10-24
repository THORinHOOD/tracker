package com.lada.tracker.controllers.utils;

import com.lada.tracker.controllers.dto.MessageRequestWardCreate;
import com.lada.tracker.controllers.dto.RequestFromWardDto;
import com.lada.tracker.entities.Message;

import java.sql.Timestamp;

public class Converter {

    public static com.lada.tracker.entities.RequestFromWard newRequestFromWard(RequestFromWardDto createRequest) {
        Timestamp currentTime = currentTimestamp();
        return com.lada.tracker.entities.RequestFromWard.builder()
                .name(createRequest.getName())
                .email(createRequest.getEmail())
                .phone(createRequest.getPhone())
                .status(1) // TODO
                .body(createRequest.getBody())
                .trafic(createRequest.getTrafic())
                .registrationDate(currentTime)
                .lastTransaction(currentTime)
                .build();
    }

    public static Message newMessage(MessageRequestWardCreate createRequest) {
        return Message.builder()
                .creation(currentTimestamp())
                .employeeId(createRequest.getEmployeeId())
                .message(createRequest.getMessage())
                .build();
    }


    private static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis()); // TODO change
    }
}
