package com.roman.service.dto.meeting;

import lombok.Getter;

@Getter
public class ShowMeetingDto {

    private final String id;
    private final String title;
    private final String time;

    public ShowMeetingDto(String id, String title, String time) {
        this.id = id;
        this.title = title;
        this.time = time;
    }


    public String toString(){
        return "%s %s %s".formatted(id,time,time);
    }
}
