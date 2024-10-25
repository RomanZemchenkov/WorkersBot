package com.roman.dao.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.roman.GlobalVariables.MEETING_PART;

@SpringBootTest
public class RedisRepositoryTest extends RedisInitializer {

    private final RedisRepository redisRepository;
    private static final Long DIRECTOR_ID = 1L;

    @Autowired
    public RedisRepositoryTest(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Test
    void test(){
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[0],"11,21,31,54,44");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[1],"2024-10-24");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[2],"13:30");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[3],"title");

        String fullMeetingInfo = redisRepository.getFullMeetingInfo(DIRECTOR_ID);
        System.out.println(fullMeetingInfo);
    }

    @Test
    @DisplayName("Testing the save and get numbers methods")
    void saveAndGetNumbers(){
        redisRepository.saveWorkerNumber(DIRECTOR_ID,1,"1");
        redisRepository.saveWorkerNumber(DIRECTOR_ID,2,"2");
        redisRepository.saveWorkerNumber(DIRECTOR_ID,3,"3");
        redisRepository.saveWorkerNumber(DIRECTOR_ID,4,"4");
        redisRepository.saveWorkerNumber(DIRECTOR_ID,5,"5");

        Map<String, String> savedWorkersNumber = redisRepository.getSavedWorkersNumber(DIRECTOR_ID);
        System.out.println(savedWorkersNumber);
    }

    @AfterEach
    void clearInfo(){
        redisRepository.deleteKey("*");
    }
}
