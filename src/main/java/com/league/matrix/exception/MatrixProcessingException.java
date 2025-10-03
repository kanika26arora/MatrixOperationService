package com.league.matrix.exception;

public class MatrixProcessingException extends RuntimeException {
    public MatrixProcessingException(String message) {
        super(message);
    }

    public MatrixProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
