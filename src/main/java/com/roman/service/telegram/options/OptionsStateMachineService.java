package com.roman.service.telegram.options;

import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
import com.roman.service.stage.RegistrationEvent;
import com.roman.service.stage.RegistrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import reactor.core.publisher.Mono;

@Service
public class OptionsStateMachineService {

    private final OptionsStateMachineStorage storage;
    private static final Logger logger = LoggerFactory.getLogger(OptionsStateMachineService.class);

    @Autowired
    public OptionsStateMachineService(OptionsStateMachineStorage storage) {
        this.storage = storage;
    }

    public Mono<StateMachineEventResult<OptionsState, OptionEvent>> createEvent(Message message, OptionEvent event){
        User currentUser = message.getFrom();
        StateMachine<OptionsState, OptionEvent> currentMachine = storage.getOrCreateStateMachine(currentUser);
        State<OptionsState, OptionEvent> state = currentMachine.getState();
        logger.info("Current state: {}", state);

        return currentMachine
                .startReactively()
                .then(sendEvent(currentMachine,message,event));
    }

    private Mono<StateMachineEventResult<OptionsState, OptionEvent>> sendEvent(StateMachine<OptionsState,OptionEvent> sm,
                                                                               Message message, OptionEvent event){
        org.springframework.messaging.Message<OptionEvent> eventMessage = MessageBuilder
                .withPayload(event)
                .setHeader("message", message)
                .build();
        return sm.sendEvent(Mono.just(eventMessage)).next();
    }
}
