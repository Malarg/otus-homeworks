package tu.otus.bookscatalog.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import tu.otus.bookscatalog.dao.BookDao;
import tu.otus.bookscatalog.dao.BookReviewDao;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.BookReview;
import tu.otus.bookscatalog.service.consoleconverters.BookReviewConsoleConverter;
import tu.otus.bookscatalog.service.io.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
public class BookReviewsShellComponent {
    private final BookReviewDao bookReviewDao;
    private final BookDao bookDao;
    private final BookReviewConsoleConverter bookReviewConsoleConverter;
    private final IOService ioService;
    private final BooksShellComponent booksShellComponent;

    @ShellMethod(value = "Show all book reviews for selected book", key = "reviews")
    public void listBookReviews() {
        var book = booksShellComponent.getBookByUserSelectedNumber();
        var bookReviews = bookReviewDao.findByBookId(book.getId());
        bookReviews.forEach(bookReview -> ioService.outputString(bookReviewConsoleConverter.convert(bookReview)));
    }

    @ShellMethod(value = "Add book review", key = "add-review")
    public void addBookReview() {
        var book = booksShellComponent.getBookByUserSelectedNumber();
        var userName = ioService.readStringWithPrompt("Enter your name: ");
        var title = ioService.readStringWithPrompt("Enter title: ");
        var text = ioService.readStringWithPrompt("Enter review text: ");
        bookReviewDao.save(new BookReview(book, userName, title, text));
        ioService.outputString("Book review successfully added\n");
    }

    @ShellMethod(value = "Delete book review", key = "delete-review")
    public void deleteBookReview() {
        var book = booksShellComponent.getBookByUserSelectedNumber();
        var bookReview = getBookReviewByUserSelectedNumber(book);
        bookReviewDao.delete(bookReview);
        ioService.outputString("Book review successfully deleted\n");
    }

    public BookReview getBookReviewByUserSelectedNumber(Book book) {
        var bookReviews = showNumeratedBookReviewsAndReturn(book);
        var bookReviewNumber = ioService.readStringWithPrompt("Enter book review number: ");
        return bookReviews.get(Integer.parseInt(bookReviewNumber) - 1);
    }

    public List<BookReview> showNumeratedBookReviewsAndReturn(Book book) {
        var reviews = bookReviewDao.findByBookId(book.getId());
        if (reviews.isEmpty()) {
            ioService.outputString("This book does not contains reviews\n");
            return reviews;
        }
        for (var i = 0; i < reviews.size(); i++) {
            ioService.outputString(String.format("%d. %s\n", i + 1, bookReviewConsoleConverter.convert(reviews.get(i))));
        }
        return reviews;
    }
}
