package backend.academy.analyzer.reader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlReaderTest {

    private final UrlReader urlReader = new UrlReader();

    @Test
    void readValidUrl() throws IOException, URISyntaxException {
        URI uri = new URI("https://example.com");
        Stream<String> lines = urlReader.read(uri.toString());
        List<String> result = lines.toList();
        assertFalse(result.isEmpty());
    }

    @Test
    void readInvalidUrl() {
        assertThrows(IOException.class, () -> urlReader.read("https://invalid.url"));
    }

    @Test
    void readMalformedUrl() {
        assertThrows(Exception.class, () -> urlReader.read("malformed:url"));
    }

}
