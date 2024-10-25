package com.roman.dao.repository;

import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.Worker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "PersonalInfo.withAll")
    Optional<PersonalInfo> findPersonalInfoByUsername(String username);

    @Query(value = "SELECT p.chatId FROM personal_info p WHERE p.id IN :ids")
    List<Long> findPersonalChatId(@Param(value = "ids") String[] workersId);
}
