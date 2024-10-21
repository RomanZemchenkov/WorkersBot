package com.roman;

import lombok.Getter;

@Getter
public enum Stages {
    REGISTRATION("REGISTRATION"),
    EMPTY_STAGE("Empty stage");

    private String info;

    Stages(String info){
        this.info = info;
    }
}
