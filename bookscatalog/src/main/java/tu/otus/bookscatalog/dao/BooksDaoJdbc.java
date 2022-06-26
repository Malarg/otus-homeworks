package tu.otus.bookscatalog.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Author;
import tu.otus.bookscatalog.domain.Book;
import tu.otus.bookscatalog.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class BooksDaoJdbc implements BooksDao, RowMapper<Book> {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final GenresDao genresDao;
    private final AuthorsDao authorsDao;

    public BooksDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, GenresDao genresDao, AuthorsDao authorsDao) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.genresDao = genresDao;
        this.authorsDao = authorsDao;
    }

    @Override
    public List<Book> getAll() {
        return namedParameterJdbcOperations.query("select id, title, authorId from books", this);
    }

    @Override
    public Book getById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return namedParameterJdbcOperations.queryForObject("select id, title, authorId from books where id = :id", params, this);
    }

    @Override
    public Book insert(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor().getId());

        namedParameterJdbcOperations.update("insert into books (title, authorId) values (:title, :authorId)", params, keyHolder);

        long insertedBookId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        insertGenres(insertedBookId, book.getGenres());

        return new Book(insertedBookId, book.getTitle(), book.getAuthor(), book.getGenres());
    }

    @Override
    public void update(Book book) {
        deleteBookGenres(book.getId());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("authorId", book.getAuthor().getId());
        params.addValue("title", book.getTitle());

        namedParameterJdbcOperations.update("update books set title = :title, authorId = :authorId where id = :id", params);
        insertGenres(book.getId(), book.getGenres());
    }

    @Override
    public void delete(Book book) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        deleteBookGenres(book.getId());
        namedParameterJdbcOperations.update("delete from books where id=:id", params);
    }

    @Override
    public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        long authorId = resultSet.getLong("authorId");
        Author author = authorsDao.getById(authorId);
        List<Genre> genres = genresDao.getByBookId(id);
        return new Book(id, title, author, genres);
    }

    private void deleteBookGenres(long bookId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", bookId);
        namedParameterJdbcOperations.update("delete from BookGenreJoint where bookId=:id", params);
    }

    private void insertGenres(long bookId, List<Genre> genres) {
        for (Genre genre : genres) {
            MapSqlParameterSource genreBookParams = new MapSqlParameterSource();
            genreBookParams.addValue("genreId", genre.getId());
            genreBookParams.addValue("bookId", bookId);
            namedParameterJdbcOperations.update("insert into BookGenreJoint (bookId, genreId) values (:bookId, :genreId)", genreBookParams);
        }
    }
}
