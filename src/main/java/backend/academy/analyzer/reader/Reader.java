package backend.academy.analyzer.reader;

import java.io.IOException;
import java.util.stream.Stream;

public interface Reader {

    Stream<String> read(String path) throws IOException;

}
