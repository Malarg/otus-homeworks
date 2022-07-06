package ru.otus.testingapp.view;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
public class SessionCommandExecutor {
    private final SessionView sessionView;


    @ShellMethod(value = "Start session", key = {"s", "start"})
    public void startSession() {
        sessionView.startSession();
    }
}
