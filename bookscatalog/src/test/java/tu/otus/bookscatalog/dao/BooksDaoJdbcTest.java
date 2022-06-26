package tu.otus.bookscatalog.dao;

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

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DisplayName("Класс BooksDaoJdbc")
@Import({BooksDaoJdbc.class, GenresDaoJdbc.class, AuthorsDaoJdbc.class})
class BooksDaoJdbcTest {

    @Autowired
    private BooksDaoJdbc booksDao;

    @Autowired
    private GenresDaoJdbc genresDao;

    @Autowired
    private AuthorsDao authorsDao;

    Genre comedyGenre;
    Author petechkin;

    @BeforeEach
    void init() {
        comedyGenre = genresDao.insert(new Genre(null, "Комедия"));
        petechkin = authorsDao.insert(new Author(null, "Vasily", "Petechkin"));
    }

    @Test
    @DisplayName("возвращает все книги")
    void shouldReturnAllBooks() {
        Book awesomeBook = new Book(null, "Awesome book", petechkin, List.of(comedyGenre));

        awesomeBook = booksDao.insert(awesomeBook);
        List<Book> books = booksDao.getAll();

        assertThat(books).contains(awesomeBook);
    }

    @Test
    @DisplayName("удаляет книгу")
    void shouldDeleteBook() {
        Book awesomeBook = new Book(null, "Awesome book", petechkin, List.of(comedyGenre));
        awesomeBook = booksDao.insert(awesomeBook);
        List<Book> books = booksDao.getAll();

        assertThat(books).contains(awesomeBook);

        booksDao.delete(awesomeBook);
        books = booksDao.getAll();

        assertThat(books).doesNotContain(awesomeBook);
    }

    @Test
    @DisplayName("обновляет книгу")
    void shouldUpdateBook() {
        Book awesomeBook = new Book(null, "Awesome book", petechkin, List.of(comedyGenre));
        awesomeBook = booksDao.insert(awesomeBook);

        Author katechkin = authorsDao.insert(new Author(null, "Ivan", "Katechkin"));
        Genre horrorGenre = genresDao.insert(new Genre(null, "Horror"));

        awesomeBook = new Book(awesomeBook.getId(), "Terrible book", katechkin, List.of(comedyGenre, horrorGenre));
        booksDao.update(awesomeBook);

        Book updatedBook = booksDao.getById(awesomeBook.getId());

        assertThat(updatedBook.getTitle()).isEqualTo("Terrible book");
        assertThat(updatedBook.getGenres()).isEqualTo(List.of(comedyGenre, horrorGenre));
        assertThat(updatedBook.getAuthor()).isEqualTo(katechkin);
    }
}