package tu.otus.bookscatalog.service.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import tu.otus.bookscatalog.dao.DeleteGenreException;
import tu.otus.bookscatalog.dao.GenreDao;
import tu.otus.bookscatalog.domain.Genre;
import tu.otus.bookscatalog.service.consoleconverters.GenreConsoleConverter;
import tu.otus.bookscatalog.service.io.IOService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@ConditionalOnProperty(name = "condition.should-include-shell", havingValue = "true")
//Класс написан с допущениями, что пользователь всегда вводит валидные данные
public class GenresShellComponent {
    private final GenreConsoleConverter genreConverter;
    private final GenreDao genreDao;
    private final IOService ioService;

    @ShellMethod(value = "Show all genres", key = "genres")
    public void listGenres() {
        showNumeratedGenresAndReturn();
    }

    @ShellMethod(value = "Add genre", key = "add-genre")
    public void addGenre() {
        var title = ioService.readStringWithPrompt("Enter title: ");
        genreDao.insert(new Genre(title));
        ioService.outputString("Genre successfully added\n");
    }

    @ShellMethod(value = "Delete genre", key = "delete-genre")
    public void deleteGenre() {
        List<Genre> genres = showNumeratedGenresAndReturn();
        var genreNumber = ioService.readStringWithPrompt("Enter genre number: ");
        var genre = genres.get(Integer.parseInt(genreNumber) - 1);
        try {
            genreDao.delete(genre);
        } catch (DeleteGenreException e) {
            ioService.outputString("Genre is used by some books. Please delete all books that use this genre first.");
            return;
        }
        ioService.outputString("Genre successfully deleted\n");
    }

    public List<Genre> getGenresBySelectedUserNumbers() {
        List<Genre> genres = showNumeratedGenresAndReturn();
        var genreNumbers = ioService.readStringWithPrompt("Enter genre numbers, split with comma: ");
        var genreNumbersArray = Arrays.stream(genreNumbers.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return genreNumbersArray.stream().map(i -> i - 1).map(genres::get).collect(Collectors.toList());
    }

    private List<Genre> showNumeratedGenresAndReturn() {
        var genres = genreDao.getAll();
        if (genres.isEmpty()) {
            ioService.outputString("Genres list is empty");
            return genres;
        }
        for (int i = 0; i < genres.size(); i++) {
            ioService.outputString(i + 1 + ". " + genreConverter.convert(genres.get(i)));
        }
        return genres;
    }
}
