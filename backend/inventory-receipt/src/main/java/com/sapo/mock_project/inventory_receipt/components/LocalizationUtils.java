package com.sapo.mock_project.inventory_receipt.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * Lớp này cung cấp các phương thức tiện ích để hỗ trợ việc nội địa hóa (localization) trong ứng dụng.
 *
 * <p>Sử dụng MessageSource của Spring để lấy các thông điệp đã được bản địa hóa
 * dựa trên locale hiện tại.</p>
 */
@RequiredArgsConstructor
@Component
public class LocalizationUtils {

    /**
     * MessageSource để lấy thông điệp đã được bản địa hóa.
     *
     * <p>MessageSource chứa các message dựa trên key và locale cụ thể.</p>
     */
    private final MessageSource messageSource;

    /**
     * LocaleResolver để xác định locale hiện tại từ request.
     *
     * <p>LocaleResolver được dùng để lấy thông tin về locale dựa trên yêu cầu (request)
     * của người dùng hiện tại.</p>
     */
    private final LocaleResolver localeResolver;

    /**
     * Lấy thông điệp đã được bản địa hóa dựa trên key thông điệp và các tham số tùy chọn.
     *
     * @param messageKey key của thông điệp cần bản địa hóa
     * @param params     các tham số tùy chọn được chèn vào thông điệp (nếu có)
     * @return chuỗi thông điệp đã được bản địa hóa
     */
    public String getLocalizedMessage(String messageKey, Object... params) {
        // Lấy HttpServletRequest hiện tại từ RequestContextHolder của Spring
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Xác định locale hiện tại của người dùng từ request
        Locale locale = localeResolver.resolveLocale(request);

        // Trả về thông điệp đã được bản địa hóa dựa trên key và locale
        return messageSource.getMessage(messageKey, params, locale);
    }
}
