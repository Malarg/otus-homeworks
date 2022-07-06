package tu.otus.bookscatalog.dao;

import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Book;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public BookDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Book> getAll() {
        EntityGraph<?> entityGraph = entityManager.getEntityGraph("authors-entity-graph");
        return entityManager.createQuery("select b from Book b join fetch b.genres", Book.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    @Transactional
    public Book getById(Long id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        } else {
            try {
                return entityManager.merge(book);
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    @Transactional
    public void delete(Book book) {
        entityManager.remove(entityManager.contains(book) ? book : entityManager.merge(book));
    }
}
