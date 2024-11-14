package backend.academy.analyzer.parser;

import backend.academy.analyzer.log.LogRecord;
import java.util.Optional;

public interface LogParser {

    Optional<LogRecord> parse(String line);

}
