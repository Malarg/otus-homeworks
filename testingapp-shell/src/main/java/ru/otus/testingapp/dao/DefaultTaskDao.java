package ru.otus.testingapp.dao;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.testingapp.domain.Answer;
import ru.otus.testingapp.domain.Person;
import ru.otus.testingapp.domain.Task;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class DefaultTaskDao implements TasksDao {

    private final MessageSource messageSource;

    public DefaultTaskDao(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public List<Task<?>> getAll() {
        var questionAndAnswers = getQuestionsAndAnswers();
        var result = new ArrayList<Task<?>>(questionAndAnswers.size());
        for (var question : questionAndAnswers.keySet()) {
            var answers = questionAndAnswers.get(question);
            result.add(firstNonNull(new Callable[]{
                    () -> tryParsePeople(question, answers),
                    () -> tryParseInt(question, answers),
                    () -> tryParseDate(question, answers),
                    () -> parseString(question, answers)
            }));
        }
        result.forEach(t -> Collections.shuffle(t.getAnswers()));
        return result;
    }

    private Map<String, List<String>> getQuestionsAndAnswers() {
        var result = new HashMap<String, List<String>>();
        var questionNumber = 1;
        while (isQuestionExists(questionNumber)) {
            var question = getQuestion(questionNumber);
            var answers = getAnswers(questionNumber);
            result.put(question, List.of(answers));
            questionNumber++;
        }
        return result;
    }

    private boolean isQuestionExists(int questionNumber) {
        return messageSource.getMessage("task" + questionNumber + ".question", null, null, Locale.getDefault()) != null;
    }

    private String getQuestion(int questionNumber) {
        return messageSource.getMessage("task" + questionNumber + ".question", null, null, Locale.getDefault());
    }

    private String[] getAnswers(int answersNumber) {
        return messageSource.getMessage("task" + answersNumber + ".answers", null, null, Locale.getDefault()).split("\\|");
    }

    private Task<Person> tryParsePeople(String question, List<String> answers) {
        var people = answers.stream().map(this::tryParsePerson).map(Answer::new).collect(Collectors.toList());
        if (people.stream().anyMatch(a -> a.getValue() == null)) {
            return null;
        }
        return new Task<>(question, people, people.get(0));
    }

    public Person tryParsePerson(String value) {
        if (value.startsWith("Person(") && value.endsWith(")") && value.contains(",")) {
            var firstAndLastName = value.replace("Person(", "").replace(")", "").split(",");
            return new Person(firstAndLastName[0], firstAndLastName[1]);
        }
        return null;
    }

    private Task<Integer> tryParseInt(String question, List<String> answers) {
        var integers = answers.stream().map(this::stringToInt).collect(Collectors.toList());
        if (integers.stream().anyMatch(Objects::isNull)) {
            return null;
        }
        var integersList = integers.stream().map(Answer::new).collect(Collectors.toList());
        return new Task<>(question, integersList, integersList.get(0));
    }

    private Integer stringToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Task<Date> tryParseDate(String question, List<String> answers) {
        var dates = answers.stream().map(this::stringToDate).map(Answer::new).collect(Collectors.toList());
        if (dates.stream().anyMatch(a -> a.getValue() == null)) {
            return null;
        }
        return new Task<>(question, dates, dates.get(0));
    }

    private Date stringToDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        try {
            return parser.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    private Task<String> parseString(String question, List<String> answers) {
        var answersList = answers.stream().map(Answer::new).collect(Collectors.toList());
        return new Task<>(question, answersList, answersList.get(0));
    }

    private <T> T firstNonNull(Callable<?>[] values) {
        for (var value : values) {
            try {
                var result = value.call();
                if (result != null) {
                    return (T) result;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
