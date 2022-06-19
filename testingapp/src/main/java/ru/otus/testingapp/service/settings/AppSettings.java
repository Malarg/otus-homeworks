package ru.otus.testingapp.service.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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
