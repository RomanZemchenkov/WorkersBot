package com.roman.dao.redis;

import com.redis.testcontainers.RedisContainer;
import com.roman.service.telegram.DockerInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer extends DockerInitializer {

    private static final RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:7.4.0")).withExposedPorts(6379);

    @BeforeAll
    static void initRedis(){
        redisContainer.start();
    }

    @DynamicPropertySource
    static void setRedisProperties(DynamicPropertyRegistry registry){
        registry.add("spring.redis.port",() -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.redis.host",redisContainer::getHost);
    }


}
