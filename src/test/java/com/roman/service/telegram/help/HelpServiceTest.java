package com.roman.service.telegram.help;

import com.roman.service.telegram.DockerInitializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
public class HelpServiceTest extends DockerInitializer {

    private final HelpService helpService;
    private static final Long[] EXIST_USER_ID = {1L,2L};
    private static final String FIRSTNAME = "Name";

    @Autowired
    public HelpServiceTest(HelpService helpService) {
        this.helpService = helpService;
    }

    @ParameterizedTest
    @DisplayName("Testing the check user post method and sended messages")
    @MethodSource("argumentsForCheckUserPostMethod")
    void checkUserPostMethod(User currentUser, String expectedResponseMessage){
        String responseMessage = assertDoesNotThrow(() -> helpService.checkUserPost(currentUser));
        System.out.println(responseMessage);

        assertThat(responseMessage).isEqualTo(expectedResponseMessage);
    }

    static Stream<Arguments> argumentsForCheckUserPostMethod(){
        return Stream.of(
                Arguments.of(new User(EXIST_USER_ID[0],FIRSTNAME,false), HelpMessage.DIRECTOR_POST_HELP_MESSAGE),
                Arguments.of(new User(EXIST_USER_ID[1],FIRSTNAME,false), HelpMessage.WORKER_POST_HELP_MESSAGE),
                Arguments.of(new User(3L,FIRSTNAME,false), HelpMessage.NO_POST_HELP_MESSAGE)
        );
    }
}
