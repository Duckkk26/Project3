package com.sapo.mock_project.inventory_receipt.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Bạn có thể thay đổi cách xử lý null và empty ở đây
        }

        // Kiểm tra độ dài, có chữ hoa và có số
        return value.length() > 6 && value.matches(".*[A-Z].*") && value.matches(".*\\d.*");
    }
}
