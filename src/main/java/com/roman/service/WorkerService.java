package com.roman.service;

import com.roman.dao.entity.Worker;
import com.roman.dao.repository.WorkerRepository;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createWorker(RegistrationWorkerDto workerDto){
        String workerId = workerDto.getWorkerId();
        Worker worker = new Worker(Long.parseLong(workerId));
        workerRepository.save(worker);
    }

//    public Worker findWorkerByUsername(String workerUsername){
//        Optional<Worker> mayBeWorker = workerRepository.findWorkerByUsername(workerUsername);
//        if(mayBeWorker.isEmpty()){
//            throw new RuntimeException("Потом сделаю");
//        }
//        return mayBeWorker.get();
//    }


}
