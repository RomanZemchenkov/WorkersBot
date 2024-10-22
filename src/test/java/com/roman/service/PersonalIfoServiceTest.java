package com.roman.service;

import com.roman.service.telegram.DockerInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class PersonalIfoServiceTest extends DockerInitializer {

    private final PersonalInfoService personalInfoService;

    @Autowired
    public PersonalIfoServiceTest(PersonalInfoService personalInfoService) {
        this.personalInfoService = personalInfoService;
    }

    @Test
    @DisplayName("ff")
    void find(){
    }
}
