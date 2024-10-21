package com.roman.service.exception;

import static com.roman.service.telegram.registration.RegistrationMessage.USERNAME_DOESNT_EXIST_EXCEPTION_MESSAGE;

public class UsernameDoesntExistException extends RuntimeException{

    public UsernameDoesntExistException(String message){
        super(USERNAME_DOESNT_EXIST_EXCEPTION_MESSAGE.formatted(message));
    }
}
