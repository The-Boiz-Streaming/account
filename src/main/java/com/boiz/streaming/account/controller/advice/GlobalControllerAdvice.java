package com.boiz.streaming.account.controller.advice;

import com.boiz.streaming.account.exception.CustomException;
import com.boiz.streaming.account.exception.UnsupportedRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(final CustomException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatus());
        problemDetail.setProperty("errors", List.of(ex.getMessage()));
        return ResponseEntity.status(ex.getStatus())
                .body(problemDetail);
    }

    @ExceptionHandler(UnsupportedRoleException.class)
    public ResponseEntity<ProblemDetail> handleUnsupportedRoleException(final UnsupportedRoleException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setProperty("errors", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }

}
