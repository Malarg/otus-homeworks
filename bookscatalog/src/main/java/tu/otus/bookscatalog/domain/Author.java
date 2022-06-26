package tu.otus.bookscatalog.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Author {
    private final Long id;
    private final String firstName;
    private final String lastName;
}
