package com.roman.service.mapper;

import com.roman.dao.entity.Meeting;
import com.roman.service.dto.meeting.ShowMeetingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeetingMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "time", source = "time")
    ShowMeetingDto mapToShow(Meeting meeting);
}
