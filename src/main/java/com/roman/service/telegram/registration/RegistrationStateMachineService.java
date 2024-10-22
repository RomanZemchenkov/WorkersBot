package com.roman.service.telegram.registration;

import com.roman.service.stage.RegistrationEvent;
import com.roman.service.stage.RegistrationState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;

import static com.roman.service.telegram.registration.RegistrationMessage.REGISTRATION_MESSAGE_KEY;


@Component
@RequiredArgsConstructor
public class RegistrationStateMachineService {

    private final StateMachineStorage stateMachineStorage;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationStateMachineService.class);

    public Mono<StateMachineEventResult<RegistrationState, RegistrationEvent>> createEvent(Message message, RegistrationEvent currentEvent) {
        User currentUser = message.getFrom();
        StateMachine<RegistrationState, RegistrationEvent> currentMachine = stateMachineStorage.getOrCreateStateMachine(currentUser);
        State<RegistrationState, RegistrationEvent> state = currentMachine.getState();
        logger.info("Current state: {}", state);

        return currentMachine.startReactively()
                .then(sendEvent(currentMachine, message , currentEvent));

    }

    public Mono<StateMachineEventResult<RegistrationState, RegistrationEvent>> sendEvent(StateMachine<RegistrationState, RegistrationEvent> sm,
                                                                                         Message message, RegistrationEvent currentEvent) {
        org.springframework.messaging.Message<RegistrationEvent> eventMessage = MessageBuilder
                .withPayload(currentEvent)
                .setHeader(REGISTRATION_MESSAGE_KEY, message)
                .build();

        return sm.sendEvent(Mono.just(eventMessage)).next();
    }
}
