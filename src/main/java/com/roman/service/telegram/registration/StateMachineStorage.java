package com.roman.service.telegram.registration;

import com.roman.service.stage.RegistrationEvent;
import com.roman.service.stage.RegistrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class StateMachineStorage {

    private final Map<Long, StateMachine<RegistrationState, RegistrationEvent>> stateMachineMap = new HashMap<>();
    private final StateMachineFactory<RegistrationState,RegistrationEvent> stateMachineFactory;
    private static final Logger logger = LoggerFactory.getLogger(StateMachineStorage.class);

    @Autowired
    public StateMachineStorage(StateMachineFactory<RegistrationState, RegistrationEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<RegistrationState,RegistrationEvent> getOrCreateStateMachine(User user){
        StateMachine<RegistrationState, RegistrationEvent> sm = stateMachineMap.computeIfAbsent(user.getId(), id -> stateMachineFactory.getStateMachine());
        sm.startReactively();
        logger.info("StateMachine was created for user {} was created or given",user.getId());
        return sm;
    }

    public void removeStateMachine(User user){
        stateMachineMap.remove(user.getId());
    }
}
