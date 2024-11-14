package backend.academy.analyzer;

import backend.academy.analyzer.filter.FilterChain;
import backend.academy.analyzer.log.LogReport;
import backend.academy.analyzer.params.Params;
import backend.academy.analyzer.parser.LogParser;
import backend.academy.analyzer.parser.ParamsParser;
import backend.academy.analyzer.render.Renderer;
import backend.academy.analyzer.render.RendererFactory;
import backend.academy.analyzer.stats.CollectorsFactory;
import backend.academy.analyzer.stats.Metric;
import backend.academy.analyzer.utils.ReaderUtils;
import backend.academy.analyzer.writer.Writer;
import com.beust.jcommander.ParameterException;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
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

        Stream<String> lines = ReaderUtils.readPaths(params.paths());
        LogReport logReport = getLogReport(lines, params.getInfoMetrics());
        Renderer renderer = RendererFactory.getRenderer(params.format());

        try {
            writer.write(renderer.render(logReport), params.format());
        } catch (Exception e) {
            errorOutput.println(e.getMessage());
        }
    }

    private LogReport getLogReport(Stream<String> lines, List<Metric> paramsInfoMetrics) {
        return lines
            .map(logParser::parse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .filter(filterChain::filter)
            .collect(CollectorsFactory.getLogReportCollector(paramsInfoMetrics));
    }

}
