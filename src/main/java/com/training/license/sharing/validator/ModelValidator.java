package com.training.license.sharing.validator;

import com.training.license.sharing.util.CustomExceptions.ModelNotValidException;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import java.util.HashMap;
import java.util.Map;
@Log4j2
public class ModelValidator {
    private ModelValidator() {
    }

    public static void validateData(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                log.error(error.getDefaultMessage());
                errors.put(error.getField(), error.getDefaultMessage());
            });

            throw new ModelNotValidException(errors);
        }
    }
}