package com.roman.service;

import com.roman.dao.entity.PersonalInfo;
import com.roman.dao.entity.Post;
import com.roman.dao.entity.Worker;
import com.roman.dao.repository.PersonalInfoRepository;
import com.roman.dao.repository.worker.WorkerRepository;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import com.roman.service.dto.worker.ShowFullInfoWorkerDto;
import com.roman.service.exception.BirthdayFormatException;
import com.roman.service.mapper.PersonalInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalInfoService {

    private final PersonalInfoMapper mapper;
    private final PersonalInfoRepository personalInfoRepository;
    private final PostService postService;
    private final WorkerRepository workerRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createPersonalInfo(RegistrationWorkerDto dto) {
        Worker currentWorker = workerRepository.findById(Long.parseLong(dto.getWorkerId())).get();
        PersonalInfo personalInfo = mapper.mapToInfo(dto);
        personalInfo.setWorker(currentWorker);
        personalInfoRepository.save(personalInfo);
    }

    public ShowFullInfoWorkerDto findFullInfo(String username){
        PersonalInfo workerInfo = personalInfoRepository.findPersonalInfoByUsername(username).get();
        return mapper.mapToShow(workerInfo);
    }


    public List<Long> findWorkersChat(String[] workersId) {
        return personalInfoRepository.findPersonalChatId(workersId);
    }

    @Transactional
    public void updatePersonalInfoPost(Long workerId, String post) {
        PersonalInfo personalInfo = personalInfoRepository.findById(workerId).get();
        Post existPost = postService.findPostOrCreate(post);
        personalInfo.setPost(existPost);

        personalInfoRepository.save(personalInfo);
    }

    @Transactional
    public void updatePersonalInfoBirthday(Long workerId, String birthday) {
        PersonalInfo personalInfo = personalInfoRepository.findById(workerId).get();
        try {
            personalInfo.setBirthday(LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            throw new BirthdayFormatException();
        }

        personalInfoRepository.save(personalInfo);

    }

}
