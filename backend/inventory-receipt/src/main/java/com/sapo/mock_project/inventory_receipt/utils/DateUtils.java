package com.sapo.mock_project.inventory_receipt.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lớp tiện ích hỗ trợ thao tác với các đối tượng thời gian (LocalDate, LocalDateTime).
 */
public class DateUtils {

    /**
     * Chuyển đổi đối tượng LocalDate thành LocalDateTime với thời gian bắt đầu của ngày (00:00:00).
     *
     * @param date Đối tượng LocalDate cần chuyển đổi.
     * @return Đối tượng LocalDateTime tương ứng, nếu LocalDate không null; nếu null trả về null.
     */
    public static LocalDateTime getDateTimeFrom(LocalDate date) {
        return date != null ? date.atStartOfDay() : null;
    }

    /**
     * Chuyển đổi đối tượng LocalDate thành LocalDateTime với thời gian kết thúc của ngày (23:59:59).
     *
     * @param date Đối tượng LocalDate cần chuyển đổi.
     * @return Đối tượng LocalDateTime tương ứng, nếu LocalDate không null; nếu null trả về null.
     */
    public static LocalDateTime getDateTimeTo(LocalDate date) {
        return date != null ? date.atTime(23, 59, 59) : null;
    }
}
