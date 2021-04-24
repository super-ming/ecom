package com.example.demo.validation;

import com.example.demo.model.requests.CreateUserRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final CreateUserRequest user = (CreateUserRequest) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}