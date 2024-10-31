package backend.academy.analyzer.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileReader implements Reader {

    @Override
    public Stream<String> read(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.lines(filePath);
    }

}
