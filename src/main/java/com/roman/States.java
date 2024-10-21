package com.roman;

import lombok.Getter;

@Getter
public enum States {

    EMPTY_STATE("Empty state");

    private final String info;

    States(String info) {
        this.info = info;
    }
}
