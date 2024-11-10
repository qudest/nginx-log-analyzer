package backend.academy.analyzer.writer;

import backend.academy.analyzer.params.OutputFormat;

public final class FileExtensionFactory {

    private static final String ADOC = ".adoc";
    private static final String MD = ".md";

    public static String getFileExtension(OutputFormat outputFormat) {
        if (outputFormat == OutputFormat.ADOC) {
            return ADOC;
        }
        return MD;
    }

    private FileExtensionFactory() {
    }

}
