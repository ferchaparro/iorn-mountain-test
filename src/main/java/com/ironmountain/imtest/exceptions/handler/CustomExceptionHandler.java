package com.ironmountain.imtest.exceptions.handler;

import com.ironmountain.imtest.exceptions.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private Map<String, Object> getResponseBody(Exception e, HttpStatus status){
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status.value());
        responseBody.put("error", status.getReasonPhrase());
        responseBody.put("message", e.getMessage());
        responseBody.put("exception", e.getClass().toString());
        if(e.getCause() != null) {
            responseBody.put("cause", e.getCause().getClass().toString());
        }
        return responseBody;
    }

    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity<Object> handleCustomException(HttpException e, WebRequest req) {
        return ResponseEntity.status(e.getStatus())
                .body(getResponseBody(e, e.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleCustomException(Exception e, WebRequest req) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(getResponseBody(e, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}