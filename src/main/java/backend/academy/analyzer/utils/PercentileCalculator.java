package backend.academy.analyzer.utils;

import com.google.common.math.Quantiles;
import java.util.Collection;

public final class PercentileCalculator {

    public static double calculate(int percentileIndex, Collection<? extends Number> values) {
        if (values.isEmpty()) {
            return 0;
        }

        return Quantiles.percentiles().index(percentileIndex).compute(values);
    }

    private PercentileCalculator() {
    }

}
