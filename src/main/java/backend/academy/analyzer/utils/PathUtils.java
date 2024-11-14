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
            URI.create(path).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> match(PathSplit pathSplit) throws IOException {
        List<String> paths = new ArrayList<>();
        String location = pathSplit.location();
        String glob = pathSplit.glob();

        Path path = Path.of(location);
        if (!path.isAbsolute()) {
            path = Path.of(System.getProperty("user.dir"), location);
        }
        final Path finalPath = path;

        String pattern = "**" + FileSystems.getDefault().getSeparator();
        boolean isRecursive = glob.contains(pattern);
        if (isRecursive) {
            glob = glob.substring(pattern.length());
        }

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

        Files.walkFileTree(path, new SimpleFileVisitor<>() {

            @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
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

    public static PathSplit split(String path) {
        int lastIndexOfSeparator = path.lastIndexOf(FileSystems.getDefault().getSeparator());
        // Пример *.txt
        if (lastIndexOfSeparator == -1) {
            return new PathSplit("", path);
        }

        // Пример /path/to/*.txt
        String location = path.substring(0, lastIndexOfSeparator);
        String glob = path.substring(lastIndexOfSeparator + 1);

        // Пример /path/to/**/*.txt
        if (path.startsWith("**", lastIndexOfSeparator - 2)) {
            location = path.substring(0, lastIndexOfSeparator - 2);
            glob = path.substring(lastIndexOfSeparator - 2);
        }

        return new PathSplit(location, glob);
    }

    private PathUtils() {
    }

}
