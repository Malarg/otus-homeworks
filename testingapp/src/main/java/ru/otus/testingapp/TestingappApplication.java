package ru.otus.testingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.testingapp.view.SessionView;

@SpringBootApplication
public class TestingappApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(TestingappApplication.class, args);
		var sessionView = context.getBean(SessionView.class);
		sessionView.startSession();
	}

}
