package com.roman.service.telegram.registration;

public class RegistrationMessage {

    public static final String DIRECTOR_POST = "director";
    public static final String ANOTHER_POST = "another";
    public static final String REGISTRATION_MESSAGE_KEY = "message";
    public static final String START_REGISTRATION_MESSAGE = "Спасибо за регистрацию. Ваша учётная запись была создана и сохранена. Для вас были сгерерированы:\n" +
                                                            "1. Токен - **%s**,\n" +
                                                            "2. Пароль - **%s**.\n" +
                                                            "Они будут необходимы в случае смены телеграмм аккаунта, чтобы заново войти.\n" +
                                                            "Теперь попрошу вас выбрать вашу должность.\n";
    public static final String SUCCESSFUL_REGISTRATION_MESSAGE = "Спасибо за регистрацию.";
    public static final String POST_WRITE_MESSAGE = "Пожалуйста, напишите название вашей должности.";
    public static final String WRITE_DIRECTOR_USERNAME_MESSAGE = "Пожалуйста, введи username вашего директора в форме 'username' or '@username'.";
    public static final String WRITE_BIRTHDAY_MESSAGE = "Пожалуйста, введи ваш день рождения.";
    public static final String WRITE_COMPANY_NAME_MESSAGE = "Пожалуйста, введи название вашей компании.";
}
