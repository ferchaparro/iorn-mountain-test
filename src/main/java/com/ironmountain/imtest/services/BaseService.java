package com.ironmountain.imtest.services;

import com.ironmountain.imtest.exceptions.BusinessException;
import com.ironmountain.imtest.exceptions.messages.Msg;

import java.util.Objects;

public abstract class BaseService<T> {
    public T assertNotNull(T dto, Msg nullMsg) {
        if(Objects.isNull(dto)) {
            throw new BusinessException(nullMsg);
        }

        return dto;
    }

    public T assertNotExist(T dto, Msg existMsg) {
        if(assertCondition(dto)){
            throw new BusinessException(existMsg);
        }
        return dto;
    }

    public abstract boolean assertCondition(T dto);
}
