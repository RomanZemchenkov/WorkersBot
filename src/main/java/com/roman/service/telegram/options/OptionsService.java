package com.roman.service.telegram.options;

import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;


@Service
@RequiredArgsConstructor
public class OptionsService {

    private final OptionsStateMachineService optionsStateMachineService;

    public void checkOptionsState(Message message, OptionsState currentState){
        //тут уже из кеша будем получать самого пользователя ??? надо ли оно нам
        switch (currentState){
            //после определения состояния мы будем расшифровывать команду и по ней отправлять событие
            case CHOOSE -> {
                String currentCommand = message.getText();
                switch (currentCommand){
                    case "/workers" -> sendEvent(message,OptionEvent.WANT_TO_WATCH_WORKERS);
                    case "/meetings" -> sendEvent(message, OptionEvent.WANT_TO_OPEN_MEETING_MENU);
                }
            }
            case OBSERVED_WORKERS -> sendEvent(message, OptionEvent.WANT_TO_WATCH_ONE_WORKER);
            case OBSERVED_WORKER -> {
                int length = message.getText().split(" ").length;
                switch (length){
//                    case 1 -> sendEvent(message, OptionEvent.BACK);
                    case 2 -> sendEvent(message, OptionEvent.WANT_TO_CALLING_WORKER);
                    case 3, 4 -> sendEvent(message, OptionEvent.WANT_TO_CALLING_WORKER_BY_TIME);
                }
            }
            case CHOOSE_MEETING_OPERATION -> {
                String currentCommand = message.getText();
                switch (currentCommand){
                    case "/addMeeting" -> sendEvent(message, OptionEvent.WANT_TO_CREATE_MEETING);
                }
            }
            case CREATE_MEETING -> sendEvent(message,OptionEvent.WANT_TO_ADD_PARTICIPANTS);
            case ADD_MEETING_PARTICIPANTS -> sendEvent(message,OptionEvent.WANT_TO_ADD_TIME);
            case ADD_MEETING_TIME -> sendEvent(message,OptionEvent.WANT_TO_ADD_TITLE);

        }
    }

    private void sendEvent(Message message, OptionEvent optionEvent){
        optionsStateMachineService.createEvent(message,optionEvent)
                .subscribe(this::handleResult);
    }

    private void handleResult(StateMachineEventResult<OptionsState, OptionEvent> result) {
        if (result.getResultType() == StateMachineEventResult.ResultType.ACCEPTED) {
            // Логика при успешной отправке события
            writeState(result);
        }
    }

    private void writeState(StateMachineEventResult<OptionsState, OptionEvent> result) {
        //Из кеша достаём пользователя и меняем его остояния, не делая запись в базу
        //В базу будем записывать только по окончанию работы пользователя
        Message message = result.getMessage().getHeaders().get("message", Message.class);
        Long workerId = message.getFrom().getId();
        System.out.println("Данные перезаписаны");
        //тут обновляем данные
    }
}
