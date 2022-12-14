package com.ironmountain.imtest.transfer;

import com.ironmountain.imtest.exceptions.messages.Msg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse<T> {
    private Msg message;
    private T data;
}