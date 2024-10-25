package com.roman.service;

import com.roman.dao.redis.RedisInitializer;
import com.roman.dao.redis.RedisRepository;
import com.roman.service.dto.meeting.CreateMeetingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class MeetingServiceTest extends RedisInitializer {

    private final MeetingService meetingService;
    private final RedisRepository redisRepository;
    private static final Long DIRECTOR_ID = 1L;
    private static final String[] MEETING_PART= {"participants","date","time","name"};

    @Autowired
    public MeetingServiceTest(MeetingService meetingService, RedisRepository redisRepository) {
        this.meetingService = meetingService;
        this.redisRepository = redisRepository;
    }

    @Test
    void test(){
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[0],"1,2");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[1],"2024-10-24");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[2],"13:30");
        redisRepository.saveMeetingPart(DIRECTOR_ID,MEETING_PART[3],"title");
        CreateMeetingDto meeting = meetingService.createMeeting(DIRECTOR_ID);

        System.out.println(meeting);
    }
}
