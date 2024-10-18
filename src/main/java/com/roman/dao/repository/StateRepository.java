package com.roman.dao.repository;

import com.roman.dao.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State,Long> {

    Optional<State> findStateByWorkerId(Long workerId);
}
