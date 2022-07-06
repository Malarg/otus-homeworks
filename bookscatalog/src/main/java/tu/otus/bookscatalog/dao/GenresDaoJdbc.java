package tu.otus.bookscatalog.dao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tu.otus.bookscatalog.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class GenresDaoJdbc implements GenresDao {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    public GenresDaoJdbc(JdbcOperations jdbc, NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public int count() {
        var result = namedParameterJdbcOperations.getJdbcOperations().queryForObject("select count(*) from genres", Integer.class);
        return result == null ? 0 : result;
    }

    @Override
    public List<Genre> getAll() {
        return namedParameterJdbcOperations.query("select id, title from genres", new GenreMapper());
    }

    @Override
    public Genre insert(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", genre.getTitle());
        namedParameterJdbcOperations.update("insert into genres (title) values (:title)", params, keyHolder);
        return new Genre(Objects.requireNonNull(keyHolder.getKey()).longValue(), genre.getTitle());
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject(
                "select id, title from genres where id = :id", params, new GenreMapper()
        );
    }

    @Override
    public List<Genre> getByBookId(long bookId) {
        Map<String, Object> params = Collections.singletonMap("bookId", bookId);
        List<Long> genresIds = namedParameterJdbcOperations.queryForList("select genreId from BookGenreJoint where bookId=:bookId", params, Long.class);
        return genresIds.stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public void delete(Genre genre) throws DeleteGenreException {
        Map<String, Object> params = Collections.singletonMap("id", genre.getId());
        try {
            namedParameterJdbcOperations.update("delete from genres where id = :id", params);
        } catch (Exception e) {
            throw new DeleteGenreException(e.getMessage());
        }
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            return new Genre(id, title);
        }
    }
}
