package com.roman.dao.repository;

import com.roman.dao.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Optional<Company> findCompanyByName(String name);

}
