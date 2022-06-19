package ru.otus.testingapp.service;

import org.springframework.stereotype.Service;
import ru.otus.testingapp.domain.Task;
import ru.otus.testingapp.domain.TestResult;
import ru.otus.testingapp.service.settings.AppSettings;
import ru.otus.testingapp.dao.TasksDao;
import ru.otus.testingapp.view.converter.AnswerConverter;

import java.util.List;

@Service
public class DefaultTaskService implements TaskService {

    private final TasksDao dao;
    private final AnswerConverter answerConverter;
    private final AppSettings appSettings;

    DefaultTaskService(TasksDao dao, AnswerConverter answerConverter, AppSettings appSettings) {
        this.dao = dao;
        this.answerConverter = answerConverter;
        this.appSettings = appSettings;
    }

    @Override
    public List<Task<?>> getAllTasks() {
        return dao.getAll();
    }

    @Override
    public TestResult getTestResult(List<Task<?>> tasks, List<String> answers) {
        var requiredCorrectAnswers = appSettings.getRequiredCorrectAnswers();
        var correctAnswersCount = 0;
        for (var i = 0; i < tasks.size(); i++) {
            var isAnswersCorrect = checkAnswer(tasks.get(i), answers.get(i));
            if (isAnswersCorrect) {
                correctAnswersCount++;
            }
        }
        return new TestResult(correctAnswersCount >= requiredCorrectAnswers, correctAnswersCount);
    }

    private boolean checkAnswer(Task<?> task, String answer) {
        int intAnswer = -1;
        if (task.getAnswers().size() > 1) {
            try {
                intAnswer = Integer.parseInt(answer);
            } catch (Exception e) {
                return false;
            }
            return intAnswer == task.getAnswers().indexOf(task.getCorrectAnswer()) + 1;
        }
        return answer.equals(answerConverter.convertToString(task.getCorrectAnswer()));
    }
}
