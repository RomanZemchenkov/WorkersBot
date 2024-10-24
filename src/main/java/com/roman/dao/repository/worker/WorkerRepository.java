package com.roman.dao.repository.worker;

import com.roman.dao.entity.Worker;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker,Long> {


    @Query(value = "SELECT w FROM worker AS w JOIN company AS c ON w.company.id = c.id WHERE w.personalInfo.username = :username")
    Optional<Worker> findWorkerWithCompanyAndPersonaInfoByUsername(String username);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"personalInfo"})
    Optional<Worker> findWorkerWithPersonalInfoById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"company"})
    Optional<Worker> findWorkerWithCompanyById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "Worker.withAll")
    List<Worker> findAllWorkersByCompanyId(Long companyId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT w FROM worker w LEFT JOIN FETCH w.meetings WHERE w.id IN :ids")
    List<Worker> findAllWorkerWithMeetingById(List<Long> ids);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "Worker.withAll")
    Optional<Worker> findAllById(Long id);
}
