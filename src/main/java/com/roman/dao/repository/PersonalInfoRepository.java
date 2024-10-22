package com.roman.dao.repository;

import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.Worker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "PersonalInfo.withAll")
    Optional<PersonalInfo> findPersonalInfoByUsername(String username);
}
