package backend.academy.analyzer.reader;

import backend.academy.analyzer.utils.PathSplit;
import backend.academy.analyzer.utils.PathUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class FileReader implements Reader {

    @Override
    public Stream<String> read(String path) throws IOException {
        PathSplit split = PathUtils.split(path);
        List<String> paths = PathUtils.match(split);
        Stream<String> stream = Stream.empty();
        for (String filePath : paths) {
            stream = Stream.concat(stream, Files.lines(Path.of(filePath)));
        }
        return stream;
    }

}
