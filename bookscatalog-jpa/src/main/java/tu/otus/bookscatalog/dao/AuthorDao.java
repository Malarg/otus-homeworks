package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Author;

import java.util.List;

public interface AuthorDao {
    int count();
    List<Author> getAll();
    Author getById(long id);
    Author save(Author author);

    void delete(Author author) throws DeleteAuthorException;
}
