package backend.academy.analyzer.converter;

import com.beust.jcommander.ParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ISO8601TimestampConverterTest {

    private final ISO8601TimestampConverter converter = new ISO8601TimestampConverter();

    @Test
    void convertLocalDate() {
        String ts = "2024-10-25";
        assertEquals(
            LocalDate.of(2024, 10, 25).atStartOfDay(ZoneOffset.UTC).toInstant(),
            converter.convert(ts)
        );
    }

    @Test
    void convertWithOffset() {
        String ts = "2024-10-25T14:42:39+03:00";
        assertEquals(
            Instant.parse("2024-10-25T11:42:39Z"),
            converter.convert(ts)
        );
    }

    @Test
    void convertUTC() {
        String ts = "2024-10-25T14:42:39Z";
        assertEquals(
            Instant.parse("2024-10-25T14:42:39Z"),
            converter.convert(ts)
        );
    }

    @Test
    void convertReduced() {
        String ts = "20241025T144239Z";
        assertEquals(
            Instant.parse("2024-10-25T14:42:39Z"),
            converter.convert(ts)
        );
    }

    @Test
    void convertNull() {
        assertThrows(ParameterException.class, () -> converter.convert(null));
    }

    @Test
    void convertInvalid() {
        String ts1 = "2024-10-25T14:42:39";
        String ts2 = "2024-10-25 14:42:39";
        String ts3 = "2024-10-25T14:42:39+19:00"; // Zone offset not in valid range: -18:00 to +18:00
        assertThrows(ParameterException.class, () -> converter.convert(ts1));
        assertThrows(ParameterException.class, () -> converter.convert(ts2));
        assertThrows(ParameterException.class, () -> converter.convert(ts3));
    }

    @Test
    void convertWithMillis() {
        String ts = "2024-10-25T14:42:39.123Z";
        assertEquals(
            Instant.parse("2024-10-25T14:42:39.123Z"),
            converter.convert(ts)
        );
    }

}
