package tu.otus.bookscatalog.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class Book {
    private final Long id;
    private final String title;
    private final Author author;
    private final List<Genre> genres;
}
