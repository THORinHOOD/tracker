package com.lada.tracker.utils;

import lombok.Getter;

public class ResultWrapper {

    public static final ResultWrapper SUCCESSFUL = new ResultWrapper().setSuccess(true);

    public static  ResultWrapper BAD(String message) {
        return new ResultWrapper()
                .setSuccess(false)
                .setMessage(message);
    }

    @Getter
    private String message;
    @Getter
    private boolean success;

    public ResultWrapper setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResultWrapper setSuccess(boolean success) {
        this.success = success;
        return this;
    }

}
