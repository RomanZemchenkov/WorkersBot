package com.roman.service.telegram.registration;

import lombok.Getter;

@Getter
public enum RegistrationState {
    WAITING("Waiting for the user to enter the command '/registration.'"),
    WAITING_POST("Waiting for the user to choose director or another"),
    WAITING_COMPANY_NAME("Waiting for the user to enter the yours company name."),
    WAITING_ANOTHER_POST("Waiting fot the user to enter yours post"),
    WAITING_DIRECTOR_USERNAME("Waiting for the user to enter the director username"),
    WAITING_BIRTHDAY("Waiting for the user to enter the yours birthday"),
    REGISTRATION_COMPLETE("Registration was successful");

    private String info;

    RegistrationState(String info){
        this.info = info;
    }
}
