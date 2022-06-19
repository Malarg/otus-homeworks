package ru.otus.testingapp.view.converter;

import org.springframework.stereotype.Service;
import ru.otus.testingapp.domain.Task;

@Service
public class TaskConverter implements ConsoleConverter<Task<?>> {

    private final AnswerConverter answerConverter;

    public TaskConverter(AnswerConverter answerConverter) {
        this.answerConverter = answerConverter;
    }

    @Override
    public String convertToString(Task<?> value) {
        var result = value.getQuestion();
        if (value.getAnswers().size() == 1) {
            return result;
        }
        result += '\n';
        for (var i = 0; i < value.getAnswers().size(); i++) {
            result += i + 1 + ". " + answerConverter.convertToString(value.getAnswers().get(i));
            if (i <value.getAnswers().size() - 1) {
                result += '\n';
            }
        }
        return result;
    }
}
