package tu.otus.bookscatalog.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import tu.otus.bookscatalog.dao.AuthorDao;
import tu.otus.bookscatalog.dao.BookDao;
import tu.otus.bookscatalog.dao.BookReviewDao;
import tu.otus.bookscatalog.dao.GenreDao;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.BookReview;
import tu.otus.bookscatalog.domain.Genre;
import tu.otus.bookscatalog.service.consoleconverters.BookConsoleConverter;
import tu.otus.bookscatalog.service.io.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
//Класс написан с допущениями, что пользователь всегда вводит валидные данные
public class BooksShellComponent {
    private final BookDao bookDao;
    private final GenreDao genreDao;
    private final AuthorDao authorDao;
    private final BookReviewDao bookReviewDao;
    private final IOService ioService;
    private final BookConsoleConverter bookConverter;
    private final GenresShellComponent genresShellComponent;
    private final AuthorsShellComponent authorsShellComponent;

    @ShellMethod(value = "Show all books", key = "books")
    public void listBooks() {
        showNumeratedBooksAndReturn();
    }

    @ShellMethod(value = "Add book", key = "add-book")
    public void addBook() {
        if (!verifyIfAuthorsAndGenresExists()) {
            return;
        }
        Author authorNumber = authorsShellComponent.getAuthorByUserSelectedNumber();
        List<Genre> genres = genresShellComponent.getGenresBySelectedUserNumbers();
        var title = ioService.readStringWithPrompt("Enter title: ");
        bookDao.save(new Book(title, authorNumber, genres));
        ioService.outputString("Book successfully added\n");
    }

    @ShellMethod(value = "Delete book", key = "delete-book")
    public void deleteBook() {
        var book = getBookByUserSelectedNumber();
        bookDao.delete(book);
        ioService.outputString("Book successfully deleted\n");
    }

    @ShellMethod(value = "Autofill book", key = "t")
    public void autofillBook() {
        var genre = genreDao.save(new Genre("Фантастика"));
        var author = authorDao.save(new Author("Джоан", "Роулинг"));
        var book = bookDao.save(new Book("Властелин Колец", author, List.of(genre)));
        bookReviewDao.save(new BookReview(book, "Сокол", "Интересно", "Очень приятный книга"));
    }

    private boolean verifyIfAuthorsAndGenresExists() {
        if (genreDao.count() == 0) {
            ioService.outputString("No genres found. Please add genres first.");
            return false;
        }
        if (authorDao.count() == 0) {
            ioService.outputString("No authors found. Please add authors first.");
            return false;
        }
        return true;
    }

    public Book getBookByUserSelectedNumber() {
        List<Book> books = showNumeratedBooksAndReturn();
        var bookNumber = ioService.readStringWithPrompt("Enter book number: ");
        return books.get(Integer.parseInt(bookNumber) - 1);
    }

    private List<Book> showNumeratedBooksAndReturn() {
        var books = bookDao.findAll();
        if (books.isEmpty()) {
            ioService.outputString("Books list is empty.");
            return books;
        }
        for (int i = 0; i < books.size(); i++) {
            ioService.outputString(i + 1 + ". " + bookConverter.convert(books.get(i)));
        }
        return books;
    }
}
