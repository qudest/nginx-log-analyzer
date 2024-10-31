package backend.academy.analyzer.utils;

import org.junit.jupiter.api.Test;
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

}
