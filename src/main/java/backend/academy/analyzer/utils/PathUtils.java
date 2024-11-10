package backend.academy.analyzer.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class PathUtils {

    public static boolean isValidUrl(String path) {
        try {
            URL url = URI.create(path).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> match(String glob, String location) throws IOException {
        List<String> paths = new ArrayList<>();

        Path path = Path.of(location);
        if (!path.isAbsolute()) {
            path = Path.of(System.getProperty("user.dir"), location);
        }
        final Path finalPath = path;

        String pattern = "**" + FileSystems.getDefault().getSeparator();
        boolean isRecursive = glob.startsWith(pattern);
        if (isRecursive) {
            glob = glob.substring(pattern.length());
        }

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

        Files.walkFileTree(path, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (!isRecursive && !finalPath.equals(dir)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (pathMatcher.matches(file.getFileName())) {
                    paths.add(file.toString());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

        });

        return paths;
    }

    public static Map.Entry<String, String> split(String path) {
        int index = path.lastIndexOf(FileSystems.getDefault().getSeparator());
        if (index == -1) {
            return Map.entry("", path);
        }

        if (path.startsWith("**", index-2)) {
            return Map.entry(path.substring(0, index-2), path.substring(index-2));
        } else {
            return Map.entry(path.substring(0, index), path.substring(index+1));
        }
    }

    private PathUtils() {
    }

}
