package com.roman.service.telegram.registration;

import com.roman.Stages;
import com.roman.service.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationStateMachineService stateMachineService;
    private final StateService stateService;

    public void startRegistration(Message message) {
        stateMachineService.createEvent(message, RegistrationEvent.START_REGISTRATION)
                .subscribe(this::handleResult);
    }

    public void checkRegistrationState(Message message, RegistrationState state) {
        switch (state) {
            case WAITING_COMPANY_NAME -> sendEvent(message,RegistrationEvent.ENTER_COMPANY);
            case WAITING_POST -> {
                String post = message.getText();
                sendEvent(message,post.equals("director") ? RegistrationEvent.ENTER_DIRECTOR_POST : RegistrationEvent.ENTER_ANOTHER_POST);
            }
            case WAITING_DIRECTOR_USERNAME -> sendEvent(message, RegistrationEvent.ENTER_DIRECTOR_USERNAME);
            case WAITING_ANOTHER_POST -> sendEvent(message, RegistrationEvent.ENTER_POST);
            case WAITING_BIRTHDAY -> sendEvent(message,RegistrationEvent.ENTER_BIRTHDAY);
        }
    }

    private void sendEvent(Message message, RegistrationEvent event){
        stateMachineService.createEvent(message, event)
                .subscribe(this::handleResult);
    }

    private void handleResult(StateMachineEventResult<RegistrationState, RegistrationEvent> result) {
        if (result.getResultType() == StateMachineEventResult.ResultType.ACCEPTED) {
            // Логика при успешной отправке события
            writeState(result);
        }
        System.out.println(result.getResultType());
    }

    private void writeState(StateMachineEventResult<RegistrationState, RegistrationEvent> result) {
        Message message = result.getMessage().getHeaders().get("message", Message.class);
        Long workerId = message.getFrom().getId();
        RegistrationState currentStateAfterDoEvent = result.getRegion().getState().getId();
        stateService.updateOrCreateWorkerState(workerId, Stages.REGISTRATION, currentStateAfterDoEvent.name());
        //если всё хорошо, то мы обновляем состояние для этого пользователя в бд
    }
}
