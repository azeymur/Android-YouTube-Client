package com.zeymur.youtubeclient;

public class RequestState {
    private STATE state;
    private String message;
    private IRetry retry;

    public RequestState(STATE state) {
        this.state = state;
        this.message = null;
        this.retry = null;
    }

    public RequestState(STATE state, String message, IRetry retry) {
        this.state = state;
        this.message = message;
        this.retry = retry;
    }

    public String getMessage() { return message; }

    public IRetry getRetry() { return retry; }

    public STATE getState() { return state; }

    public enum STATE {
        RUNNING,
        SUCCESS,
        FAILED,
    }
}
