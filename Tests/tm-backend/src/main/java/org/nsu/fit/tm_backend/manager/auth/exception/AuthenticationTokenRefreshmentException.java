package org.nsu.fit.tm_backend.manager.auth.exception;

public class AuthenticationTokenRefreshmentException extends RuntimeException {
    public AuthenticationTokenRefreshmentException(String message) {
        super(message);
    }

    public AuthenticationTokenRefreshmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
