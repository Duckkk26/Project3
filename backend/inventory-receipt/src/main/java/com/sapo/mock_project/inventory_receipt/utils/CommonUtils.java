package com.sapo.mock_project.inventory_receipt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapo.mock_project.inventory_receipt.constants.DateTimePattern;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtils {

    public static Map<String, Boolean> getFilterParamsFromCookie(String name, HttpServletRequest request) {
        // Lấy tất cả các cookie từ request
        String stringCookie = request.getHeader(name);

        if (stringCookie != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // Chuyển giá trị của cookie thành Map
                Map<String, Boolean> filterParams = objectMapper.readValue(stringCookie, Map.class);

                return filterParams;
            } catch (Exception e) {
                // Xử lý ngoại lệ (ví dụ: lỗi JSON không hợp lệ)
                e.printStackTrace();
                return new HashMap<>(); // Trả về map rỗng nếu có lỗi
            }
        }

        // Trả về map rỗng nếu không tìm thấy cookie hoặc không có cookie nào
        return new HashMap<>();
    }

    public static Object getFieldValue(Object object, String field) {
        try {
            // Lấy phương thức get tương ứng từ đối tượng supplier
            Method getMethod = object.getClass().getMethod("get" + StringUtils.snakeCaseToCamelCase(field));

            // Gọi phương thức get để lấy giá trị của trường
            return getMethod.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value for field " + field, e);
        }
    }

    /**
     * Chuyển đổi giá trị thành kiểu dữ liệu phù hợp với trường
     */
    public static Object convertValueForField(Field field, Object value) {
        if (value == null) {
            return null;
        }

        Class<?> fieldType = field.getType();

        // Kiểm tra kiểu dữ liệu và chuyển đổi nếu cần
        if (fieldType.isEnum()) {
            // Nếu là Enum, trả về tên của enum
            return value;
        } else if (fieldType == String.class) {
            // Nếu là chuỗi, chuyển giá trị thành chuỗi
            return value.toString();
        } else if (fieldType == Integer.class || fieldType == int.class) {
            // Nếu là số nguyên
            return Integer.parseInt(value.toString());
        } else if (fieldType == Long.class || fieldType == long.class) {
            // Nếu là số nguyên dài
            return Long.parseLong(value.toString());
        } else if (fieldType == BigDecimal.class) {
            // Nếu là số thập phân
            return new BigDecimal(value.toString());
        } else if (fieldType == LocalDateTime.class) {
            String stringValue = value.toString();

            if (stringValue.length() == "yyyy-MM-ddTHH:mm".length()) {
                stringValue = stringValue + ":00";
            }

            // Nếu là LocalDateTime
            return LocalDateTime.parse(stringValue, DateTimeFormatter.ofPattern(DateTimePattern.YYYYMMDDHHMMSS));
        }

        // Trường hợp mặc định, trả về giá trị ban đầu
        return value;
    }

    // Tìm kiếm field trong cả lớp hiện tại và lớp cha
    public static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;

        // Duyệt qua các lớp cha nếu không tìm thấy field trong lớp hiện tại
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass(); // Chuyển sang lớp cha
            }
        }

        // Nếu không tìm thấy field trong cả lớp và lớp cha
        throw new NoSuchFieldException("Field " + fieldName + " not found in class hierarchy.");
    }

    public static <T> String joinParams(List<T> paramValue) {
        return (paramValue == null || paramValue.isEmpty())
                ? null
                : paramValue.stream()
                .map(Object::toString) // Ensure all values are converted to String
                .collect(Collectors.joining(","));
    }
}
