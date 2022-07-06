package ru.otus.testingapp.service.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AppSettings implements RequiredCorrectAnswersProvider {

    private final int requiredCorrectAnswers;

    AppSettings(@Value("${requiredCorrectAnswers}") int requiredCorrectAnswers) {
        this.requiredCorrectAnswers = requiredCorrectAnswers;
    }

    @Override
    public int getRequiredCorrectAnswers() {
        return requiredCorrectAnswers;
    }
}
