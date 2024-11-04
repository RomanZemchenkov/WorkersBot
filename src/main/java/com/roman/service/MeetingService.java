package com.roman.service;

import com.roman.dao.entity.Meeting;
import com.roman.dao.entity.Worker;
import com.roman.dao.redis.RedisRepository;
import com.roman.dao.repository.MeetingRepository;
import com.roman.service.dto.meeting.CreateMeetingDto;
import com.roman.service.dto.meeting.ShowMeetingDto;
import com.roman.service.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingService {

    private final RedisRepository redisRepository;
    private final MeetingRepository meetingRepository;
    private final WorkerService workerService;
    private final MeetingMapper meetingMapper;

    @Transactional
    public CreateMeetingDto createMeeting(Long directorId){
        String fullMeetingInfo = redisRepository.getFullMeetingInfo(directorId);
        CreateMeetingDto meetingDto = parseToDto(fullMeetingInfo);
        List<Worker> allWorkers = workerService.findAllWorkersWithMeetings(meetingDto.getWorkersId());
        LocalDateTime meetingTime = parseToLocalDateTime(meetingDto.getTime());
        Meeting meetingBeforeCreate = new Meeting(meetingDto.getTitle(), meetingTime, allWorkers);

        allWorkers.forEach(worker -> worker.getMeetings().add(meetingBeforeCreate));

        meetingRepository.save(meetingBeforeCreate);
        return meetingDto;
    }

    public List<ShowMeetingDto> findMeetings(Long userId) {
        List<Meeting> allMeetings = meetingRepository.findAllByWorkerId(userId);
        return allMeetings.stream()
                .map(meetingMapper::mapToShow)
                .toList();
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
