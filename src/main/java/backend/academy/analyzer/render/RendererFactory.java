package backend.academy.analyzer.render;

import backend.academy.analyzer.params.OutputFormat;

public class RendererFactory {

    public static Renderer getRenderer(OutputFormat outputFormat) {
        if (outputFormat == OutputFormat.ADOC) {
            return new AdocRenderer();
        }
        return new MarkdownRenderer();
    }

}
