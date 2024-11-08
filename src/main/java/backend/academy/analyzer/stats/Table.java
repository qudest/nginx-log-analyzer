package backend.academy.analyzer.stats;

import java.util.List;

public record Table(

    String name,
    List<String> headers,
    List<Metric> values

) {
}
