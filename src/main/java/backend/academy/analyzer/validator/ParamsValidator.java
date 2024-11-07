package backend.academy.analyzer.validator;

import backend.academy.analyzer.params.Params;
import com.beust.jcommander.ParameterException;
import java.time.Instant;

public final class ParamsValidator {

    public static void validate(Params params) {
        validateTimeRange(params);
        validateFilterValue(params);
    }

    private static void validateTimeRange(Params params) {
        Instant from = params.from();
        Instant to = params.to();
        if (from != null && to != null && from.isAfter(to)) {
            throw new ParameterException("The start time cannot be later than the end time");
        }
    }

    private static void validateFilterValue(Params params) {
        if (params.filterField() == null && params.filterValue() != null) {
            throw new ParameterException("If you use a filter value, the filter field is required");
        }
    }

    private ParamsValidator() {
    }

}
