package tu.otus.bookscatalog.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Genre {
    private final Long id;
    private final String title;
}
