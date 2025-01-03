package com.sapo.mock_project.inventory_receipt.utils;

import java.lang.reflect.Field;

public class StringUtils {
    public static String snakeCaseToCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Tách chuỗi thành các từ dựa trên dấu gạch dưới
        String[] words = str.split("_");

        // Tạo một StringBuilder để gộp các từ đã chuyển đổi
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                // Chữ cái đầu tiên của mỗi từ cần viết hoa
                camelCaseString.append(Character.toUpperCase(words[i].charAt(0)))
                        .append(words[i].substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }

    public static String snakeCaseToEqualCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Tách chuỗi thành các từ dựa trên dấu gạch dưới
        String[] words = str.split("_");

        // Tạo một StringBuilder để gộp các từ đã chuyển đổi
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                // Chữ cái đầu tiên của từ đầu tiên không cần viết hoa
                camelCaseString.append(words[i].toLowerCase());
                continue;
            }
            if (words[i].length() > 0) {
                // Chữ cái đầu tiên của mỗi từ cần viết hoa
                camelCaseString.append(Character.toUpperCase(words[i].charAt(0)))
                        .append(words[i].substring(1).toLowerCase());
            }
        }

        return camelCaseString.toString();
    }

    public static void trimAllStringFields(Object object) {
        if (object == null) {
            return;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);

                // Kiểm tra nếu field là kiểu String thì trim
                if (value != null && field.getType().equals(String.class)) {
                    String trimmedValue = ((String) value).trim();
                    field.set(object, trimmedValue);
                }

                // Nếu field là một đối tượng và là class do mình định nghĩa
                else if (value != null && !field.getType().isPrimitive()) {
                    // Kiểm tra xem class của field có thuộc package của mình không
                    if (field.getType().getPackage() != null && field.getType().getPackage().getName().startsWith("com.sapo.mock_project.inventory_receipt.dtos.request")) {
                        trimAllStringFields(value);  // Gọi đệ quy cho đối tượng class khác do mình định nghĩa
                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace(); // Log lỗi nếu có vấn đề về truy cập field
            }
        }
    }
}
