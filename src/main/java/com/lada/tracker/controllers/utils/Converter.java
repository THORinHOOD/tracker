package com.lada.tracker.controllers.utils;

import com.lada.tracker.controllers.dto.CommentCreate;
import com.lada.tracker.controllers.dto.RequestDto;
import com.lada.tracker.entities.Comment;
import com.lada.tracker.entities.Request;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Converter {

    public static Request newRequestFromDto(RequestDto createRequest) {
        Timestamp currentTime = currentTimestamp();
        return Request.builder()
                .status(1) // TODO
                .additionalInfo(createRequest.getAdditionalInfo())
                .body(createRequest.getBody())
                .registrationDate(currentTime)
                .lastTransaction(currentTime)
                .requestTypeId(createRequest.getRequestType())
                .build();
    }

    public static Comment newComment(CommentCreate createRequest) {
        return Comment.builder()
                .creation(currentTimestamp())
                .userId(createRequest.getUserId())
                .message(createRequest.getMessage())
                .build();
    }


    private static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis()); // TODO change
    }
}
