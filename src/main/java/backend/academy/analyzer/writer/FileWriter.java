package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriter implements Writer {

    @Override
    public void write(String path, String content, OutputFormat outputFormat) throws IOException {
        String fileName = "report" + FileExtensionFactory.getFileExtension(outputFormat);
        Path pathToFile = Paths.get(path, fileName);
        long counter = 1;
        while (Files.exists(pathToFile)) {
            fileName = "report_" + counter + FileExtensionFactory.getFileExtension(outputFormat);
            pathToFile = Paths.get(path, fileName);
            counter++;
        }
        Files.writeString(pathToFile, content);
    }

    @Override
    public void write(String content, OutputFormat outputFormat) throws IOException {
        write(System.getProperty("user.dir"), content, outputFormat);
    }

}
