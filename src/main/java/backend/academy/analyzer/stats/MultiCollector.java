package backend.academy.analyzer.stats;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

public final class MultiCollector {

    public static <T, R> Collector<T, ?, List<R>> get(List<Collector<T, Object, R>> collectors) {
        return Collector.of(
            () -> new CollectorsBox<>(collectors),
            CollectorsBox::add,
            CollectorsBox::combine,
            CollectorsBox::get
        );
    }

    private MultiCollector() {
    }

    static class CollectorsBox<T, R> {

        List<Object> intermediateResults = new ArrayList<>();
        List<BiConsumer<Object, T>> accumulators = new ArrayList<>();
        List<BinaryOperator<Object>> combiners = new ArrayList<>();
        List<Function<Object, R>> finishers = new ArrayList<>();

        CollectorsBox(List<Collector<T, Object, R>> collectors) {
            for (var collector : collectors) {
                var supplier = collector.supplier();
                intermediateResults.add(supplier.get());
                accumulators.add(collector.accumulator());
                combiners.add(collector.combiner());
                finishers.add(collector.finisher());
            }
        }

        void add(T t) {
            for (int i = 0; i < accumulators.size(); i++) {
                accumulators.get(i).accept(intermediateResults.get(i), t);
            }
        }

        MultiCollector.CollectorsBox<T, R> combine(MultiCollector.CollectorsBox<T, R> other) {
            for (int i = 0; i < intermediateResults.size(); i++) {
                intermediateResults.set(i,
                    combiners.get(i).apply(intermediateResults.get(i), other.intermediateResults.get(i)));
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
