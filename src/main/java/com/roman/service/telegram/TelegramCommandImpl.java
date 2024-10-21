package com.roman.service.telegram;

import com.roman.Stages;
import com.roman.dao.entity.State;
import com.roman.service.StateService;
import com.roman.service.telegram.registration.RegistrationService;
import com.roman.service.telegram.registration.RegistrationState;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Component
public class TelegramCommandImpl implements TelegramCommand {

    private final RegistrationService registrationService;
    private final StateService stateService;
    private final TelegramMessageSender telegramMessageSender;

    public TelegramCommandImpl(RegistrationService registrationService,
                               StateService stateService,
                               @Lazy TelegramMessageSender telegramMessageSender) {
        this.registrationService = registrationService;
        this.stateService = stateService;
        this.telegramMessageSender = telegramMessageSender;
    }


    @Override
    public void registration(Message message) {
        registrationService.startRegistration(message);
    }

    @Override
    public void startCommand(Long chatId) {
        String message = "Привет, я пока учусь.";
    }

    @Override
    public void helpCommand(Message message) {
        User currentUser = message.getFrom();

    }


    @Override
    public void anotherMessage(Message message) {
        Optional<State> currentWorkerState = stateService.getWorkerState(message.getFrom().getId());
        if(currentWorkerState.isEmpty()){
            SendMessage response = new SendMessage(String.valueOf(message.getChatId()), "Вы ещё не зарегистрированы. Пожалуйста, введите команду /registration.");
            telegramMessageSender.sendResponse(response);
        } else {
            State state = currentWorkerState.get();
            Stages currentStage = state.getStage();
            String currentState = state.getState();
            switch (currentStage){
                case REGISTRATION -> {
                    RegistrationState currentUserState = RegistrationState.valueOf(currentState);
                    registrationService.checkRegistrationState(message,currentUserState);
                }
                case EMPTY_STAGE -> {
                    SendMessage response = new SendMessage(String.valueOf(message.getChatId()), "Команда не распознана. Пожалуйста, введите команду /help.");
                    telegramMessageSender.sendResponse(response);
                }
            }
            System.out.println(" Проблема ");
            //проверка состояния у пользователя и вызов метода или возврат сообщения с ошибкой
        }
    }

    @Override
    public SendMessage sendResponse(Long chatId, String responseMessage) {
        return new SendMessage(String.valueOf(chatId), responseMessage);
    }



}
