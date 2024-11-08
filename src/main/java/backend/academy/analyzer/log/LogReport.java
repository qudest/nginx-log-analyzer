package backend.academy.analyzer.log;

import backend.academy.analyzer.stats.Table;
import java.util.List;
import lombok.Builder;

@Builder
public record LogReport(

    List<Table> tables

) {
}
