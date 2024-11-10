package backend.academy.analyzer.utils;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PercentileCalculatorTest {

    @Test
    void calculateWithEmptyValues() {
        assertEquals(0, PercentileCalculator.calculate(50, List.of()));
    }

    @Test
    void calculateWithSingleValue() {
        assertEquals(10, PercentileCalculator.calculate(50, List.of(10)));
    }

    @Test
    void calculateWithMultipleValues() {
        assertEquals(20, PercentileCalculator.calculate(50, List.of(10, 20, 30)));
    }

    @Test
    void calculateWithPercentileIndexZero() {
        assertEquals(10, PercentileCalculator.calculate(0, List.of(10, 20, 30)));
    }

    @Test
    void calculateWithPercentileIndexHundred() {
        assertEquals(30, PercentileCalculator.calculate(100, List.of(10, 20, 30)));
    }

}
