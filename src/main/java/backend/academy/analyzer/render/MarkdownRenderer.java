package backend.academy.analyzer.render;

import backend.academy.analyzer.stats.Metric;
import java.util.List;

public class MarkdownRenderer extends Renderer {

    private static final String HEADER = "####";
    private static final Character VERTICAL_LINE = '|';
    private static final Character HORIZONTAL_LINE = '-';
    private static final Character ITALIC = '*';

    private static final Character SPACE = ' ';
    private static final Character LINE_BREAK = '\n';

    @Override
    String renderTableName(String name) {
        return HEADER + SPACE + name + LINE_BREAK + LINE_BREAK;
    }

    @Override
    String renderTableHeaders(List<String> headers) {
        StringBuilder sb = new StringBuilder();
        for (String header : headers) {
            sb.append(VERTICAL_LINE).append(header);
        }
        sb.append(VERTICAL_LINE).append(LINE_BREAK);
        for (int i = 0; i < headers.size(); i++) {
            sb.append(VERTICAL_LINE).append(HORIZONTAL_LINE);
        }
        sb.append(VERTICAL_LINE).append(LINE_BREAK);
        return sb.toString();
    }

    @Override
    String renderTableValues(List<Metric> values) {
        StringBuilder sb = new StringBuilder();
        for (Metric metric : values) {
            sb.append(VERTICAL_LINE)
                .append(ITALIC)
                .append(metric.name())
                .append(ITALIC)
                .append(VERTICAL_LINE)
                .append(metric.value())
                .append(VERTICAL_LINE)
                .append(LINE_BREAK);
        }
        sb.append(LINE_BREAK);
        return sb.toString();
    }

}
