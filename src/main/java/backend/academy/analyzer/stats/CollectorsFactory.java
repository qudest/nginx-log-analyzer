package backend.academy.analyzer.stats;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.utils.PercentileCalculator;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("MultipleStringLiterals")
public final class CollectorsFactory {

    private static final int PERCENTILE = 95;
    private static final int LIMIT = 5;

    public static List<Collector<LogRecord, Object, Metric>> getMetricCollectors() {
        return List.of(Collectors.collectingAndThen(Collectors.counting(),
                count -> new Metric("Количество запросов", String.valueOf(count))),

            Collectors.collectingAndThen(Collectors.averagingLong(LogRecord::bodyBytesSent),
                avg -> new Metric("Средний размер ответа", String.valueOf(avg))),

            Collectors.collectingAndThen(Collectors.mapping(LogRecord::bodyBytesSent, Collectors.toList()),
                values -> new Metric("95p размера ответа",
                    String.valueOf(PercentileCalculator.calculate(PERCENTILE, values)))),

            Collectors.collectingAndThen(Collectors.mapping(LogRecord::remoteAddr, Collectors.toSet()),
                uniqueClients -> new Metric("Количество уникальных клиентов", String.valueOf(uniqueClients.size()))));
    }

    public static List<Collector<LogRecord, Object, Table>> getTableCollectors() {
        Comparator<Metric> comparator =
            Comparator.comparing((Metric metric) -> Long.parseLong(metric.value())).reversed();
        return List.of(Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::request, Collectors.counting()),
                map -> new Table("Запрашиваемые ресурсы", List.of("Ресурс", "Количество"),
                    map.entrySet().stream().map(e -> new Metric(e.getKey(), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(LIMIT)
                        .toList())),

            Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::status, Collectors.counting()),
                map -> new Table("Коды ответов", List.of("Код", "Количество"),
                    map.entrySet().stream()
                        .map(e -> new Metric(String.valueOf(e.getKey()), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(LIMIT)
                        .toList())),

            Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::remoteAddr, Collectors.counting()),
                map -> new Table("Частые клиенты", List.of("IP", "Количество"),
                    map.entrySet().stream().map(e -> new Metric(e.getKey(), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(LIMIT)
                        .toList())));
    }

    private CollectorsFactory() {
    }

}
