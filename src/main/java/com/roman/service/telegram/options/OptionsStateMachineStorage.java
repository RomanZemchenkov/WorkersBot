package com.roman.service.telegram.options;

import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
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
public class OptionsStateMachineStorage {

    private final Map<Long, StateMachine<OptionsState,OptionEvent>> stateMachineMap = new HashMap<>();
    private final StateMachineFactory<OptionsState, OptionEvent> stateMachineFactory;
    private static final Logger logger = LoggerFactory.getLogger(OptionsStateMachineStorage.class);

    @Autowired
    public OptionsStateMachineStorage(StateMachineFactory<OptionsState, OptionEvent> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<OptionsState,OptionEvent> getOrCreateStateMachine(User user){
        StateMachine<OptionsState,OptionEvent> sm = stateMachineMap.computeIfAbsent(user.getId(), id -> stateMachineFactory.getStateMachine());
        sm.startReactively();
        logger.info("StateMachine was created for user {} was created or given",user.getId());
        return sm;
    }

    public void removeStateMachine(User user){
        stateMachineMap.remove(user.getId());
    }

}
