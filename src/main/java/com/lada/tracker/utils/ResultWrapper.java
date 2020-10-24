package com.lada.tracker.utils;

import lombok.Getter;

public class ResultWrapper {

    public static final ResultWrapper SUCCESSFUL = new ResultWrapper().setSuccess(true);

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
