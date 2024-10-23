package com.roman.service;

import com.roman.dao.entity.Meeting;
import com.roman.dao.entity.Worker;
import com.roman.dao.redis.RedisRepository;
import com.roman.dao.repository.MeetingRepository;
import com.roman.service.dto.meeting.CreateMeetingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final RedisRepository redisRepository;
    private final MeetingRepository meetingRepository;
    private final WorkerService workerService;

    public CreateMeetingDto createMeeting(String directorId){
        String fullMeetingInfo = redisRepository.getFullMeetingInfo(directorId);
        CreateMeetingDto meetingDto = parseToDto(fullMeetingInfo);
        List<Worker> allWorkers = workerService.findAllWorkers(meetingDto.getWorkersId());
        LocalDateTime meetingTime = parseToLocalDateTime(meetingDto.getTime());
        meetingRepository.save(new Meeting(meetingDto.getTitle(),meetingTime, allWorkers));
        return meetingDto;
    }

    private CreateMeetingDto parseToDto(String fullInfo){
        String[] infoByPart = fullInfo.split(" ");
        String[] workersId = infoByPart[0].split(",");
        return new CreateMeetingDto(workersId,infoByPart[1] + " " + infoByPart[2],infoByPart[3]);
    }

    private LocalDateTime parseToLocalDateTime(String time){
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
