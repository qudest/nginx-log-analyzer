package backend.academy.analyzer.render;

import backend.academy.analyzer.log.LogReport;
import backend.academy.analyzer.stats.Metric;
import backend.academy.analyzer.stats.Table;
import java.util.List;

public abstract class Renderer {

    public String render(LogReport logReport) {
        StringBuilder sb = new StringBuilder();

        for (Table table : logReport.tables()) {
            sb.append(renderHeader(table.name()));
            sb.append(renderTableHeaders(table.headers()));
            sb.append(renderTableValues(table.values()));
        }

        return sb.toString();
    }

    abstract String renderHeader(String header);

    abstract String renderTableHeaders(List<String> headers);

    abstract String renderTableValues(List<Metric> values);

}
