package org.workingproject.service.validation;

import org.springframework.stereotype.Service;
import org.workingproject.dto.UserRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserValidation {
    private static final Pattern LATIN_PATTERN = Pattern.compile("^[A-Za-z0-9._!-]+$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._@-]+$");


    public List<String> validate(UserRequestDto request) {
        List<String> errors = new ArrayList<>();

        if (request ==null){
            errors.add("Request cannot be empty");
        }
        validateField(errors,request.getEmail(),5);
        if (!EMAIL_PATTERN.matcher(request.getUserName()).matches()){
            errors.add("Username contain at list wrong characters ");
        }
        if (request.getUserName() ==null || request.getUserName().isBlank()){
            errors.add("Username cannot be empty");
        }
        if (request.getUserName().length()<3){
            errors.add("Username length cannot be less than 3 characters");
        }
        if (!LATIN_PATTERN.matcher(request.getUserName()).matches()){
            errors.add("Username contain at least wrong characters ");
        }
        validateField(errors,request.getPassword(),8);
        return errors;
    }

    public void validateField(List<String> errors, String field, int fielLength) {
        if (field ==null || field.isBlank()){
            errors.add("Username cannot be empty");
        }
        if (fielLength<3){
            errors.add("Username length cannot be less than 3 characters");
        }
    }
}
