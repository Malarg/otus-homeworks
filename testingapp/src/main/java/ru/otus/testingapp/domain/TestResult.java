package ru.otus.testingapp.domain;

public class TestResult {
    private final boolean isTestPassed;
    private final int correctAnswers;

    public boolean isTestPassed() {
        return isTestPassed;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public TestResult(boolean isTestPassed, int correctAnswers) {
        this.isTestPassed = isTestPassed;
        this.correctAnswers = correctAnswers;
    }
}
