package backend.academy.analyzer.stats;

import backend.academy.analyzer.log.LogRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

public class StatisticsCollector {

    public static <R> Collector<LogRecord, ?, List<R>> get(List<Collector<LogRecord, Object, R>> collectors) {
        return Collector.of(
                () -> new CollectorsBox<>(collectors),
                CollectorsBox::add,
                CollectorsBox::combine,
                CollectorsBox::get
        );
    }

    static class CollectorsBox<R> {

        List<Object> intermediateResults = new ArrayList<>();
        List<BiConsumer<Object, LogRecord>> accumulators = new ArrayList<>();
        List<BinaryOperator<Object>> combiners = new ArrayList<>();
        List<Function<Object, R>> finishers = new ArrayList<>();

        public CollectorsBox(List<Collector<LogRecord, Object, R>> collectors) {
            for (var collector : collectors) {
                var supplier = collector.supplier();
                intermediateResults.add(supplier.get());
                accumulators.add(collector.accumulator());
                combiners.add(collector.combiner());
                finishers.add(collector.finisher());
            }
        }

        void add(LogRecord logRecord) {
            for (int i = 0; i < accumulators.size(); i++) {
                accumulators.get(i).accept(intermediateResults.get(i), logRecord);
            }
        }

        StatisticsCollector.CollectorsBox<R> combine(StatisticsCollector.CollectorsBox<R> other) {
            for (int i = 0; i < intermediateResults.size(); i++) {
                intermediateResults.set(i, combiners.get(i).apply(intermediateResults.get(i), other.intermediateResults.get(i)));
            }
            return this;
        }

        List<R> get() {
            List<R> elements = new ArrayList<>();
            for (int i = 0; i < finishers.size(); i++) {
                elements.add(finishers.get(i).apply(intermediateResults.get(i)));
            }
            return elements;
        }
    }

}
