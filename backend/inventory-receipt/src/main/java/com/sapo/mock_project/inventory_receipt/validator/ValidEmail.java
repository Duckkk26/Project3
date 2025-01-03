package com.sapo.mock_project.inventory_receipt.validator;

import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Định nghĩa annotation
@Constraint(validatedBy = EmailValidator.class) // Liên kết với validator
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default MessageValidateKeys.EMAIL_INVALID_PATTERN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}