package backend.academy.analyzer.filter;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.params.Params;
import java.util.List;
import lombok.Setter;

@Setter
public class FilterChain {

    private final List<Filter> filters;
    private Params params;

    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    public boolean filter(LogRecord logRecord) {
        if (params == null) {
            throw new IllegalStateException("Params must be set before filtering");
        }
        return filters.stream().allMatch(filter -> filter.doFilter(logRecord, params));
    }

}
