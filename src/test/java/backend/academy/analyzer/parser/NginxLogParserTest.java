package backend.academy.analyzer.parser;

import backend.academy.analyzer.log.LogRecord;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

class NginxLogParserTest {

    private final NginxLogParser nginxLogParser = new NginxLogParser();

    @Test
    void parseValidLogLine() {
        String logLine =
            "127.0.0.1 - - [17/May/2015:08:05:32 +0000] \"GET / HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"";
        LogRecord record = nginxLogParser.parse(logLine);
        assertNotNull(record);
        assertEquals("127.0.0.1", record.remoteAddr());
        assertEquals("-", record.remoteUser());
        assertEquals(ZonedDateTime.parse("17/May/2015:08:05:32 +0000",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)).toInstant(), record.timeLocal());
        assertEquals("GET / HTTP/1.1", record.request());
        assertEquals(200, record.status());
        assertEquals(1234L, record.bodyBytesSent());
        assertEquals("-", record.httpReferer());
        assertEquals("Mozilla/5.0", record.httpUserAgent());
    }

    @Test
    void parseLogLineWithIPv6Address() {
        String logLine =
            "2001:0db8:85a3:0000:0000:8a2e:0370:7334 - user [17/May/2015:08:05:32 +0000] \"GET / HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"";
        LogRecord record = nginxLogParser.parse(logLine);
        assertNotNull(record);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", record.remoteAddr());
        assertEquals("user", record.remoteUser());
        assertEquals(ZonedDateTime.parse("17/May/2015:08:05:32 +0000",
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)).toInstant(), record.timeLocal());
        assertEquals("GET / HTTP/1.1", record.request());
        assertEquals(200, record.status());
        assertEquals(1234L, record.bodyBytesSent());
        assertEquals("-", record.httpReferer());
        assertEquals("Mozilla/5.0", record.httpUserAgent());
    }

    @Test
    void parseLogLineWithInvalidFormat() {
        String logLine = "Invalid log line format";
        assertNull(nginxLogParser.parse(logLine));
    }

    @Test
    void parseLogLineWithMissingFields() {
        String logLine = "127.0.0.1 - user [17/May/2015:08:05:32 +0000] \"GET / HTTP/1.1\" 200";
        assertNull(nginxLogParser.parse(logLine));
    }

    @Test
    void parseLogLineWithInvalidDate() {
        String logLine = "127.0.0.1 - user [31/Feb/2015:08:05:32 +0000] \"GET / HTTP/1.1\" 200\"";
        assertNull(nginxLogParser.parse(logLine));
    }

}
