package com.roman.dao.redis;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest extends RedisInitializer {

    private final RedisRepository redisRepository;
    private static final String DIRECTOR_ID = "1";
    private static final String[] MEETING_PART= {"participants","date","time","name"};

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

    @AfterEach
    void clearInfo(){
        redisRepository.deleteKey("*");
    }
}
