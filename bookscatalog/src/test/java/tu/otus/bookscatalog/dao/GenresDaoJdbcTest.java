package tu.otus.bookscatalog.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import tu.otus.bookscatalog.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс GenresDaoJdbc")
@JdbcTest
@Import(GenresDaoJdbc.class)
class GenresDaoJdbcTest {

    @Autowired
    private GenresDaoJdbc dao;

    @Test
    @DisplayName("должен возвращать имеющиеся жанры")
    void shouldReturnExistingGenres() {
        Genre genre = new Genre(null, "Комедия");

        Genre insertedGenre = dao.insert(genre);
        List<Genre> genres = dao.getAll();

        assertThat(genres).contains(insertedGenre);
    }

    @Test
    @DisplayName("должен возвращать жанр по id")
    void shouldReturnGenreById() {
        Genre genre = new Genre(null, "Комедия");

        Genre insertedGenre = dao.insert(genre);
        Genre returnedGenre = dao.getById(insertedGenre.getId());

        assertThat(insertedGenre).isEqualTo(returnedGenre);
    }

    @Test
    @DisplayName("должен удалять жанр")
    void shouldDeleteGenre() throws DeleteGenreException {
        Genre genre = new Genre(null, "Комедия");

        Genre insertedGenre = dao.insert(genre);
        dao.delete(insertedGenre);
        List<Genre> genres = dao.getAll();

        assertThat(genres).doesNotContain(insertedGenre);
    }
}