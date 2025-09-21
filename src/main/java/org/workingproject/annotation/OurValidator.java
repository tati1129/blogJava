package org.workingproject.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class OurValidator implements ConstraintValidator<OurValidation, String> {
    private String defaultMessage;

    @Override
    public void initialize(OurValidation constraintAnnotation) {
        this.defaultMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            addConstraintViolation(context, defaultMessage + "\n Password cannot be null");
            return false;
        }

        List<String> errors = new ArrayList<>();
        if (password.length() < 8) {
            errors.add("Password length should be at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            errors.add("Password must contain at least one digit");

        }

        if (!errors.isEmpty()) {
            addConstraintViolation(context, defaultMessage);
            for (String error : errors) {
                addConstraintViolation(context, error);
            }
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}


