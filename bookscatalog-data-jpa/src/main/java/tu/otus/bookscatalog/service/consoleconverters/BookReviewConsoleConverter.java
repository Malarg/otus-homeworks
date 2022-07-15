package tu.otus.bookscatalog.service.consoleconverters;

import org.springframework.stereotype.Service;
import tu.otus.bookscatalog.domain.BookReview;

@Service
public class BookReviewConsoleConverter implements ConsoleConverter<BookReview> {
    @Override
    public String convert(BookReview input) {
        return input.getUserName() + ". " + input.getTitle() + ": " + input.getText() + "\n";
    }
}
