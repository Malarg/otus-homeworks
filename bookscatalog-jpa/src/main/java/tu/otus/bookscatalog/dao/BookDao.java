package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Book;

import java.util.List;

public interface BookDao {
    List<Book> getAll();
    Book getById(Long id);
    Book save(Book book);
    void delete(Book book);
}
