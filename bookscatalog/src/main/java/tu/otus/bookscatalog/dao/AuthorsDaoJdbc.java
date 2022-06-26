package tu.otus.bookscatalog.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class AuthorsDaoJdbc implements AuthorsDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public AuthorsDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        var result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from authors", Integer.class);
        return result == null ? 0 : result;
    }

    @Override
    public List<Author> getAll() {
        return namedParameterJdbcOperations.query("select id, firstname, lastname from authors", new AuthorsMapper());
    }

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select id, firstName, lastName from authors where id = :id", params, new AuthorsMapper()
        );
    }

    @Override
    public Author insert(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("firstName", author.getFirstName());
        params.addValue("lastName", author.getLastName());
        namedParameterJdbcOperations.update("insert into Authors (firstName, lastName) values (:firstName, :lastName)", params, keyHolder);
        return new Author(Objects.requireNonNull(keyHolder.getKey()).longValue(), author.getFirstName(), author.getLastName());
    }

    @Override
    public void delete(Author author) throws DeleteAuthorException {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", author.getId());
        try {
            namedParameterJdbcOperations.update("delete from authors where id = :id", params);
        } catch (DataIntegrityViolationException e) {
            throw new DeleteAuthorException(e.getMessage());
        }
    }

    private static class AuthorsMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("FirstName");
            String lastName = resultSet.getString("LastName");
            return new Author(id, firstName, lastName);
        }
    }
}
