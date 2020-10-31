package com.lada.tracker.services.models;

import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class Response<T> {

    public static <BODY> Response<BODY> OK(String message, Object... params) {
        return (Response<BODY>) new Response<>()
                .setSuccess(true)
                .setMessage(String.format(message, params));
    }

    public static <BODY> Response<BODY> OK(BODY body, String message, Object... params) {
        Response<BODY> response = OK(message, params);
        return response.setBody(body);
    }

    public static <BODY> Response<BODY> OK(BODY body) {
        Response<BODY> response = new Response<>();
        return response.setSuccess(true).setBody(body);
    }

    public static <BODY> Response<BODY> BAD(String message, Object... params) {
        return (Response<BODY>) new Response<>()
                .setSuccess(false)
                .setMessage(message);
    }

    public static <BODY> Response<BODY> BAD(BODY body, String message, Object... params) {
        Response<BODY> response = BAD(message, params);
        return response.setBody(body);
    }

    public ResponseEntity<Response<T>> makeResponse() {
        return success ?
                ResponseEntity.ok(this) :
                ResponseEntity.badRequest().body(this);
    }

    public static <BODY> Response<BODY> EXECUTE(Supplier<BODY> supplier) {
        try {
            return Response.OK(supplier.get());
        } catch (Exception exception) {
            return Response.BAD("Неожиданная ошибка:\n" + exception.toString());
        }
    }

    public static <BODY> Response<BODY> EXECUTE_RAW(Supplier<Response<BODY>> supplier) {
        try {
            return supplier.get();
        } catch (Exception exception) {
            return Response.BAD("Неожиданная ошибка:\n" + exception.toString());
        }
    }

    private String message;
    private boolean success;
    private T body;

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Response<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getBody() {
        return body;
    }

}
