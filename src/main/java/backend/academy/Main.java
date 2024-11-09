package backend.academy;

import backend.academy.analyzer.Analyzer;
import backend.academy.analyzer.filter.FilterChain;
import backend.academy.analyzer.filter.TimestampFilter;
import backend.academy.analyzer.filter.ValueFilter;
import backend.academy.analyzer.parser.ArgsParser;
import backend.academy.analyzer.parser.LogParser;
import backend.academy.analyzer.parser.NginxLogParser;
import backend.academy.analyzer.parser.ParamsParser;
import backend.academy.analyzer.writer.FileWriter;
import backend.academy.analyzer.writer.Writer;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        ParamsParser paramsParser = new ArgsParser();
        LogParser logParser = new NginxLogParser();
        FilterChain filterChain = new FilterChain(
            List.of(
                new TimestampFilter(),
                new ValueFilter()
            ));
        Writer writer = new FileWriter();
        Analyzer analyzer = new Analyzer(System.err, paramsParser, logParser, filterChain, writer);
        analyzer.run(args);
    }
}
