package com.roman.service.stage;

import lombok.Getter;

@Getter
public enum States {

    EMPTY_STATE("EMPTY_STATE"),
    CHOOSE("CHOOSE");

    private final String info;

    States(String info) {
        this.info = info;
    }
}
