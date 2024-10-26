package backend.academy.analyzer.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
