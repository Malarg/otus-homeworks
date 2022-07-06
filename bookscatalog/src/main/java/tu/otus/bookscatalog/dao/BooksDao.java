package tu.otus.bookscatalog.dao;

import tu.otus.bookscatalog.domain.Book;

import java.util.List;

public interface BooksDao {
    List<Book> getAll();
    Book getById(Long id);
    Book insert(Book book);
    void update(Book book);
    void delete(Book book);
}
