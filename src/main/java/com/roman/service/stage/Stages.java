package com.roman.service.stage;

import lombok.Getter;

@Getter
public enum Stages {
    REGISTRATION("REGISTRATION"),
    EMPTY_STAGE("Empty stage"),
    ONLINE("Online");

    private String info;

    Stages(String info){
        this.info = info;
    }
}
