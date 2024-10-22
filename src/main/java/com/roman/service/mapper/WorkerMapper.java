package com.roman.service.mapper;

import com.roman.dao.entity.Worker;
import com.roman.service.dto.worker.ShowWorkerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "worker.personalInfo.username")
    @Mapping(target = "firstname", source = "worker.personalInfo.firstname")
    @Mapping(target = "lastname", source = "worker.personalInfo.lastname")
    @Mapping(target = "post", source = "worker.personalInfo.post.title")
    ShowWorkerDto mapToShow(Worker worker);
}
