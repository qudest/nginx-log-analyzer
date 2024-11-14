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

        List<CollectorData<T, R>> collectorDataList;

        CollectorsBox(List<Collector<T, Object, R>> collectors) {
            collectorDataList = new ArrayList<>(collectors.size());
            for (var collector : collectors) {
                var supplier = collector.supplier();
                collectorDataList.add(new CollectorData<>(
                    supplier.get(),
                    collector.accumulator(),
                    collector.combiner(),
                    collector.finisher()));
            }
        }

        void add(T t) {
            for (var data : collectorDataList) {
                data.accumulator.accept(data.intermediateResult, t);
            }
        }

        MultiCollector.CollectorsBox<T, R> combine(MultiCollector.CollectorsBox<T, R> other) {
            for (int i = 0; i < collectorDataList.size(); i++) {
                var data = collectorDataList.get(i);
                var otherData = other.collectorDataList.get(i);
                data.intermediateResult = data.combiner.apply(data.intermediateResult, otherData.intermediateResult);
            }
            return this;
        }

        List<R> get() {
            List<R> elements = new ArrayList<>(collectorDataList.size());
            for (var data : collectorDataList) {
                elements.add(data.finisher.apply(data.intermediateResult));
            }
            return elements;
        }

        static class CollectorData<T, R> {
            Object intermediateResult;
            BiConsumer<Object, T> accumulator;
            BinaryOperator<Object> combiner;
            Function<Object, R> finisher;

            CollectorData(
                Object intermediateResult, BiConsumer<Object, T> accumulator,
                BinaryOperator<Object> combiner, Function<Object, R> finisher
            ) {
                this.intermediateResult = intermediateResult;
                this.accumulator = accumulator;
                this.combiner = combiner;
                this.finisher = finisher;
            }
        }

    }

}
