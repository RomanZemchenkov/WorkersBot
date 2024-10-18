package com.roman.dao.repository;

import com.roman.dao.entity.PersonalToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalTokenRepository extends JpaRepository<PersonalToken,Long> {
}
