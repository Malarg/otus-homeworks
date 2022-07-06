package tu.otus.bookscatalog.service.consoleconverters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tu.otus.bookscatalog.domain.Book;

@Service
@RequiredArgsConstructor
public class BookConsoleConverter implements ConsoleConverter<Book> {
    private final AuthorConsoleConverter authorConverter;
    private final GenreConsoleConverter genreConverter;
    @Override
    public String convert(Book input) {
        String genresString = input.getGenres().stream().map(genreConverter::convert).reduce("", (a, b) -> a + ", " + b).substring(2);
        return input.getTitle() + ", " + authorConverter.convert(input.getAuthor()) + ". Genres: " + genresString + ".\n";
    }
}