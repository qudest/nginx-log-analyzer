package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;

public class FileExtensionFactory {

    public static String getFileExtension(OutputFormat outputFormat) {
        if (outputFormat == OutputFormat.ADOC) {
            return ".adoc";
        }
        return ".md";
    }

}
