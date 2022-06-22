package ru.otus.testingapp.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс SessionCommandExecutor")
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
class SessionCommandExecutorTest {

    @Autowired(required=false)
    SessionCommandExecutor sessionCommandExecutor;

    @DisplayName("Не поднимается")
    @Test
    public void shouldntCreate() {
        assertThat(sessionCommandExecutor).isEqualTo(null);
    }
}