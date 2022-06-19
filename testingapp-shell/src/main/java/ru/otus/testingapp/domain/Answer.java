package ru.otus.testingapp.domain;

import java.util.Objects;

public class Answer<T> {
    private final T value;

    public T getValue() {
        return value;
    }

    public Answer(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer<?> answer = (Answer<?>) o;
        return value.equals(answer.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
