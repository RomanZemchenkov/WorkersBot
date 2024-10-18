package com.roman.service.telegram.registration;

import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class RegistrationStateMachineListener extends StateMachineListenerAdapter<RegistrationState,RegistrationEvent> {


}
