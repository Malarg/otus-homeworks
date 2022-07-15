package tu.otus.bookscatalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Genre;

@Repository
public interface GenreDao extends JpaRepository<Genre, Long> {

}
