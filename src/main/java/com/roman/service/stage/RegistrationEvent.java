package com.roman.service.stage;

import lombok.Getter;

@Getter
public enum RegistrationEvent {

    START_REGISTRATION("User was enter the command '/registration'"),
    ENTER_DIRECTOR_POST("User was enter director post"),
    ENTER_DIRECTOR_USERNAME("User was enter director username"),
    ENTER_ANOTHER_POST("User was enter another post"),
    ENTER_POST("User was enter yours post"),
    ENTER_COMPANY("User was enter the company name"),
    ENTER_BIRTHDAY("User was enter the yours birthday");

    private String info;

    RegistrationEvent(String info){
        this.info = info;
    }
}
