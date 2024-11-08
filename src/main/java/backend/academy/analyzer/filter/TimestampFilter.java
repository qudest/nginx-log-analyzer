package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;

public class TimestampFilter implements Filter {

    @Override
    public boolean doFilter(LogRecord logRecord, Params params) {
        if (params.from() != null && params.to() != null) {
            return isAfter(logRecord, params) && isBefore(logRecord, params);
        }
        if (params.from() != null) {
            return isAfter(logRecord, params);
        }
        if (params.to() != null) {
            return isBefore(logRecord, params);
        }
        return true;
    }

    private static boolean isAfter(LogRecord logRecord, Params params) {
        return logRecord.timeLocal().isAfter(params.from());
    }

    private static boolean isBefore(LogRecord logRecord, Params params) {
        return logRecord.timeLocal().isBefore(params.to());
    }

}
