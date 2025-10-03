package com.league.matrix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.league.matrix.exception.MatrixProcessingException;

@RestControllerAdvice
public class MatrixExceptionHandler {

    @ExceptionHandler(MatrixProcessingException.class)
    public ResponseEntity<String> handleMatrixProcessing(MatrixProcessingException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ MissingServletRequestPartException.class, MultipartException.class })
    public ResponseEntity<String> handleMultipartIssues(Exception exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.TEXT_PLAIN)
                .body("Missing or invalid file upload: " + exception.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleSizeExceeded(MaxUploadSizeExceededException exception) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Uploaded file is too large");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Unexpected error: " + exception.getMessage());
    }
}
