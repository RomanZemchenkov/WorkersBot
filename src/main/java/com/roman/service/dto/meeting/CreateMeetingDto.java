package com.roman.service.dto.meeting;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateMeetingDto {

    private final String[] workersId;
    private final String time;
    private final String title;

    public CreateMeetingDto(String[] workersId, String time, String title) {
        this.workersId = workersId;
        this.time = time;
        this.title = title;
    }
}
