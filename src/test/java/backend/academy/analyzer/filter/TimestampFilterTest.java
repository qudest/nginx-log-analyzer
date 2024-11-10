package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimestampFilterTest {

    private final TimestampFilter timestampFilter = new TimestampFilter();
    private final Instant from = LocalDate.of(2024, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant();
    private final Instant to = LocalDate.of(2024, 1, 31).atStartOfDay(ZoneOffset.UTC).toInstant();
    private final Instant between = LocalDate.of(2024, 1, 15).atStartOfDay(ZoneOffset.UTC).toInstant();

    @Test
    void filterBetween() {
        Params params = Params.builder()
            .from(from)
            .to(to)
            .build();
        LogRecord log = LogRecord.builder().timeLocal(between).build();

        assertTrue(timestampFilter.doFilter(log, params));
    }

    @Test
    void filterAfter() {
        Params params = Params.builder()
            .from(from)
            .build();
        LogRecord log = LogRecord.builder().timeLocal(between).build();

        assertTrue(timestampFilter.doFilter(log, params));
    }

    @Test
    void filterBefore() {
        Params params = Params.builder()
            .to(to)
            .build();
        LogRecord log = LogRecord.builder().timeLocal(between).build();

        assertTrue(timestampFilter.doFilter(log, params));
    }

    @Test
    void filterInvalid() {
        Params params = Params.builder()
            .from(to)
            .to(from)
            .build();
        Instant before = LocalDate.of(2023, 12, 31).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant after = LocalDate.of(2024, 2, 1).atStartOfDay(ZoneOffset.UTC).toInstant();
        LogRecord logBefore = LogRecord.builder().timeLocal(before).build();
        LogRecord logAfter = LogRecord.builder().timeLocal(after).build();

        assertFalse(timestampFilter.doFilter(logBefore, params));
        assertFalse(timestampFilter.doFilter(logAfter, params));

    }

}
