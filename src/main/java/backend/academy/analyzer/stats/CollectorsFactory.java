package backend.academy.analyzer.stats;

import backend.academy.analyzer.log.LogRecord;
import backend.academy.analyzer.log.LogReport;
import backend.academy.analyzer.utils.PercentileCalculator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

            // доп статистика
            Collectors.collectingAndThen(Collectors.mapping(LogRecord::remoteAddr, Collectors.toSet()),
                uniqueClients -> new Metric("Количество уникальных клиентов", String.valueOf(uniqueClients.size()))));
    }

    public static List<Collector<LogRecord, Object, Table>> getTableCollectors() {
        Comparator<Metric> comparator =
            Comparator.comparing((Metric metric) -> Long.valueOf(metric.value())).reversed();
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

            // доп статистика
            Collectors.collectingAndThen(Collectors.groupingBy(LogRecord::remoteAddr, Collectors.counting()),
                map -> new Table("Частые клиенты", List.of("IP", "Количество"),
                    map.entrySet().stream().map(e -> new Metric(e.getKey(), String.valueOf(e.getValue())))
                        .sorted(comparator)
                        .limit(LIMIT)
                        .toList())));
    }

    public static Collector<LogRecord, ?, LogReport> getLogReportCollector(List<Metric> paramsMetrics) {
        return Collectors.teeing(
            MultiCollector.get(getMetricCollectors()),
            MultiCollector.get(getTableCollectors()),
            (metrics, tables) -> {
                List<Table> allTables = new ArrayList<>();
                allTables.add(new Table("Общая информация", List.of("Метрика", "Значение"),
                    Stream.concat(paramsMetrics.stream(), metrics.stream()).toList()
                ));
                allTables.addAll(tables);
                return LogReport.builder().tables(allTables).build();
            }
        );
    }

    private CollectorsFactory() {
    }

}
