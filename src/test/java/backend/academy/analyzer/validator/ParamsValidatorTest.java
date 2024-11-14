package backend.academy.analyzer.validator;

import backend.academy.analyzer.params.FilterField;
import backend.academy.analyzer.params.Params;
import com.beust.jcommander.ParameterException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ParamsValidatorTest {

    @Test
    void validateWithValidParams() {
        Params params = Params.builder()
            .from(Instant.parse("2024-01-01T00:00:00Z"))
            .to(Instant.parse("2024-01-02T00:00:00Z"))
            .filterField(FilterField.STATUS)
            .filterValue("value")
            .paths(List.of("valid/path"))
            .build();
        ParamsValidator.validate(params);
    }

    @Test
    void validateWithInvalidTimeRange() {
        Params params = Params.builder()
            .from(Instant.parse("2023-01-02T00:00:00Z"))
            .to(Instant.parse("2023-01-01T00:00:00Z"))
            .build();
        assertThrows(ParameterException.class, () -> ParamsValidator.validate(params));
    }

    @Test
    void validateWithFilterValueWithoutFilterField() {
        Params params = Params.builder()
            .filterValue("value")
            .build();
        assertThrows(ParameterException.class, () -> ParamsValidator.validate(params));
    }

    @Test
    void validateWithEmptyPath() {
        Params params = Params.builder()
            .paths(List.of())
            .build();
        assertThrows(ParameterException.class, () -> ParamsValidator.validate(params));
    }

    @Test
    void validateWithNullPath() {
        Params params = Params.builder()
            .paths(null)
            .build();
        assertThrows(ParameterException.class, () -> ParamsValidator.validate(params));
    }
}
