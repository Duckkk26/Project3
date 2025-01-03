package com.sapo.mock_project.inventory_receipt.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Bạn có thể thay đổi cách xử lý null và empty ở đây
        }

        // Kiểm tra định dạng số điện thoại
        return value.matches("^0\\d{9}$"); // Bắt đầu bằng 0 và có 10 số
    }
}
