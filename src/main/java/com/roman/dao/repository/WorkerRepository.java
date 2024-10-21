package com.roman.dao.repository;

import com.roman.dao.entity.Worker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker,Long> {

//    @Query(value = "SELECT w FROM Worker AS w JOIN PersonalInfo AS pi ON pi.worker = w WHERE pi.username = :username")
//    Optional<Worker> findWorkerByUsername(String username);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"personalInfo"})
    Optional<Worker> findWorkerWithPersonalInfoById(Long id);
}
