package backend.academy.analyzer;

import backend.academy.analyzer.converter.ISO8601TimestampConverter;
import com.beust.jcommander.Parameter;
import java.time.Instant;
import java.util.List;
import lombok.Getter;

@Getter
public class CliParams {

    @Parameter(
        names = "--path",
        description = "Path to one or more NGINX log files as a local template or URL",
        variableArity = true,
        required = true
    )
    private List<String> path;

    @Parameter(
        names = "--from",
        description = "Analysis start time in ISO8601 format (optional)",
        converter = ISO8601TimestampConverter.class
    )
    private Instant from;

    @Parameter(
        names = "--to",
        description = "Analysis end time in ISO8601 format (optional)",
        converter = ISO8601TimestampConverter.class
    )
    private Instant to;

    @Parameter(
        names = "--format",
        description = "Output format (optional)"
    )
    private OutputFormat format;

    @Parameter(
        names = "--filter-field",
        description = "Filter field (optional)"
    )
    private String filterField;

    @Parameter(
        names = "--filter-value",
        description = "Filter value (optional)"
    )
    private String filterValue;

}
