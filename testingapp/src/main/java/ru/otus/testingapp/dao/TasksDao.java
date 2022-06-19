package ru.otus.testingapp.dao;

import ru.otus.testingapp.domain.Task;

import java.util.List;

public interface TasksDao {
    List<Task<?>> getAll();
}
