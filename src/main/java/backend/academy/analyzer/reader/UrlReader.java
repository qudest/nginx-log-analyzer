package backend.academy.analyzer.reader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class UrlReader implements Reader {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public Stream<String> read(String path) throws IOException {
        URI uri = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().lines();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while reading from URL: " + path, e);
        }
    }

}
