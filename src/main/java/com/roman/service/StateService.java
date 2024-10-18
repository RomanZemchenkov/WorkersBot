package com.roman.service;

import com.roman.Stages;
import com.roman.dao.entity.State;
import com.roman.dao.entity.Worker;
import com.roman.dao.repository.StateRepository;
import com.roman.dao.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class StateService {

    private final StateRepository stateRepository;
    private final WorkerRepository workerRepository;

    public void updateOrCreateWorkerState(Long workerId, Stages currentStage, String currentState){
        Optional<State> mayBeWorkerState = stateRepository.findStateByWorkerId(workerId);
        mayBeWorkerState.ifPresentOrElse(state -> ifPresent(state,currentStage,currentState), () -> ifEmpty(workerId,currentStage,currentState));
    }

    private void ifPresent(State state, Stages currentStage, String currentState){
        state.setStage(currentStage);
        state.setState(currentState);

        stateRepository.save(state);
    }

    private void ifEmpty(Long workerId,Stages currentStage, String currentState){
        Worker worker = workerRepository.findById(workerId).get();
        State state = new State();
        state.setWorker(worker);

        ifPresent(state,currentStage,currentState);
    }

    public Optional<State> getWorkerState(Long workerId){
        return stateRepository.findStateByWorkerId(workerId);
    }
}
