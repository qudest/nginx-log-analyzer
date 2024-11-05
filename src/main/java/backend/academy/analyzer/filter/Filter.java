package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;

public interface Filter {

    boolean doFilter(LogRecord logRecord, Params params);

}
