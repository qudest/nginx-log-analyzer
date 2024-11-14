package backend.academy.analyzer.utils;

import backend.academy.analyzer.reader.Reader;
import backend.academy.analyzer.reader.ReaderFactory;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ReaderUtils {

    public static Stream<String> readPaths(List<String> paths) {
        return paths.stream().flatMap(path -> {
            Reader reader = ReaderFactory.getReader(path);
            try {
                return reader.read(path);
            } catch (Exception e) {
                log.error("Error reading file: {}", path, e);
                return Stream.empty();
            }
        });
    }

    private ReaderUtils() {
    }

}
