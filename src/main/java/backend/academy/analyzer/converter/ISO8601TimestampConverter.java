package backend.academy.analyzer.converter;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ISO8601TimestampConverter implements IStringConverter<Instant> {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        .appendOptional(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX"))
        .toFormatter();

    @Override
    public Instant convert(String s) {
        if (s == null) {
            log.error("Timestamp value is null");
            throw new ParameterException("Timestamp value is missing.");
        }
        try {
            if (s.contains("T")) {
                return ZonedDateTime.parse(s, FORMATTER).toInstant();
            } else {
                return LocalDate.parse(s).atStartOfDay(ZoneOffset.UTC).toInstant();
            }
        } catch (DateTimeParseException e) {
            log.error("Invalid timestamp value: {}", s);
            throw new ParameterException("Invalid timestamp value: " + s + ". Use ISO 8601 format.", e);
        }
    }

}
