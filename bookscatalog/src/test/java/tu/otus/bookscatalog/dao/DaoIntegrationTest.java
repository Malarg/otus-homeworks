package tu.otus.bookscatalog.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@RequiredArgsConstructor
@Import({BooksDaoJdbc.class, GenresDaoJdbc.class, AuthorsDaoJdbc.class})
public class DaoIntegrationTest {
    @Autowired
    private BooksDao booksDao;

    @Autowired
    private GenresDao genresDao;

    @Autowired
    private AuthorsDao authorsDao;

    private Genre comedyGenre;
    private Author petechkin;
    private Book awesomeBook;

    @BeforeEach
    void init() {
        comedyGenre = genresDao.insert(new Genre(null, "Комедия"));
        petechkin = authorsDao.insert(new Author(null, "Vasily", "Petechkin"));
        awesomeBook = booksDao.insert(new Book(null, "Awesome book", petechkin, List.of(comedyGenre)));
    }

    @Test
    @DisplayName("AuthorsDao должен выкидывать ошибку при удалении зависимого автора")
    void shouldThrowExceptionWhenDeleteAuthorWithBooks() {

        assertThatThrownBy(() -> authorsDao.delete(petechkin))
                .isInstanceOf(DeleteAuthorException.class);
    }

    @Test
    @DisplayName("GenresDao должен выкидывать ошибку при удалении зависимого жанра")
    void shouldThrowExceptionWhenDeleteGenreWithBooks() {

        assertThatThrownBy(() -> genresDao.delete(comedyGenre))
                .isInstanceOf(DeleteGenreException.class);
    }
}
