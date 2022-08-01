package com.zeymur.youtubeclient.model;

import java.util.List;

public class RequestError {
    private Error error;

    public RequestError(String message) {
        this.error = new Error(message);
    }

    public String getMessage() {
        if (error.errors != null && error.errors.size() > 0)
            return String.format("%s (%s)", error.message, error.errors.get(0).reason);
        else
            return error.message;
    }

    // Inner Classes

    public class Error {
        private List<InnerError> errors;
        private String message;

        public Error(String message) {
            this.message = message;
        }
    }

    public class InnerError {
        private String domain;
        private String reason;
        private String message;
    }
}
