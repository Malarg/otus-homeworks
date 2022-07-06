package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Genre;

import java.util.List;

public interface GenreDao {
    int count();

    List<Genre> getAll();

    Genre insert(Genre genre);

    Genre getById(long id);

    void delete(Genre genre) throws DeleteGenreException;
}
