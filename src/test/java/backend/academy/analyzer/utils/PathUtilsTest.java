package backend.academy.analyzer.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathUtilsTest {

    @Test
    void isValidUrlWithValidHttpUrl() {
        assertTrue(PathUtils.isValidUrl("http://example.com"));
    }

    @Test
    void isValidUrlWithValidHttpsUrl() {
        assertTrue(PathUtils.isValidUrl("https://example.com"));
    }

    @Test
    void isValidUrlWithInvalidUrl() {
        assertFalse(PathUtils.isValidUrl("htp://invalid-url"));
    }

    @Test
    void isValidUrlWithMalformedUrl() {
        assertFalse(PathUtils.isValidUrl("malformed:url"));
    }

    @Test
    void isValidUrlWithEmptyString() {
        assertFalse(PathUtils.isValidUrl(""));
    }

    @Test
    void isValidUrlWithNull() {
        assertFalse(PathUtils.isValidUrl(null));
    }

    @Test
    void splitPathWithoutLocation() {
        PathSplit pathSplit = PathUtils.split("*.txt");
        assertTrue(pathSplit.location().isEmpty());
        assertEquals("*.txt", pathSplit.glob());
        PathSplit pathSplit2 = PathUtils.split("/*.txt");
        assertTrue(pathSplit2.location().isEmpty());
        assertEquals("*.txt", pathSplit2.glob());
    }

    @Test
    void splitPathWithLocation() {
        PathSplit pathSplit = PathUtils.split("/path/to/*");
        assertEquals("/path/to", pathSplit.location());
        assertEquals("*", pathSplit.glob());
    }

    @Test
    void splitPathWithLocationAndGlob() {
        PathSplit pathSplit = PathUtils.split("/path/to/*.txt");
        assertEquals("/path/to", pathSplit.location());
        assertEquals("*.txt", pathSplit.glob());
    }

    @Test
    void splitPathWithDoubleAsteriskGlob() {
        PathSplit pathSplit = PathUtils.split("/path/to/**/file.txt");
        assertEquals("/path/to/", pathSplit.location());
        assertEquals("**/file.txt", pathSplit.glob());
    }

    @Test
    void splitPathWithDoubleAsteriskAtStart() {
        PathSplit pathSplit = PathUtils.split("/**/file.txt");
        assertEquals("/", pathSplit.location());
        assertEquals("**/file.txt", pathSplit.glob());
    }

    @Test
    void splitPathWithNoGlob() {
        PathSplit pathSplit = PathUtils.split("/path/to/directory/");
        assertEquals("/path/to/directory", pathSplit.location());
        assertEquals("", pathSplit.glob());
    }

    @Test
    void splitEmptyPath() {
        PathSplit pathSplit = PathUtils.split("");
        assertTrue(pathSplit.location().isEmpty());
        assertTrue(pathSplit.glob().isEmpty());
    }

}
