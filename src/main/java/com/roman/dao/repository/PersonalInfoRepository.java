package com.roman.dao.repository;

import com.roman.dao.entity.PersonalInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"company"})
    Optional<PersonalInfo> findPersonalInfoWithCompanyByUsername(String username);
}
