package ru.Kodrik.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.Kodrik.MySecondTestAppSpringBoot.exception.ValidationFailedException;

@Service
public class RequestValidationService implements ValidationService {

    @Override
    public void isValid(BindingResult bindingResult) throws ValidationFailedException {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            bindingResult.getFieldErrors().forEach(err ->
                    sb.append(err.getField())
                            .append(": ")
                            .append(err.getDefaultMessage())
                            .append("; "));
            throw new ValidationFailedException(sb.toString());
        }
    }

}
