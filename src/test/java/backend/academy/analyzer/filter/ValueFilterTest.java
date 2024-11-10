package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.FilterField;
import backend.academy.analyzer.params.Params;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueFilterTest {

    private final ValueFilter valueFilter = new ValueFilter();
    private final LogRecord logRecord = LogRecord.builder()
        .remoteAddr("93.180.71.3")
        .remoteUser("-")
        .timeLocal(LocalDateTime.of(2015, 5, 17, 8, 5, 37).toInstant(ZoneOffset.UTC))
        .request("GET /downloads/product_1 HTTP/1.1")
        .status(304)
        .bodyBytesSent(0)
        .httpReferer("https://google.com")
        .httpUserAgent("Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)")
        .build();

    @Test
    void filterByAddress() {
        Params params1 = Params.builder()
            .filterField(FilterField.ADDRESS)
            .filterValue("93\\..+").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.ADDRESS)
            .filterValue("92\\..+").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByUser() {
        Params params1 = Params.builder()
            .filterField(FilterField.USER)
            .filterValue("-").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.USER)
            .filterValue("admin").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByRequest() {
        Params params1 = Params.builder()
            .filterField(FilterField.REQUEST)
            .filterValue("GET.+").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.REQUEST)
            .filterValue("POST.+").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByStatus() {
        Params params1 = Params.builder()
            .filterField(FilterField.STATUS)
            .filterValue("304").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.STATUS)
            .filterValue("200").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByBytes() {
        Params params1 = Params.builder()
            .filterField(FilterField.BYTES)
            .filterValue("0").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.BYTES)
            .filterValue("100").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByReferer() {
        Params params1 = Params.builder()
            .filterField(FilterField.REFERER)
            .filterValue("https:\\/\\/.+\\.com").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.REFERER)
            .filterValue("-").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

    @Test
    void filterByUserAgent() {
        Params params1 = Params.builder()
            .filterField(FilterField.AGENT)
            .filterValue("Debian.+").build();
        assertTrue(valueFilter.doFilter(logRecord, params1));
        Params params2 = Params.builder()
            .filterField(FilterField.AGENT)
            .filterValue("Windows.+").build();
        assertFalse(valueFilter.doFilter(logRecord, params2));
    }

}
