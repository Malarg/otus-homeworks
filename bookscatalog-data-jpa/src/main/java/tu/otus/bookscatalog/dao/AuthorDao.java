package tu.otus.bookscatalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Author;

@Repository
public interface AuthorDao extends JpaRepository<Author, Long> {

}
