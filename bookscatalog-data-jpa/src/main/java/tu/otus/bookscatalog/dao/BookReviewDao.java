package tu.otus.bookscatalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.BookReview;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookReviewDao extends JpaRepository<BookReview, Long> {

    public List<BookReview> findByBookId(Long bookId);
}
