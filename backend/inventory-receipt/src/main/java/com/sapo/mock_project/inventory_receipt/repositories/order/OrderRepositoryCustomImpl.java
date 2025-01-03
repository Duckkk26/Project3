package com.sapo.mock_project.inventory_receipt.repositories.order;

import com.sapo.mock_project.inventory_receipt.constants.enums.OrderStatus;
import com.sapo.mock_project.inventory_receipt.dtos.response.order.OrderGetListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<OrderGetListResponse> getFilteredOrders(String filterJson, String keyword,
                                                        String statuses, String supplierIds,
                                                        LocalDate startCreatedAt, LocalDate endCreatedAt,
                                                        LocalDate startExpectedAt, LocalDate endExpectedAt,
                                                        String productIds, String userCreatedIds,
                                                        String userCompletedIds, String userCancelledIds,
                                                        String tenantId,
                                                        int page, int size) {
        try {
            String sqlQuery = "{CALL filter_orders(:filterJson, :keyword, :status, :supplierIds, " +
                              ":startCreatedAt, :endCreatedAt, :startExpectedAt, :endExpectedAt, " +
                              ":productIds, :userCreatedIds, :userCompletedIds, :userCancelledIds, " +
                              ":tenantId, :page, :size)}";

            MapSqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("filterJson", filterJson)
                    .addValue("keyword", keyword)
                    .addValue("status", statuses)
                    .addValue("supplierIds", supplierIds)
                    .addValue("startCreatedAt", startCreatedAt)
                    .addValue("endCreatedAt", endCreatedAt)
                    .addValue("startExpectedAt", startExpectedAt)
                    .addValue("endExpectedAt", endExpectedAt)
                    .addValue("productIds", productIds)
                    .addValue("userCreatedIds", userCreatedIds)
                    .addValue("userCompletedIds", userCompletedIds)
                    .addValue("userCancelledIds", userCancelledIds)
                    .addValue("tenantId", tenantId)
                    .addValue("page", page)
                    .addValue("size", size);

            return namedParameterJdbcTemplate.query(sqlQuery, parameters, new OrderGetListResponseMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
    }

    public class OrderGetListResponseMapper implements RowMapper<OrderGetListResponse> {

        @Override
        public OrderGetListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderGetListResponse response = new OrderGetListResponse();

            // Lấy thông tin về metadata của ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Kiểm tra và ánh xạ các trường nếu có dữ liệu
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                if (rs.getObject(columnName) == null) {
                    continue;
                }

                switch (columnName) {
                    case "order_id":
                        response.setId(rs.getString("order_id"));
                        break;
                    case "order_sub_id":
                        response.setSubId(rs.getString("order_sub_id"));
                        break;
                    case "order_status":
                        response.setStatus(OrderStatus.valueOf(rs.getString("order_status")));
                        break;
                    case "supplier_name":
                        response.setSupplierName(rs.getString("supplier_name"));
                        break;
                    case "user_created_name":
                        response.setUserCreatedName(rs.getString("user_created_name"));
                        break;
                    case "total_quantity":
                        response.setTotalQuantity(rs.getBigDecimal("total_quantity"));
                        break;
                    case "total_price":
                        response.setTotalPrice(rs.getBigDecimal("total_price"));
                        break;
                    case "supplier_sub_id":
                        response.setSupplierSubId(rs.getString("supplier_sub_id"));
                        break;
                    case "supplier_sub_phone":
                        response.setSupplierPhone(rs.getString("supplier_sub_phone"));
                        break;
                    case "supplier_sub_email":
                        response.setSupplierEmail(rs.getString("supplier_sub_email"));
                        break;
                    case "user_completed_name":
                        response.setUserCompletedName(rs.getString("user_completed_name"));
                        break;
                    case "user_cancelled_name":
                        response.setUserCancelledName(rs.getString("user_cancelled_name"));
                        break;
                    case "order_note":
                        response.setNote(rs.getString("order_note"));
                        break;
                    case "order_tags":
                        response.setTags(rs.getString("order_tags"));
                        break;
                    case "order_expected_at":
                        Timestamp timestamp = rs.getTimestamp("order_expected_at");
                        if (timestamp != null) {
                            LocalDateTime expectedAt = timestamp.toLocalDateTime();
                            response.setExpectedAt(expectedAt);
                        }
                        break;
                    case "order_created_at":
                        Timestamp timestamp1 = rs.getTimestamp("order_created_at");
                        if (timestamp1 != null) {
                            LocalDateTime createdAt = timestamp1.toLocalDateTime();
                            response.setCreatedAt(createdAt);
                        }
                        break;
                    case "order_updated_at":
                        Timestamp timestamp2 = rs.getTimestamp("order_updated_at");
                        if (timestamp2 != null) {
                            LocalDateTime updatedAt = timestamp2.toLocalDateTime();
                            response.setUpdatedAt(updatedAt);
                        }
                        break;
                    default:
                        // Không làm gì cả nếu cột không tồn tại trong đối tượng
                        break;
                }
            }

            return response;
        }
    }

    // Tính tổng số sản phẩm
    @Override
    public int countTotalOrders(String filterJson, String keyword,
                                String status, String supplierIds,
                                LocalDate startCreatedAt, LocalDate endCreatedAt,
                                LocalDate startExpectedAt, LocalDate endExpectedAt,
                                String productIds, String userCreatedIds,
                                String userCompletedIds, String userCancelledIds, String tenantId
    ) {
        String sql = "SELECT COUNT(DISTINCT o.id) FROM orders o LEFT JOIN order_details od ON o.id = od.order_id WHERE 1=1 AND o.tenant_id = :tenantId";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("filterJson", filterJson)
                .addValue("keyword", keyword)
                .addValue("status", status)
                .addValue("supplierIds", supplierIds)
                .addValue("startCreatedAt", startCreatedAt)
                .addValue("endCreatedAt", endCreatedAt)
                .addValue("startExpectedAt", startExpectedAt)
                .addValue("endExpectedAt", endExpectedAt)
                .addValue("productIds", productIds)
                .addValue("userCreatedIds", userCreatedIds)
                .addValue("userCompletedIds", userCompletedIds)
                .addValue("userCancelledIds", userCancelledIds)
                .addValue("tenantId", tenantId);

        // Thêm điều kiện tìm kiếm vào truy vấn
        if (keyword != null) {
            sql += " AND (o.id LIKE :keyword OR o.sub_id LIKE :keyword)";
        }
        if (status != null) {
            sql += " AND FIND_IN_SET(o.status, :status)";
        }
        if (supplierIds != null) {
            sql += " AND FIND_IN_SET(o.supplier_id, :supplierIds)";
        }
        if (startCreatedAt != null && endCreatedAt != null) {
            sql += " AND o.created_at BETWEEN TIMESTAMP(:startCreatedAt, '00:00:00') AND TIMESTAMP(:endCreatedAt, '23:59:59')";
        }
        if (startCreatedAt != null && endCreatedAt == null) {
            sql += " AND o.created_at >= TIMESTAMP(:startCreatedAt, '00:00:00')";
        }
        if (startCreatedAt == null && endCreatedAt != null) {
            sql += " AND o.created_at <= TIMESTAMP(:endCreatedAt, '23:59:59')";
        }
        if (startExpectedAt != null && endExpectedAt != null) {
            sql += " AND o.expected_at BETWEEN TIMESTAMP(:startExpectedAt, '00:00:00') AND TIMESTAMP(:endExpectedAt, '23:59:59')";
        }
        if (startExpectedAt != null && endExpectedAt == null) {
            sql += " AND o.expected_at >= TIMESTAMP(:startExpectedAt, '00:00:00')";
        }
        if (startExpectedAt == null && endExpectedAt != null) {
            sql += " AND o.expected_at <= TIMESTAMP(:endExpectedAt, '23:59:59')";
        }
        if (productIds != null) {
            sql += " AND FIND_IN_SET(od.product_id, :productIds)";
        }
        if (userCreatedIds != null) {
            sql += " AND FIND_IN_SET(o.user_created_id, :userCreatedIds)";
        }
        if (userCompletedIds != null) {
            sql += " AND FIND_IN_SET(o.user_completed_id, :userCompletedIds)";
        }
        if (userCancelledIds != null) {
            sql += " AND FIND_IN_SET(o.user_cancelled_id, :userCancelledIds)";
        }

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}