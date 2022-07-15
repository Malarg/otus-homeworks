package tu.otus.bookscatalog.dao;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Book;

import java.util.List;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {

    @NotNull
    @Override
    @EntityGraph(attributePaths = {"genres", "author"})
    List<Book> findAll();
}
