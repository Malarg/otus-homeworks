package tu.otus.bookscatalog.service.consoleconverters;

public interface ConsoleConverter<T> {
    String convert(T input);
}
