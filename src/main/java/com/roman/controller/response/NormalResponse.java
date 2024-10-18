package com.roman.controller.response;

import lombok.Getter;

@Getter
public class NormalResponse extends Response{

    private final String message;

    public NormalResponse(boolean result, String message) {
        super(result);
        this.message = message;
    }
}
