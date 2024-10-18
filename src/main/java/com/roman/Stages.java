package com.roman;

import lombok.Getter;

@Getter
public enum Stages {
    REGISTRATION("REGISTRATION");


    private String info;

    Stages(String info){
        this.info = info;
    }
}
