package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Genre;

import java.util.List;

public interface GenresDao {
    int count();

    List<Genre> getAll();

    Genre insert(Genre genre);

    Genre getById(long id);

    List<Genre> getByBookId(long bookId);

    void delete(Genre genre) throws DeleteGenreException;
}
