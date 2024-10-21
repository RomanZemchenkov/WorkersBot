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
    public static final String WRITE_BIRTHDAY_MESSAGE = "Пожалуйста, введи ваш день рождения.\n" +
                                                        "Формат ввода: yyyy-mm-dd.\n" +
                                                        "Пример: 1970-01-01.";
    public static final String WRITE_COMPANY_NAME_MESSAGE = "Пожалуйста, введи название вашей компании.";

    public static final String BIRTHDAY_EXCEPTION_MESSAGE = "Неправильный формат ввода.\n" +
                                                            "Введите дату в формате yyyy-mm-dd.\n" +
                                                            "Пример: 1970-01-01.";
    public static final String USERNAME_DOESNT_EXIST_EXCEPTION_MESSAGE = "Пользователя с username '%s' не существует.";
}
