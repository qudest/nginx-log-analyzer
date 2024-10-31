package backend.academy.analyzer.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

public class UrlReader implements Reader {

    @Override
    public Stream<String> read(String path) throws IOException {
        URL url = URI.create(path).toURL();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            List<String> lines = reader.lines().toList();
            return lines.stream();
        }
    }

}
