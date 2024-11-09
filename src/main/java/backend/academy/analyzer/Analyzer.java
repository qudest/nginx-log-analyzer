package backend.academy.analyzer;

import backend.academy.analyzer.filter.FilterChain;
import backend.academy.analyzer.log.LogReport;
import backend.academy.analyzer.params.Params;
import backend.academy.analyzer.parser.LogParser;
import backend.academy.analyzer.parser.ParamsParser;
import backend.academy.analyzer.reader.Reader;
import backend.academy.analyzer.reader.ReaderFactory;
import backend.academy.analyzer.render.Renderer;
import backend.academy.analyzer.render.RendererFactory;
import backend.academy.analyzer.stats.CollectorsFactory;
import backend.academy.analyzer.stats.Metric;
import backend.academy.analyzer.stats.StatisticsCollector;
import backend.academy.analyzer.stats.Table;
import backend.academy.analyzer.writer.Writer;
import com.beust.jcommander.ParameterException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Analyzer {

    private final PrintStream errorOutput;
    private final ParamsParser paramsParser;
    private final LogParser logParser;
    private final FilterChain filterChain;
    private final Writer writer;

    public Analyzer(
        PrintStream errorOutput, ParamsParser paramsParser, LogParser logParser, FilterChain filterChain, Writer writer
    ) {
        this.errorOutput = errorOutput;
        this.paramsParser = paramsParser;
        this.logParser = logParser;
        this.filterChain = filterChain;
        this.writer = writer;
    }

    public void run(String[] args) {
        Params params;
        try {
            params = paramsParser.parse(args);
        } catch (ParameterException e) {
            errorOutput.println(e.getMessage());
            return;
        }
        filterChain.params(params);

        List<String> paths = params.path();
        Stream<String> stream = Stream.empty();
        for (String path : paths) {
            Reader reader = ReaderFactory.getReader(path);
            try {
                stream = Stream.concat(stream, reader.read(path));
            } catch (Exception e) {
                errorOutput.println(path + ": " + e.getMessage());
            }
        }

        List<Metric> headers = List.of(
            new Metric("Файл(-ы)", String.join(", ", paths)),
            new Metric("Начальная дата", params.from() == null ? "-" : params.from().toString()),
            new Metric("Конечная дата", params.to() == null ? "-" : params.to().toString())
        );

        LogReport logReport = stream
            .map(logParser::parse)
            .filter(filterChain::filter)
            .collect(
                Collectors.teeing(
                    StatisticsCollector.get(CollectorsFactory.getMetricCollectors()),
                    StatisticsCollector.get(CollectorsFactory.getTableCollectors()),
                    (metrics, tables) -> {
                        List<Table> allTables = new ArrayList<>();
                        allTables.add(new Table("Общая информация", List.of("Метрика", "Значение"),
                            Stream.concat(headers.stream(), metrics.stream()).toList()
                        ));
                        allTables.addAll(tables);

                        return LogReport.builder()
                            .tables(allTables)
                            .build();
                    }
                )
            );

        Renderer renderer = RendererFactory.getRenderer(params.format());
        String render = renderer.render(logReport);

        try {
            writer.write(render, params.format());
        } catch (Exception e) {
            errorOutput.println(e.getMessage());
        }
    }

}
