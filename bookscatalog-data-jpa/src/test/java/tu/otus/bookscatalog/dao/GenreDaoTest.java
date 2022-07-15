package tu.otus.bookscatalog.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import tu.otus.bookscatalog.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс GenresDaoJdbc")
@DataJpaTest
@Import(GenreDao.class)
class GenreDaoTest {

    @Autowired
    private GenreDao dao;

    @Test
    @DisplayName("должен возвращать имеющиеся жанры")
    void shouldReturnExistingGenres() {
        Genre genre = new Genre("Комедия");

        Genre insertedGenre = dao.save(genre);
        List<Genre> genres = dao.findAll();

        assertThat(genres).contains(insertedGenre);
    }

    @Test
    @DisplayName("должен возвращать жанр по id")
    void shouldReturnGenreById() {
        Genre genre = new Genre("Комедия");

        Genre insertedGenre = dao.save(genre);
        Genre returnedGenre = dao.getById(insertedGenre.getId());

        assertThat(insertedGenre).isEqualTo(returnedGenre);
    }

    @Test
    @DisplayName("должен удалять жанр")
    void shouldDeleteGenre() {
        Genre genre = new Genre("Комедия");

        Genre insertedGenre = dao.save(genre);
        dao.delete(insertedGenre);
        List<Genre> genres = dao.findAll();

        assertThat(genres).doesNotContain(insertedGenre);
    }
}