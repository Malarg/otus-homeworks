package tu.otus.bookscatalog.dao;


import tu.otus.bookscatalog.domain.BookReview;

import java.util.List;

public interface BookReviewDao {
    List<BookReview> getByBookId(long bookId);
    BookReview save(BookReview bookReview);
    void delete(BookReview bookReview);
}
