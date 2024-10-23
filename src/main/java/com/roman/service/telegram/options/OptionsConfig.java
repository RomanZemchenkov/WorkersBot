package com.roman.service.telegram.options;

import com.roman.service.stage.OptionEvent;
import com.roman.service.stage.OptionsState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.HashSet;
import java.util.Set;

import static com.roman.service.stage.OptionsState.*;

@Configuration
@EnableStateMachineFactory(name = "operationsStatesMachineFactory")
public class OptionsConfig extends StateMachineConfigurerAdapter<OptionsState, OptionEvent> {

    private final OptionsActions actions;

    @Autowired
    public OptionsConfig(OptionsActions actions) {
        this.actions = actions;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OptionsState, OptionEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OptionsState, OptionEvent> states) throws Exception {
        states
                .withStates()
                .initial(CHOOSE)
                .end(EMPTY)
                .states(new HashSet<>(Set.of(OBSERVED_WORKERS,
                        OBSERVED_MEETINGS,
                        CHOOSE_WORKER_FOR_CALLING,
                        WILL_CHANGE_WORKER_INFORMATION,
                        CHOOSE_MEETING_OPERATION,
                        CREATE_MEETING,
                        ADD_MEETING_PARTICIPANTS,
                        ADD_MEETING_TIME,
                        ADD_MEETING_TITLE,
                        OBSERVED_WORKER)));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OptionsState, OptionEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(CHOOSE)
                .target(OBSERVED_WORKERS)
                .event(OptionEvent.WANT_TO_WATCH_WORKERS)
                .action(actions.watchWorkersAction())
                .and()
                    .withExternal()
                    .source(OBSERVED_WORKERS)
                    .target(OBSERVED_WORKER)
                    .event(OptionEvent.WANT_TO_WATCH_ONE_WORKER)
                    .action(actions.watchOneWorkerAction())
                    .and()
                        .withExternal()
                        .source(OBSERVED_WORKER)
                        .target(OBSERVED_WORKERS)
                        .event(OptionEvent.WANT_TO_CALLING_WORKER)
                        .action(actions.callingWorkerAction())
                        .and()
                        .withExternal()
                        .source(OBSERVED_WORKER)
                        .target(OBSERVED_WORKERS)
                        .event(OptionEvent.WANT_TO_CALLING_WORKER_BY_TIME)
                        .action(actions.callingWorkerWithTimeAction())
                        .and()
                .withExternal()
                .source(CHOOSE)
                .target(CHOOSE_MEETING_OPERATION)
                .event(OptionEvent.WANT_TO_OPEN_MEETING_MENU)
                .action(actions.callingMeetingMenu())
                .and()
                    .withExternal()
                    .source(CHOOSE_MEETING_OPERATION)
                    .target(CREATE_MEETING)
                    .event(OptionEvent.WANT_TO_CREATE_MEETING)
                    .action(actions.callingCreateMeetingAction())
                    .and()
                        .withExternal()
                        .source(CREATE_MEETING)
                        .target(ADD_MEETING_PARTICIPANTS)
                        .event(OptionEvent.WANT_TO_ADD_PARTICIPANTS)
                        .action(actions.settingMeetingParticipantsAction())
                        .and()
                            .withExternal()
                            .source(ADD_MEETING_PARTICIPANTS)
                            .target(ADD_MEETING_TIME)
                            .event(OptionEvent.WANT_TO_ADD_TIME)
                            .action(actions.settingMeetingTimeAction())
                            .and()
                                .withExternal()
                                .source(ADD_MEETING_TIME)
                                .target(ADD_MEETING_TITLE)
                                .event(OptionEvent.WANT_TO_ADD_TITLE)
                                .action(actions.settingMeetingTitleAction())
                                .and();

    }
}
