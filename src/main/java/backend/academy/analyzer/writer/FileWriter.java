package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter implements Writer {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.dir"));

    @Override
    public void write(Path path, String content, OutputFormat outputFormat) throws IOException {
        String fileName = "report" + FileExtensionFactory.getFileExtension(outputFormat);
        Path pathToFile = path.resolve(fileName);
        long counter = 1;
        while (Files.exists(pathToFile)) {
            fileName = "report_" + counter + FileExtensionFactory.getFileExtension(outputFormat);
            pathToFile = path.resolve(fileName);
            counter++;
        }
        Files.writeString(pathToFile, content);
    }

    @Override
    public void write(String content, OutputFormat outputFormat) throws IOException {
        write(BASE_DIR, content, outputFormat);
    }

}
