package com.roman.service.telegram.registration;

import com.roman.service.stage.RegistrationEvent;
import com.roman.service.stage.RegistrationState;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class RegistrationStateMachineListener extends StateMachineListenerAdapter<RegistrationState, RegistrationEvent> {


}
