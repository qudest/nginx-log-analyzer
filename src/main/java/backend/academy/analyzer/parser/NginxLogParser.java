package backend.academy.analyzer.parser;

import backend.academy.analyzer.log.LogRecord;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NginxLogParser implements LogParser {

    private static final String LOG_PATTERN =
            "^(?<remoteAddr>(\\d{1,3}\\.){3}\\d{1,3}|[0-9a-fA-F:]+) - (?<remoteUser>[^ ]+) \\[(?<timeLocal>[^]]+)] " +
                "\"(?<request>[^\"]+)\" (?<status>\\d{3}) (?<bodyBytesSent>\\d+) " +
                "\"(?<httpReferer>[^\"]*)\" \"(?<httpUserAgent>[^\"]*)\"$";

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    @Override
    public LogRecord parse(String line) {
        Pattern pattern = Pattern.compile(LOG_PATTERN);
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            return LogRecord.builder()
                .remoteAddr(matcher.group("remoteAddr"))
                .remoteUser(matcher.group("remoteUser"))
                .timeLocal(ZonedDateTime.parse(matcher.group("timeLocal"), FORMATTER).toInstant())
                .request(matcher.group("request"))
                .status(Integer.parseInt(matcher.group("status")))
                .bodyBytesSent(Long.parseLong(matcher.group("bodyBytesSent")))
                .httpReferer(matcher.group("httpReferer"))
                .httpUserAgent(matcher.group("httpUserAgent"))
                .build();
        } else {
            throw new IllegalArgumentException("Invalid log record: " + line);
        }

    }

}
