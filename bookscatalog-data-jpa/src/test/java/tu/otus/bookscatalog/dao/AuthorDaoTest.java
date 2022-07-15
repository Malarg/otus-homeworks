package tu.otus.bookscatalog.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import tu.otus.bookscatalog.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Класс AuthorsDaoJdbc")
@Import(AuthorDao.class)
class AuthorDaoTest {

    @Autowired
    private AuthorDao dao;

    @Test
    @DisplayName("должен возвращать имеющихся авторов")
    void shouldReturnExistingAuthors() {
        Author author = new Author("Василий", "Петечкин");

        Author insertedAuthor = dao.save(author);
        List<Author> authors = dao.findAll();

        assertThat(authors).contains(insertedAuthor);
    }

    @Test
    @DisplayName("должен возвращать автора по id")
    void shouldReturnAuthorById() {
        Author author = new Author("Василий", "Петечкин");

        Author insertedAuthor = dao.save(author);
        Author returnedAuthor = dao.getById(insertedAuthor.getId());

        assertThat(insertedAuthor).isEqualTo(returnedAuthor);
    }

    @Test
    @DisplayName("должен удалять автора")
    void shouldDeleteAuthor() {
        Author author = new Author("Василий", "Петечкин");

        Author insertedAuthor = dao.save(author);
        dao.delete(insertedAuthor);
        List<Author> authors = dao.findAll();

        assertThat(authors).doesNotContain(insertedAuthor);
    }
}