package backend.academy.analyzer.render;

import backend.academy.analyzer.stats.Metric;
import java.util.List;

public class AdocRenderer extends Renderer {

    private static final String HEADER = "====";
    private static final String VERTICAL_LINE = "|";
    private static final String TABLE_ENDS = VERTICAL_LINE + "===";
    private static final String ITALIC = "_";
    private static final String BOLD = "*";

    private static final Character SPACE = ' ';
    private static final Character LINE_BREAK = '\n';

    @Override
    String renderTableName(String name) {
        return HEADER + SPACE + name + LINE_BREAK + LINE_BREAK;
    }

    @Override
    String renderTableHeaders(List<String> headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(TABLE_ENDS).append(LINE_BREAK);
        for (String header : headers) {
            sb.append(VERTICAL_LINE).append(BOLD).append(header).append(BOLD);
        }
        sb.append(LINE_BREAK);
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
                .append(LINE_BREAK);
        }
        sb.append(TABLE_ENDS).append(LINE_BREAK).append(LINE_BREAK);
        return sb.toString();
    }

}
