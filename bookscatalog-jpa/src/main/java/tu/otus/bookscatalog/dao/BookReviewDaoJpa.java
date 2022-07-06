package tu.otus.bookscatalog.dao;

import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.BookReview;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookReviewDaoJpa implements BookReviewDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public BookReviewDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BookReview> getByBookId(long bookId) {
        return entityManager.createQuery("select r from BookReview r join r.book b where b.id = :bookId", BookReview.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    @Override
    @Transactional
    public BookReview save(BookReview bookReview) {
        if (bookReview.getId() == 0L) {
            entityManager.persist(bookReview);
            return bookReview;
        } else {
            return entityManager.merge(bookReview);
        }
    }

    @Override
    @Transactional
    public void delete(BookReview bookReview) {
        entityManager.remove(entityManager.contains(bookReview) ? bookReview : entityManager.merge(bookReview));
    }
}
