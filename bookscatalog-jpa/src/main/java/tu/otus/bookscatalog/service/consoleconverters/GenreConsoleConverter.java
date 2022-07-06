package tu.otus.bookscatalog.service.consoleconverters;

import org.springframework.stereotype.Service;
import tu.otus.bookscatalog.domain.Genre;

@Service
public class GenreConsoleConverter implements ConsoleConverter<Genre> {
    @Override
    public String convert(Genre input) {
        return input.getTitle();
    }
}
