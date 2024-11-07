package backend.academy.analyzer.validator;

import backend.academy.analyzer.params.Params;
import com.beust.jcommander.ParameterException;
import java.time.Instant;

public final class ParamsValidator {

    public static void validate(Params params) {
        validateTimeRange(params);
    }

    private static void validateTimeRange(Params params) {
        Instant from = params.from();
        Instant to = params.to();
        if (from != null && to != null && from.isAfter(to)) {
            throw new ParameterException("The start time cannot be later than the end time");
        }
    }

    private ParamsValidator() {
    }

}
