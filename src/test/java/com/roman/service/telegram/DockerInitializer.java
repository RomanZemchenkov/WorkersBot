package com.roman.service.telegram;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("test")
public class DockerInitializer {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.3");

    @BeforeAll
    static void init(){
        container.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",container::getJdbcUrl);
    }
}
