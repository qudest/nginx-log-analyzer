package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;
import java.util.regex.Pattern;

public class ValueFilter implements Filter {

    @SuppressWarnings("ReturnCount")
    @Override
    public boolean doFilter(LogRecord logRecord, Params params) {
        String filterValue = params.filterValue();
        if (filterValue == null) {
            return true;
        }
        Pattern pattern = Pattern.compile(filterValue);
        switch (params.filterField()) {
            case ADDRESS -> {
                return pattern.matcher(logRecord.remoteAddr()).find();
            }
            case USER -> {
                return pattern.matcher(logRecord.remoteUser()).find();
            }
            case REQUEST -> {
                return pattern.matcher(logRecord.request()).find();
            }
            case STATUS -> {
                return pattern.matcher(String.valueOf(logRecord.status())).find();
            }
            case BYTES -> {
                return pattern.matcher(String.valueOf(logRecord.bodyBytesSent())).find();
            }
            case REFERER -> {
                return pattern.matcher(logRecord.httpReferer()).find();
            }
            case AGENT -> {
                return pattern.matcher(logRecord.httpUserAgent()).find();
            }
            default -> {
                return false;
            }
        }
    }

}
