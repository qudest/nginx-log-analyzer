package backend.academy.analyzer.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileReaderTest {

    private final FileReader fileReader = new FileReader();

    @Test
    void readValidFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, List.of("line1", "line2", "line3"));
        Stream<String> lines = fileReader.read(tempFile.toString());
        List<String> result = lines.toList();
        assertEquals(List.of("line1", "line2", "line3"), result);
        Files.delete(tempFile);
    }

    @Test
    void readEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("test", ".txt");
        Stream<String> lines = fileReader.read(tempFile.toString());
        List<String> result = lines.toList();
        assertTrue(result.isEmpty());
        Files.delete(tempFile);
    }

    @Test
    void readNonExistentFile() {
        assertThrows(IOException.class, () -> fileReader.read("nonexistentfile.txt"));
    }

}
