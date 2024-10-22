package com.roman.service.mapper;

import com.roman.dao.entity.PersonalInfo;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import com.roman.service.dto.worker.ShowFullInfoWorkerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PersonalInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstname", source = "dto.firstname")
    @Mapping(target = "lastname", source = "dto.lastname")
    @Mapping(target = "username", source = "dto.username")
    @Mapping(target = "chatId", source = "dto.chatId")
    PersonalInfo mapToInfo(RegistrationWorkerDto dto);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "state", source = "personalInfo.worker.state.state")
    @Mapping(target = "post", source = "personalInfo.post.title")
    @Mapping(target = "chatId", source = "chatId")
    ShowFullInfoWorkerDto mapToShow(PersonalInfo personalInfo);
}
