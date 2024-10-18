package com.roman.service.mapper;

import com.roman.dao.entity.PersonalInfo;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PersonalInfoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstname", source = "dto.firstname")
    @Mapping(target = "lastname", source = "dto.lastname")
    @Mapping(target = "username", source = "dto.username")
    PersonalInfo mapToInfo(RegistrationWorkerDto dto);
}
