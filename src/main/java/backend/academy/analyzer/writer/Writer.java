package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;
import java.io.IOException;
import java.nio.file.Path;

public interface Writer {

    void write(Path path, String content, OutputFormat outputFormat) throws IOException;

    void write(String content, OutputFormat outputFormat) throws IOException;

}
