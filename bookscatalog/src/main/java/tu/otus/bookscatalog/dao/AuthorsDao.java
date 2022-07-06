package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Author;

import java.util.List;

public interface AuthorsDao {
    int count();
    List<Author> getAll();
    Author getById(long id);
    Author insert(Author author);

    void delete(Author author) throws DeleteAuthorException;
}
