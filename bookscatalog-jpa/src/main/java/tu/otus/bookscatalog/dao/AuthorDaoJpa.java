package tu.otus.bookscatalog.dao;

import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Author;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public AuthorDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public int count() {
        return entityManager.createQuery("select count(*) from Author", Long.class).getSingleResult().intValue();
    }

    @Override
    @Transactional
    public List<Author> getAll() {
        return entityManager.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    @Transactional
    public Author getById(long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    @Transactional
    public Author save(Author author) {
        if (author.getId() == 0) {
            entityManager.persist(author);
            return author;
        } else {
            return entityManager.merge(author);
        }
    }

    @Override
    @Transactional
    public void delete(Author author) throws DeleteAuthorException {
        try {
            entityManager.createQuery("delete from Author a where a.id = :id")
                    .setParameter("id", author.getId())
                    .executeUpdate();
        } catch (PersistenceException e) {
            throw new DeleteAuthorException(e.getMessage());
        }
    }
}
