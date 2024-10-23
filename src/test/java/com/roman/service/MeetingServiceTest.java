package com.roman.service;

import com.roman.dao.redis.RedisInitializer;
import com.roman.dao.redis.RedisRepository;
import com.roman.service.dto.meeting.CreateMeetingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class MeetingServiceTest extends RedisInitializer {

    private final MeetingService meetingService;
    private final RedisRepository redisRepository;
    private static final String DIRECTOR_ID = "1";
    private static final String[] MEETING_PART= {"participants","date","time","name"};

    @Autowired
    public MeetingServiceTest(MeetingService meetingService, RedisRepository redisRepository) {
        this.meetingService = meetingService;
        this.redisRepository = redisRepository;
    }

    @Test
    @Transactional
    void test(){
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[0],"2");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[1],"2024-10-24");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[2],"13:30");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[3],"title");
        CreateMeetingDto meeting = meetingService.createMeeting("1");

        System.out.println(meeting);
    }
}
