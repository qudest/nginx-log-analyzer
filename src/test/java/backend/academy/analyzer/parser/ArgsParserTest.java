package backend.academy.analyzer.parser;

import backend.academy.analyzer.params.FilterField;
import backend.academy.analyzer.params.OutputFormat;
import backend.academy.analyzer.params.Params;
import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ArgsParserTest {

    private final ParamsParser paramsParser = new ArgsParser();

    @Test
    void parse() {
        Params params = Params.builder()
            .paths(List.of(
                "logs/**/2024-08-31.txt",
                "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs"
            ))
            .from(LocalDateTime.of(2024, 10, 25, 0, 0, 0).toInstant(ZoneOffset.UTC))
            .to(LocalDateTime.of(2024, 10, 28, 0, 0, 0).toInstant(ZoneOffset.UTC))
            .format(OutputFormat.ADOC)
            .filterField(FilterField.AGENT)
            .filterValue("Mozilla")
            .build();
        String[] args = {
            "--path",
            "logs/**/2024-08-31.txt",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--from",
            "2024-10-25",
            "--to",
            "2024-10-28",
            "--format",
            "adoc",
            "--filter-field",
            "agent",
            "--filter-value",
            "Mozilla"
        };
        Params parsed = paramsParser.parse(args);
        assertEquals(params, parsed);
    }

    @Test
    void parseWithoutPath() {
        String[] args = {
            "--from",
            "2024-10-25",
            "--to",
            "2024-10-28",
            "--format",
            "adoc",
            "--filter-field",
            "agent",
            "--filter-value",
            "Mozilla"
        };
        assertThrows(ParameterException.class, () -> paramsParser.parse(args));
    }

    @Test
    void parseWithWrongTimestamp() {
        String[] args = {
            "--path",
            "logs/**/2024-08-31.txt",
            "--from",
            "2024-10-28", // после to
            "--to",
            "2024-10-25",
        };
        assertThrows(ParameterException.class, () -> paramsParser.parse(args));
    }

    @Test
    void parseWithWrongEnumType() {
        String[] args1 = {
            "--path",
            "logs/**/2024-08-31.txt",
            "--format",
            "TEST"
        };
        assertThrows(ParameterException.class, () -> paramsParser.parse(args1));

        String[] args2 = {
            "--path",
            "logs/**/2024-08-31.txt",
            "--filter-field",
            "TEST"
        };
        assertThrows(ParameterException.class, () -> paramsParser.parse(args2));
    }

    @Test
    void parseWithoutFilterField() {
        String[] args = {
            "--path",
            "logs/**/2024-08-31.txt",
            "--filter-value",
            "AGENT"
        };
        assertThrows(ParameterException.class, () -> paramsParser.parse(args));
    }

}
