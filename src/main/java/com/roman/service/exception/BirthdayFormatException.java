package com.roman.service.exception;

import static com.roman.service.telegram.registration.RegistrationMessage.BIRTHDAY_EXCEPTION_MESSAGE;

public class BirthdayFormatException extends RuntimeException{

    public BirthdayFormatException(){
        super(BIRTHDAY_EXCEPTION_MESSAGE);
    }
}
