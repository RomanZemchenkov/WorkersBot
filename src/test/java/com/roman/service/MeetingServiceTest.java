package com.roman.service;

import com.roman.dao.redis.RedisInitializer;
import com.roman.dao.redis.RedisRepository;
import com.roman.service.dto.meeting.CreateMeetingDto;
import com.roman.service.dto.meeting.ShowMeetingDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    @Test
    @DisplayName("Testing the find all meetings by worker id")
    void findAllMeetingsByWorkerId(){
        List<ShowMeetingDto> meetings = assertDoesNotThrow(() -> meetingService.findMeetings(DIRECTOR_ID));

        assertThat(meetings.size()).isEqualTo(4);
    }
}
