package ru.otus.testingapp.service;

import ru.otus.testingapp.domain.Task;
import ru.otus.testingapp.domain.TestResult;

import java.util.List;

public interface TaskService {
    List<Task<?>> getAllTasks();

    TestResult getTestResult(List<Task<?>> tasks, List<String> answers);
}
