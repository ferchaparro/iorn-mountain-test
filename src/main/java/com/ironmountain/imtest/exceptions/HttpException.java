package com.ironmountain.imtest.exceptions;

import com.ironmountain.imtest.exceptions.messages.Msg;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class HttpException extends RuntimeException {

    private HttpStatus status;

    public HttpException(Msg message) {
        super(message.name());
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }

    public HttpException(Throwable throwable, HttpStatus status) {
        super(throwable);
        this.status = status;
    }

    public HttpException(Msg message, HttpStatus status) {
        super(message.name());
        this.status = status;
    }

    public HttpException(Msg message, HttpStatus status, Throwable throwable) {
        super(message.name(), throwable);
        this.status = status;
    }
}
