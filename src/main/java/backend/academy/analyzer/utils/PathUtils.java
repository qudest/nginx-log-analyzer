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

    private PathUtils() {
    }

}
