package backend.academy.analyzer.utils;

import java.net.URI;
import java.net.URL;

public class PathUtils {

    public static boolean isValidUrl(String path) {
        try {
            URL url = URI.create(path).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
