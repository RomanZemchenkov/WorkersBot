package com.roman.service;

import com.roman.dao.entity.PersonalToken;
import com.roman.dao.entity.Worker;
import com.roman.dao.repository.PersonalTokenRepository;
import com.roman.dao.repository.worker.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
public class PersonalTokenService {

    private static final Random random = new Random();
    private final PersonalTokenRepository personalTokenRepository;
    private final WorkerRepository workerRepository;

    @Autowired
    public PersonalTokenService(PersonalTokenRepository personalTokenRepository, WorkerRepository workerRepository) {
        this.personalTokenRepository = personalTokenRepository;
        this.workerRepository = workerRepository;
    }

    @Transactional()
    public PersonalToken createToken(Long workerId){
        Worker currentWorker = workerRepository.findById(workerId).get();
        PersonalToken personalToken = generateToken();
        personalToken.setWorker(currentWorker);
        return personalTokenRepository.save(personalToken);
    }

    public PersonalToken generateToken(){
        StringBuilder token = new StringBuilder();
        for(int i = 0; i < 32; i++){
            int randomChar = random.nextInt(74) + 48;
            token.append((char) randomChar);
        }
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 16; i++){
            int randomChar = random.nextInt(74) + 48;
            password.append((char) randomChar);
        }
        return new PersonalToken(token.toString(),password.toString());
    }
}
