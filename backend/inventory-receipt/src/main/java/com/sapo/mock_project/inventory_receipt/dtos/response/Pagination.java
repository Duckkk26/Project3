package com.sapo.mock_project.inventory_receipt.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Lớp đại diện cho đối tượng phân trang trong phản hồi API.
 *
 * @param <T> kiểu dữ liệu của dữ liệu phân trang.
 */
@Builder
public class Pagination<T> {

    /**
     * Dữ liệu của trang hiện tại.
     */
    @Schema(description = "Dữ liệu của trang hiện tại", example = "Danh sách sản phẩm")
    @JsonProperty("data")
    private T data;

    /**
     * Tổng số trang có sẵn.
     */
    @Schema(description = "Tổng số trang có sẵn", example = "10")
    @JsonProperty("total_page")
    private int totalPage;

    /**
     * Tổng số mục (items) trong tất cả các trang.
     */
    @Schema(description = "Tổng số mục (items) trong tất cả các trang", example = "100")
    @JsonProperty("total_items")
    private long totalItems;
}
