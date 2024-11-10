package backend.academy.analyzer.reader;

import backend.academy.analyzer.utils.PathUtils;

public final class ReaderFactory {

    public static Reader getReader(String path) {
        if (PathUtils.isValidUrl(path)) {
            return new UrlReader();
        } else {
            return new FileReader();
        }
    }

    private ReaderFactory() {
    }

}
