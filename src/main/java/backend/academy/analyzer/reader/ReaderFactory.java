package backend.academy.analyzer.reader;

import backend.academy.analyzer.utils.PathUtils;

public class ReaderFactory {

    public static Reader getReader(String path) {
        if (PathUtils.isValidUrl(path)) {
            return new UrlReader();
        } else {
            return new FileReader();
        }
    }

}
