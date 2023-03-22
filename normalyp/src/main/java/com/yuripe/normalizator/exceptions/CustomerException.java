package com.yuripe.normalizator.exceptions;

@SuppressWarnings("serial")
public class CustomerException extends Exception {

    private String requestId;

    // Custom error message
    private String message;

    // Custom error code representing an error in system
    private String errorCode;

    public CustomerException (String message) {
            super(message);
            this.message = message;
        }

    public CustomerException (String requestId, String message, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
