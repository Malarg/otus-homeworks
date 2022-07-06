package tu.otus.bookscatalog.dao;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Класс BooksDaoJdbc")
@Import({BookDaoJpa.class, GenreDaoJpa.class, AuthorDaoJpa.class})
class BookDaoJpaTest {

    @Autowired
    private BookDaoJpa booksDao;

    @Autowired
    private GenreDaoJpa genresDao;

    @Autowired
    private AuthorDao authorDao;

    Genre comedyGenre;
    Author petechkin;

    @BeforeEach
    void init() {
        comedyGenre = genresDao.insert(new Genre("Комедия"));
        petechkin = authorDao.save(new Author("Vasily", "Petechkin"));
    }

    @Test
    @DisplayName("возвращает все книги")
    void shouldReturnAllBooks() {
        Book awesomeBook = new Book("Awesome book", petechkin, List.of(comedyGenre));

        awesomeBook = booksDao.save(awesomeBook);
        List<Book> books = booksDao.getAll();

        assertThat(books).contains(awesomeBook);
    }

    @Test
    @DisplayName("удаляет книгу")
    void shouldDeleteBook() {
        Book awesomeBook = new Book("Awesome book", petechkin, List.of(comedyGenre));
        awesomeBook = booksDao.save(awesomeBook);
        List<Book> books = booksDao.getAll();

        assertThat(books).contains(awesomeBook);

        booksDao.delete(awesomeBook);
        books = booksDao.getAll();

        assertThat(books).doesNotContain(awesomeBook);
    }

    @Test
    @DisplayName("обновляет книгу")
    void shouldUpdateBook() {
        Book awesomeBook = new Book("Awesome book", petechkin, List.of(comedyGenre));
        awesomeBook = booksDao.save(awesomeBook);

        Author katechkin = authorDao.save(new Author("Ivan", "Katechkin"));
        Genre horrorGenre = genresDao.insert(new Genre("Horror"));

        awesomeBook.setTitle("Terrible book");
        awesomeBook.setAuthor(katechkin);
        var genres = new ArrayList<Genre>();
        genres.add(comedyGenre);
        genres.add(horrorGenre);
        awesomeBook.setGenres(genres);
        booksDao.save(awesomeBook);

        Book updatedBook = booksDao.getById(awesomeBook.getId());

        assertThat(updatedBook.getTitle()).isEqualTo("Terrible book");
        assertThat(updatedBook.getGenres()).isEqualTo(List.of(comedyGenre, horrorGenre));
        assertThat(updatedBook.getAuthor()).isEqualTo(katechkin);
    }
}