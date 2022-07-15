package tu.otus.bookscatalog.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import tu.otus.bookscatalog.dao.AuthorDao;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.service.consoleconverters.AuthorConsoleConverter;
import tu.otus.bookscatalog.service.io.IOServiceStreams;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
//Класс написан с допущениями, что пользователь всегда вводит валидные данные
public class AuthorsShellComponent {
    private final AuthorConsoleConverter authorConverter;
    private final AuthorDao authorDao;
    private final IOServiceStreams ioService;

    @ShellMethod(value = "Show all authors", key = "authors")
    public void listAuthors() {
        showNumeratedAuthorsAndReturn();
    }

    @ShellMethod(value = "Add author", key = "add-author")
    public void addAuthor() {
        var firstName = ioService.readStringWithPrompt("Enter first name: ");
        var lastName = ioService.readStringWithPrompt("Enter last name: ");
        authorDao.save(new Author(firstName, lastName));
        ioService.outputString("Author successfully added\n");
    }

    @ShellMethod(value = "Delete author", key = "delete-author")
    public void deleteAuthor() {
        var author = getAuthorByUserSelectedNumber();
        try {
            authorDao.delete(author);
        } catch (DataIntegrityViolationException e) {
            ioService.outputString("Author can't be deleted. It is used in some books.\n");
            return;
        }
        ioService.outputString("Author successfully deleted\n");
    }

    public Author getAuthorByUserSelectedNumber() {
        List<Author> authors = showNumeratedAuthorsAndReturn();
        var authorNumber = ioService.readStringWithPrompt("Enter author number: ");
        return authors.get(Integer.parseInt(authorNumber) - 1);
    }

    private List<Author> showNumeratedAuthorsAndReturn() {
        var authors = authorDao.findAll();
        if (authors.isEmpty()) {
            ioService.outputString("Authors list is empty");
        }
        for (int i = 0; i < authors.size(); i++) {
            ioService.outputString(i + 1 + ". " + authorConverter.convert(authors.get(i)));
        }
        return authors;
    }
}
