package com.roman.dao.repository.worker;

import com.roman.dao.entity.Worker;

import java.util.List;

public interface CustomerWorkerRepository {

    List<Worker> findWorkersById(String[] workersId);
}
