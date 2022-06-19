package ru.otus.testingapp.view.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.testingapp.domain.Answer;
import ru.otus.testingapp.domain.Person;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс AnswerConverterTest")
class AnswerConverterTest {

    @Test
    @DisplayName("конвертирует String")
    void shouldConvertString() {
        var str = "Hello, World!";
        var answer = new Answer<>(str);
        var answerConverter = new AnswerConverter();
        var result = answerConverter.convertToString(answer);

        assertEquals(result, str);
    }

    @Test
    @DisplayName("конвертирует Date")
    void shouldConvertDate() {
        Date date = new GregorianCalendar(2022, Calendar.MAY, 4).getTime();
        var answer = new Answer<>(date);
        var answerConverter = new AnswerConverter();
        var result = answerConverter.convertToString(answer);

        assertEquals(result, "04.05.2022");
    }

    @Test
    @DisplayName("конвертирует int")
    void shouldConvertInt() {
        var intValue = 42;
        var answer = new Answer<>(intValue);
        var answerConverter = new AnswerConverter();
        var result = answerConverter.convertToString(answer);

        assertEquals(result, "42");
    }

    @Test
    @DisplayName("конвертирует Person")
    void shouldConvertPerson() {
        var person = new Person("Иван", "Петров");
        var answer = new Answer<>(person);
        var answerConverter = new AnswerConverter();
        var result = answerConverter.convertToString(answer);

        assertEquals(result, "Иван Петров");
    }
}