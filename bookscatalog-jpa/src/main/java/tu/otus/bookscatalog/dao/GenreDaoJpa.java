package tu.otus.bookscatalog.dao;

import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class GenreDaoJpa implements GenreDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public GenreDaoJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public int count() {
        return entityManager.createQuery("select count(*) from Genre", Long.class).getSingleResult().intValue();
    }

    @Override
    @Transactional
    public List<Genre> getAll() {
        return entityManager.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    @Transactional
    public Genre insert(Genre genre) {
        if (genre.getId() == 0) {
            entityManager.persist(genre);
            return genre;
        } else {
            return entityManager.merge(genre);
        }
    }

    @Override
    @Transactional
    public Genre getById(long id) {
        return entityManager.find(Genre.class, id);
    }

    @Override
    @Transactional
    public void delete(Genre genre) throws DeleteGenreException {
        try {
            entityManager.remove(entityManager.contains(genre) ? genre : entityManager.merge(genre));
        } catch (PersistenceException e) {
            throw new DeleteGenreException(e.getMessage());
        }
    }
}
