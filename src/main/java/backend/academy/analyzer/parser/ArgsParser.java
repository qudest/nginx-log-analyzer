package backend.academy.analyzer.parser;

import backend.academy.analyzer.params.Params;
import backend.academy.analyzer.validator.ParamsValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

public class ArgsParser implements ParamsParser {

    @Override
    public Params parse(String[] args) {
        Params params = new Params();
        JCommander jc = JCommander.newBuilder()
                .addObject(params)
                .build();
        try {
            jc.parse(args);
            ParamsValidator.validate(params);
        } catch (ParameterException e) {
            jc.usage();
            throw e;
        }
        return params;
    }

}
