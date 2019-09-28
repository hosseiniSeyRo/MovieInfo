package com.rhosseini.movieinfo.model.webServise.responseModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("unchecked")
public class ResponseWrapper<T> {

    @NonNull
    private Status status;

    @Nullable
    private T data;

    @Nullable
    private Integer errorCode;

    @Nullable
    private String errorMessage;

    public ResponseWrapper(@NonNull Status status, @Nullable T data, @Nullable Integer errorCode, @Nullable String errorMessage) {
        this.status = status;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ResponseWrapper loading() {
        return new ResponseWrapper(Status.LOADING, null, null, null);
    }

    public static ResponseWrapper success(@NonNull Object data) {
        return new ResponseWrapper(Status.SUCCESS, data, null, null);
    }

    public static ResponseWrapper error(@Nullable Integer errorCode, @NonNull String errorMessage) {
        return new ResponseWrapper(Status.ERROR, null, errorCode, errorMessage);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }

    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    public enum Status {
        CREATE,
        LOADING,
        SUCCESS,
        ERROR
    }
}
