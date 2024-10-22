package com.roman.service.telegram.help;

import com.roman.service.telegram.DockerInitializer;
import com.roman.service.telegram.start.StartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static com.roman.service.telegram.start.StartMessage.WORKER_DOESNT_REGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class StartServiceTest extends DockerInitializer {

    private final StartService startService;
    private static final Long[] EXIST_USER_ID = {1L,2L};

    @Autowired
    public StartServiceTest(StartService startService) {
        this.startService = startService;
    }

    @ParameterizedTest
    @DisplayName("Testing the check worker method")
    @MethodSource("argumentsForCheckWorkerMethod")
    void checkWorkerMethod(Long userId, String expectedMessage){
        String response = assertDoesNotThrow(() -> startService.checkWorker(userId));

        assertThat(response).isEqualTo(expectedMessage);
    }

    static Stream<Arguments> argumentsForCheckWorkerMethod(){
        return Stream.of(
                Arguments.of(EXIST_USER_ID[0],"Добро пожаловать, Roman.Встреча 1 Встреча 2 Встреча 3 "),
                Arguments.of(EXIST_USER_ID[1],"Добро пожаловать, Ivan.Задача 1 Задача 2 Задача 3 "),
                Arguments.of(100L,WORKER_DOESNT_REGISTERED)
        );
    }
}
