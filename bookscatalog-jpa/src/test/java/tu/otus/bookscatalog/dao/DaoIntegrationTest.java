package tu.otus.bookscatalog.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.Genre;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@RequiredArgsConstructor
@Import({BookDaoJpa.class, GenreDaoJpa.class, AuthorDaoJpa.class})
public class DaoIntegrationTest {
    @Autowired
    private BookDao bookDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private AuthorDao authorDao;

    private Genre comedyGenre;
    private Author petechkin;
    private Book awesomeBook;

    @BeforeEach
    void init() {
        comedyGenre = genreDao.insert(new Genre("Комедия"));
        petechkin = authorDao.save(new Author("Vasily", "Petechkin"));
        var genres = new ArrayList<Genre>();
        genres.add(comedyGenre);
        awesomeBook = bookDao.save(new Book("Awesome book", petechkin, genres));
    }

    @Test
    @DisplayName("AuthorsDao должен выкидывать ошибку при удалении зависимого автора")
    void shouldThrowExceptionWhenDeleteAuthorWithBooks() {

        assertThatThrownBy(() -> authorDao.delete(petechkin))
                .isInstanceOf(DeleteAuthorException.class);
    }
}
