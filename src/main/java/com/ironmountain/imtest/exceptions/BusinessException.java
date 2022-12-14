package com.ironmountain.imtest.exceptions;

import com.ironmountain.imtest.exceptions.messages.Msg;
import org.springframework.http.HttpStatus;

public class BusinessException extends HttpException {

    public BusinessException(Msg message) {
        super(message, HttpStatus.NOT_ACCEPTABLE);
    }

    public BusinessException(Throwable throwable) {
        super(throwable, HttpStatus.NOT_ACCEPTABLE);
    }

    public BusinessException(Msg message, Throwable throwable) {
        super(message, HttpStatus.NOT_ACCEPTABLE, throwable);
    }
}