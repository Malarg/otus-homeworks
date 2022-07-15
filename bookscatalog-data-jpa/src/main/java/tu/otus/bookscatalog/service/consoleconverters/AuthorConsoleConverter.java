package tu.otus.bookscatalog.service.consoleconverters;

import org.springframework.stereotype.Service;
import tu.otus.bookscatalog.domain.Author;

@Service
public class AuthorConsoleConverter implements ConsoleConverter<Author> {
    @Override
    public String convert(Author input) {
        return input.getFirstName() + " " + input.getLastName();
    }
}
