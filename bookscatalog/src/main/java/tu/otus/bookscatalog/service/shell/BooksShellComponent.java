package tu.otus.bookscatalog.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import tu.otus.bookscatalog.dao.AuthorsDao;
import tu.otus.bookscatalog.dao.BooksDao;
import tu.otus.bookscatalog.dao.GenresDao;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.Genre;
import tu.otus.bookscatalog.service.consoleconverters.BookConsoleConverter;
import tu.otus.bookscatalog.service.io.IOService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
//Класс написан с допущениями, что пользователь всегда вводит валидные данные
public class BooksShellComponent {
    private final BooksDao booksDao;
    private final GenresDao genresDao;
    private final AuthorsDao authorsDao;
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
        booksDao.insert(new Book(null, title, authorNumber, genres));
        ioService.outputString("Book successfully added\n");
    }

    @ShellMethod(value = "Delete book", key = "delete-book")
    public void deleteBook() {
        var book = getBookByUserSelectedNumber();
        booksDao.delete(book);
        ioService.outputString("Book successfully deleted\n");
    }

    private boolean verifyIfAuthorsAndGenresExists() {
        if (genresDao.count() == 0) {
            ioService.outputString("No genres found. Please add genres first.");
            return false;
        }
        if (authorsDao.count() == 0) {
            ioService.outputString("No authors found. Please add authors first.");
            return false;
        }
        return true;
    }

    private Book getBookByUserSelectedNumber() {
        List<Book> books = showNumeratedBooksAndReturn();
        var bookNumber = ioService.readStringWithPrompt("Enter book number: ");
        return books.get(Integer.parseInt(bookNumber) - 1);
    }

    private List<Book> showNumeratedBooksAndReturn() {
        var books = booksDao.getAll();
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
