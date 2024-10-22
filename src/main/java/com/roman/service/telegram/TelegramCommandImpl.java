package com.roman.service.telegram;

import com.roman.service.stage.OptionsState;
import com.roman.service.stage.Stages;
import com.roman.dao.entity.State;
import com.roman.service.StateService;
import com.roman.service.telegram.options.OptionsService;
import com.roman.service.telegram.help.HelpService;
import com.roman.service.telegram.registration.RegistrationService;
import com.roman.service.stage.RegistrationState;
import com.roman.service.telegram.start.StartService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Component
public class TelegramCommandImpl implements TelegramCommand {

    private final RegistrationService registrationService;
    private final HelpService helpService;
    private final StartService startService;
    private final StateService stateService;
    private final OptionsService optionsService;
    private final TelegramMessageSender telegramMessageSender;

    public TelegramCommandImpl(RegistrationService registrationService,
                               @Lazy HelpService helpService,
                               @Lazy StartService startService,
                               StateService stateService,
                               @Lazy OptionsService optionsService,
                               @Lazy TelegramMessageSender telegramMessageSender) {
        this.registrationService = registrationService;
        this.helpService = helpService;
        this.startService = startService;
        this.stateService = stateService;
        this.optionsService = optionsService;
        this.telegramMessageSender = telegramMessageSender;
    }


    @Override
    public void registration(Message message) {
        registrationService.startRegistration(message);
    }

    @Override
    public void startCommand(Message message) {
        Long currentUserId = message.getFrom().getId();
        String response = startService.checkWorker(currentUserId);
        SendMessage responseMessage = new SendMessage(String.valueOf(message.getChatId()), response);
        telegramMessageSender.sendResponse(responseMessage);
    }

    @Override
    public void helpCommand(Message message) {
        User currentUser = message.getFrom();
        String response = helpService.checkUserPost(currentUser);
        SendMessage responseMessage = new SendMessage(String.valueOf(message.getChatId()), response);
        telegramMessageSender.sendResponse(responseMessage);
    }


    @Override
    public void anotherMessage(Message message) {
        // здесь будем пытаться получить пользователя из кеша и если его, то уже из базы
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
                    RegistrationState currentRegistrationState = RegistrationState.valueOf(currentState);
                    registrationService.checkRegistrationState(message, currentRegistrationState);
                }
                case ONLINE -> {
                    OptionsState currentOptionsState = OptionsState.valueOf(currentState);
                    optionsService.checkOptionsState(message,currentOptionsState);
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
