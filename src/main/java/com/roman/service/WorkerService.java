package com.roman.service;

import com.roman.dao.entity.Company;
import com.roman.dao.entity.Worker;
import com.roman.dao.repository.CompanyRepository;
import com.roman.dao.repository.worker.WorkerRepository;
import com.roman.service.dto.telegram.RegistrationWorkerDto;
import com.roman.service.dto.worker.ShowWorkerDto;
import com.roman.service.exception.UsernameDoesntExistException;
import com.roman.service.exception.WorkerAlreadyRegisteredException;
import com.roman.service.mapper.WorkerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final CompanyRepository companyRepository;
    private final WorkerMapper workerMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createWorker(RegistrationWorkerDto workerDto){
        Long workerId = Long.parseLong(workerDto.getWorkerId());
        Optional<Worker> mayBeWorker = workerRepository.findById(workerId);
        if(mayBeWorker.isPresent()){
            throw new WorkerAlreadyRegisteredException();
        }
        Worker worker = new Worker(workerId);
        workerRepository.save(worker);
    }

    public List<ShowWorkerDto> findAllWorkersWithAllInformation(Long directorId){
        Worker currentWorker = workerRepository.findWorkerWithCompanyById(directorId).get();
        return workerRepository.findAllWorkersByCompanyId(currentWorker.getCompany().getId())
                .stream()
                .map(workerMapper::mapToShow)
                .toList();
    }


    public List<Worker> findAllWorkersWithMeetings(String[] workersId){
        return workerRepository.findAllWorkerWithMeetingById(Arrays.stream(workersId).map(Long::parseLong).toList());
    }

    public Optional<Worker> findAllWorkerInformation(Long workerId){
        return workerRepository.findAllById(workerId);
    }


    @Transactional
    public void updateCompany(Long workerId, String companyName) {
        Worker worker = workerRepository.findById(workerId).get();
        Company company = companyRepository.findCompanyByName(companyName).get();
        worker.setCompany(company);

        workerRepository.save(worker);
    }


    public Worker findWorkerWithCompany(String directorUsername) {
        Optional<Worker> mayBeWorker = workerRepository.findWorkerWithCompanyAndPersonaInfoByUsername(directorUsername);
        if (mayBeWorker.isEmpty()) {
            throw new UsernameDoesntExistException(directorUsername);
        }
        return mayBeWorker.get();
    }


}
