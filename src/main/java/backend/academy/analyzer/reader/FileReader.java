package backend.academy.analyzer.reader;

import backend.academy.analyzer.utils.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileReader implements Reader {

    @Override
    public Stream<String> read(String path) throws IOException {
        Map.Entry<String, String> entry = PathUtils.split(path);
        List<String> files = PathUtils.match(entry.getValue(), entry.getKey());
        Stream<String> stream = Stream.empty();
        for (String file : files) {
            stream = Stream.concat(stream, Files.lines(Path.of(file)));
        }
        return stream;
    }

}
