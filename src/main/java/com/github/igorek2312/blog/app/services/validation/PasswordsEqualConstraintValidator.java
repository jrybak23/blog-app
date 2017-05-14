package com.github.igorek2312.blog.app.services.validation;

import com.github.igorek2312.blog.app.transfer.ConfirmPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Igor Rybak
 */
public class PasswordsEqualConstraintValidator implements ConstraintValidator<ConfirmPasswordConstraint, ConfirmPassword> {
    public void initialize(ConfirmPasswordConstraint constraint) {
    }

    public boolean isValid(ConfirmPassword confirmPassword, ConstraintValidatorContext context) {
        return confirmPassword.isConfirmedPasswordValid();
    }
}
