package com.roman.dao.repository;

import com.roman.dao.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query(value = "SELECT m FROM meeting m LEFT JOIN FETCH m.workers w WHERE w.id = :workerId")
    List<Meeting> findAllByWorkerId(@Param(value = "workerId") Long workerId);
}
