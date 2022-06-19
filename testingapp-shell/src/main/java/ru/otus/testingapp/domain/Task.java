package ru.otus.testingapp.domain;

import java.util.Collections;
import java.util.List;

public class Task<T> {
    String question;
    List<Answer<T>> answers;
    Answer<T> correctAnswer;

    public String getQuestion() {
        return question;
    }

    public List<Answer<T>> getAnswers() {
        return answers;
    }

    public Answer<T> getCorrectAnswer() {
        return correctAnswer;
    }

    public Task(String question, List<Answer<T>> answers, Answer<T> correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }
}
