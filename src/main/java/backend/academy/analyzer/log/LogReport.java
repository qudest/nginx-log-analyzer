package backend.academy.analyzer.log;

import backend.academy.analyzer.stats.Metric;
import backend.academy.analyzer.stats.Table;
import java.util.List;
import lombok.Builder;

@Builder
public record LogReport(

    List<Metric> headers,
    List<Metric> metrics,
    List<Table> tables

) {
}
