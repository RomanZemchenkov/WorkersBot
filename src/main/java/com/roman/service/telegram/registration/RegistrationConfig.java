package com.roman.service.telegram.registration;

import com.roman.service.stage.RegistrationEvent;
import com.roman.service.stage.RegistrationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.HashSet;
import java.util.Set;

import static com.roman.service.telegram.registration.RegistrationMessage.POST_WRITE_MESSAGE;
import static com.roman.service.telegram.registration.RegistrationMessage.SUCCESSFUL_REGISTRATION_MESSAGE;
import static com.roman.service.telegram.registration.RegistrationMessage.WRITE_BIRTHDAY_MESSAGE;
import static com.roman.service.telegram.registration.RegistrationMessage.WRITE_COMPANY_NAME_MESSAGE;
import static com.roman.service.telegram.registration.RegistrationMessage.WRITE_DIRECTOR_USERNAME_MESSAGE;
import static com.roman.service.stage.RegistrationState.*;

@Configuration
@EnableStateMachineFactory(name = {"registrationMachineFactory"})
public class RegistrationConfig extends EnumStateMachineConfigurerAdapter<RegistrationState, RegistrationEvent> {

    private final RegistrationActions registrationActions;

    @Autowired
    public RegistrationConfig(RegistrationActions registrationActions) {
        this.registrationActions = registrationActions;
    }

    @Override
    public void configure(StateMachineStateConfigurer<RegistrationState, RegistrationEvent> states) throws Exception {
        states
                .withStates()
                .initial(WAITING)
                .end(REGISTRATION_COMPLETE)
                .states(new HashSet<>(Set.of(WAITING_POST,WAITING_ANOTHER_POST,WAITING_COMPANY_NAME,WAITING_DIRECTOR_USERNAME, WAITING_BIRTHDAY)));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<RegistrationState, RegistrationEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(WAITING)
                    .target(WAITING_POST)
                    .event(RegistrationEvent.START_REGISTRATION)
                    .action(registrationActions.afterRegistrationAction(), registrationActions.workerAlreadyRegisteredExceptionHandler()) //просим ввести должность
                    .and()
                .withExternal()
                    .source(WAITING_POST)
                    .target(WAITING_COMPANY_NAME)
                    .event(RegistrationEvent.ENTER_DIRECTOR_POST)
                    .action(registrationActions.afterEnterPostAction(WRITE_COMPANY_NAME_MESSAGE)) // просим ввести компанию
                    .and()
                        .withExternal()
                        .source(WAITING_COMPANY_NAME)
                        .target(WAITING_BIRTHDAY)
                        .event(RegistrationEvent.ENTER_COMPANY)
                        .action(registrationActions.afterDirectorEnterCompanyName(WRITE_BIRTHDAY_MESSAGE)) // просим ввести день рождения
                        .and()
                .withExternal()
                    .source(WAITING_POST)
                    .target(WAITING_DIRECTOR_USERNAME)
                    .event(RegistrationEvent.ENTER_ANOTHER_POST)
                    .action(registrationActions.sendNotificationToUserAction(WRITE_DIRECTOR_USERNAME_MESSAGE)) // просим ввести username директора
                    .and()
                        .withExternal()
                        .source(WAITING_DIRECTOR_USERNAME)
                        .target(WAITING_ANOTHER_POST)
                        .event(RegistrationEvent.ENTER_DIRECTOR_USERNAME)
                        .action(registrationActions.afterEnterDirectorUsername(POST_WRITE_MESSAGE), registrationActions.directorUsernameExceptionHandlerAction()) //просим ввести должность
                        .and()
                            .withExternal()
                            .source(WAITING_ANOTHER_POST)
                            .target(WAITING_BIRTHDAY)
                            .event(RegistrationEvent.ENTER_POST)
                            .action(registrationActions.afterEnterPostAction(WRITE_BIRTHDAY_MESSAGE)) //просим ввести день рождения
                            .and()
                .withExternal()
                    .source(WAITING_BIRTHDAY)
                    .target(REGISTRATION_COMPLETE)
                    .event(RegistrationEvent.ENTER_BIRTHDAY)
                    .action(registrationActions.enterBirthdayAction(SUCCESSFUL_REGISTRATION_MESSAGE),
                            registrationActions.birthdayFormatExceptionHandleAction());

    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<RegistrationState, RegistrationEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(false)
                .listener(registrationStateMachineListener());
    }

    @Bean(name = "registrationStateMachineListener")
    public RegistrationStateMachineListener registrationStateMachineListener(){
        return new RegistrationStateMachineListener();
    }
}
