package ru.otus.testingapp.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import ru.otus.testingapp.domain.Answer;
import ru.otus.testingapp.domain.Person;
import ru.otus.testingapp.domain.Task;

import java.util.Date;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс DefaultTaskDaoTest")
@SpringBootTest
class DefaultTaskDaoTest {

    @Autowired
    ApplicationContext context;

    @DisplayName("генерирует список вопросов")
    @Test
    void shouldParseQuestionList() {
        Locale.setDefault(Locale.ROOT);
        var messageSource = context.getBean(MessageSource.class);
        var dao = new DefaultTaskDao(messageSource);
        var tasks = dao.getAll();

        assertThat(tasks)
                .hasSize(5)
                .hasOnlyElementsOfType(Task.class)
                .anyMatch(s -> s.getQuestion().equals("Who lives in a pineapple under the sea?") &&
                        s.getAnswers().stream().allMatch(p -> ((Answer<?>) p).getValue() instanceof Person)
                ).anyMatch(s -> s.getQuestion().equals("What is the best time of the year?") &&
                        s.getAnswers().stream().allMatch(p -> ((Answer<?>) p).getValue() instanceof String)
                ).anyMatch(s -> s.getQuestion().equals("What is the first word in Wine abbreviature?") &&
                        s.getAnswers().stream().allMatch(p -> ((Answer<?>) p).getValue() instanceof String)
                ).anyMatch(s -> s.getQuestion().equals("2^7?") &&
                        s.getAnswers().stream().allMatch(p -> ((Answer<?>) p).getValue() instanceof Integer)
                ).anyMatch(s -> s.getQuestion().equals("When was last Star Wars date? Type in dd.mm.yyyy format.") &&
                        s.getAnswers().stream().allMatch(p -> ((Answer<?>) p).getValue() instanceof Date)
                );
    }
}