package backend.academy.analyzer.parser;

import backend.academy.analyzer.log.LogRecord;

public interface LogParser {

    LogRecord parse(String line);

}
