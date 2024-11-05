package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;

public class TimestampFilter implements Filter {

    @Override
    public boolean doFilter(LogRecord logRecord, Params params) {
        if (params.from() != null) {
            return logRecord.timeLocal().isAfter(params.from());
        }
        if (params.to() != null) {
            return logRecord.timeLocal().isBefore(params.to());
        }
        return false;
    }

}
