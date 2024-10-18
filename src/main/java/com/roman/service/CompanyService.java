package com.roman.service;

import com.roman.dao.entity.Company;
import com.roman.dao.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company findCompanyOrCreate(String companyName){
        Optional<Company> mayBeCompany = companyRepository.findCompanyByName(companyName);
        return mayBeCompany.orElseGet(() -> createNewCompany(companyName));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Company createNewCompany(String companyName){
        return companyRepository.save(new Company(companyName));
    }
}
