package me.tgmerge.rxjavaplayground._backbone.network;

public class ApiException extends RuntimeException {
    int status;
    private String message;
    ApiException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}