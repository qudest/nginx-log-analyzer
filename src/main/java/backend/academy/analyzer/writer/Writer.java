package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;
import java.io.IOException;

public interface Writer {

    void write(String path, String content, OutputFormat outputFormat) throws IOException;

    void write(String content, OutputFormat outputFormat) throws IOException;

}
