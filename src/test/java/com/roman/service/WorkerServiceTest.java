package com.roman.service;

import com.roman.service.dto.worker.ShowWorkerDto;
import com.roman.service.telegram.DockerInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class WorkerServiceTest extends DockerInitializer {

    private final WorkerService workerService;

    @Autowired
    public WorkerServiceTest(WorkerService workerService) {
        this.workerService = workerService;
    }

    @Test
    @DisplayName("Testing the find all workers by director id")
    void findAllByDirectorId(){
        List<ShowWorkerDto> allWorkers = workerService.findAllWorkersWithAllInformation(1L);
        System.out.println(allWorkers);
    }

    @Test
    @DisplayName("Testing the find all workers by workers id")
    void findAllByWorkersId(){
        workerService.findAllWorkersWithMeetings(new String[]{"1","2"});
    }
}
