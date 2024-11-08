package backend.academy.analyzer.stats;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.utils.PercentileCalculator;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorsFactory {

    public static List<Collector<LogRecord, Object, Metric>> getMetricCollectors() {
        return List.of(Collectors.collectingAndThen(Collectors.counting(),
                count -> new Metric("Количество запросов", String.valueOf(count))),

            Collectors.collectingAndThen(Collectors.averagingLong(LogRecord::bodyBytesSent),
                avg -> new Metric("Средний размер ответа", String.valueOf(avg))),

            Collectors.collectingAndThen(Collectors.mapping(LogRecord::bodyBytesSent, Collectors.toList()),
                values -> new Metric("95p размера ответа", String.valueOf(PercentileCalculator.calculate(95, values)))),

            Collectors.collectingAndThen(Collectors.mapping(LogRecord::remoteAddr, Collectors.toSet()),
                uniqueClients -> new Metric("Количество уникальных клиентов", String.valueOf(uniqueClients.size()))));
    }

    public static List<Collector<LogRecord, Object, Table>> getTableCollectors() {
        Comparator<Metric> comparator =
            Comparator.comparing((Metric metric) -> Long.parseLong(metric.value())).reversed();
        int limit = 5;
        return List.of(Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::request, Collectors.counting()),
                map -> new Table("Запрашиваемые ресурсы",
                    map.entrySet().stream().map(e -> new Metric(e.getKey(), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(limit)
                        .toList())),

            Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::status, Collectors.counting()),
                map -> new Table("Коды ответов", map.entrySet().stream()
                    .map(e -> new Metric(String.valueOf(e.getKey()), String.valueOf(e.getValue())))
                    .sorted(comparator)
                    .limit(limit)
                    .toList())),

            Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::remoteAddr, Collectors.counting()),
                map -> new Table("Частые клиенты",
                    map.entrySet().stream().map(e -> new Metric(e.getKey(), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(limit)
                        .toList())));
    }

}
