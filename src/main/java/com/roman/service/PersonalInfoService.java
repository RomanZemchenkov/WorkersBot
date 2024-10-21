package com.roman.service;

import com.roman.dao.entity.Company;
import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.Post;
import com.roman.dao.entity.Worker;
import com.roman.dao.repository.CompanyRepository;
import com.roman.dao.repository.PersonalInfoRepository;
import com.roman.dao.repository.PostRepository;
import com.roman.dao.repository.WorkerRepository;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import com.roman.service.exception.BirthdayFormatException;
import com.roman.service.exception.UsernameDoesntExistException;
import com.roman.service.mapper.PersonalInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalInfoService {

    private final PersonalInfoMapper mapper;
    private final PersonalInfoRepository personalInfoRepository;
    private final PostRepository postRepository;
    private final CompanyRepository companyRepository;
    private final WorkerRepository workerRepository;
    private static final String BIRTHDAY_PATTERN = "yyyy.MM.dd";

    @Transactional(propagation = Propagation.REQUIRED)
    public void createPersonalInfo(RegistrationWorkerDto dto) {
        Worker currentWorker = workerRepository.findById(Long.parseLong(dto.getWorkerId())).get();
        PersonalInfo personalInfo = mapper.mapToInfo(dto);
        personalInfo.setWorker(currentWorker);
        personalInfoRepository.save(personalInfo);
    }

    @Transactional(readOnly = true)
    public PersonalInfo findPersonalInfoWithCompany(String workerUsername) {
        Optional<PersonalInfo> mayBeInfo = personalInfoRepository.findPersonalInfoWithCompanyByUsername(workerUsername);
        if (mayBeInfo.isEmpty()) {
            throw new UsernameDoesntExistException(workerUsername);
        }
        return mayBeInfo.get();
    }

    @Transactional(readOnly = true)
    public PersonalInfo findPersonalInfo(Long workerId) {
        Optional<PersonalInfo> mayBeInfo = personalInfoRepository.findById(workerId);
        if (mayBeInfo.isEmpty()) {
            throw new RuntimeException("Потом сделаю");
        }
        return mayBeInfo.get();
    }

    public void updatePersonalInfoPost(Long workerId, String post) {
        PersonalInfo personalInfo = personalInfoRepository.findById(workerId).get();
        Post existPost = postRepository.findPostByTitle(post).orElseGet(() -> postRepository.save(new Post(post)));
        personalInfo.setPost(existPost);

        personalInfoRepository.save(personalInfo);
    }

    public void updatePersonalInfoCompany(Long workerId, String companyName) {
        PersonalInfo personalInfo = personalInfoRepository.findById(workerId).get();
        Company company = companyRepository.findCompanyByName(companyName).get();
        personalInfo.setCompany(company);

        personalInfoRepository.save(personalInfo);
    }

    public void updatePersonalInfoBirthday(Long workerId, String birthday) {
        PersonalInfo personalInfo = personalInfoRepository.findById(workerId).get();
        try {
            personalInfo.setBirthday(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e){
            throw new BirthdayFormatException();
        }

        personalInfoRepository.save(personalInfo);

    }
}
