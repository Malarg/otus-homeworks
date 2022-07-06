package ru.otus.testingapp.view;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.testingapp.domain.Task;
import ru.otus.testingapp.domain.TestResult;
import ru.otus.testingapp.service.IOService;
import ru.otus.testingapp.service.IOServiceStreams;
import ru.otus.testingapp.service.TaskService;
import ru.otus.testingapp.view.converter.TaskConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Service
public class SessionView {
    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final MessageSource messageSource;
    private final IOService ioService;

    public SessionView(TaskService taskService, TaskConverter taskConverter, MessageSource messageSource, IOService ioService) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
        this.messageSource = messageSource;
        this.ioService = ioService;
    }

    public void startSession() {
        String userName = ioService.readStringWithPrompt(messageSource.getMessage("enterName", null, null, Locale.getDefault()));
        var tasks = taskService.getAllTasks();
        var answers = getAnswers(tasks);
        var testResult = taskService.getTestResult(tasks, answers);
        showTestResult(userName, testResult);
    }

    private void showTestResult(String userName, TestResult testResult) {
        var resultStringSource = testResult.isTestPassed() ? "testCompleted" : "testFailed";
        var resultString = messageSource.getMessage(resultStringSource, new Object[]{userName, testResult.getCorrectAnswers()}, Locale.getDefault());
        ioService.outputString(resultString);
    }

    private List<String> getAnswers(List<Task<?>> tasks) {
        var answers = new ArrayList<String>(tasks.size());
        for (var task : tasks) {
            answers.add(ioService.readStringWithPrompt(taskConverter.convertToString(task)));
        }
        return answers;
    }
}
