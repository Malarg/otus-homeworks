package ru.otus.testingapp.view.converter;

import org.springframework.stereotype.Service;
import ru.otus.testingapp.domain.Answer;
import ru.otus.testingapp.domain.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class AnswerConverter implements ConsoleConverter<Answer<?>> {

    @Override
    public String convertToString(Answer<?> value) {
        if (value.getValue() instanceof Person) {
            return ((Person) value.getValue()).getFirstName() + " " + ((Person) value.getValue()).getLastName();
        }
        if (value.getValue() instanceof Date) {
            SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            return parser.format((Date)value.getValue());
        }
        return value.getValue().toString();
    }
}