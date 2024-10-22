package com.roman.service.exception;

public class MessageFormatException extends RuntimeException{

    public MessageFormatException(String infoAboutException){
        super(infoAboutException);
    }
}
