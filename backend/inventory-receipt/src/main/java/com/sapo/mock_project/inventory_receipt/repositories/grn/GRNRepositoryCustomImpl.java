package com.sapo.mock_project.inventory_receipt.repositories.grn;

import com.sapo.mock_project.inventory_receipt.constants.enums.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.grn.GRNGetListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GRNRepositoryCustomImpl implements GRNRepositoryCustom {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<GRNGetListResponse> getFilterGRN(String filterJson, String keyword, String statuses,
                                                 String receivedStatus, String supplierIds, String productIds,
                                                 LocalDate startCreatedAt, LocalDate endCreatedAt,
                                                 LocalDate startExpectedAt, LocalDate endExpectedAt,
                                                 String userCreatedIds, String userCompletedIds,
                                                 String userCancelledIds, String tenantId,
                                                 int page, int size) {
        try {
            String sqlQuery = "{CALL filter_grns(:filterJson, :keyword, :statuses, :received_statuses, :supplierIds, " +
                              ":startCreatedAt, :endCreatedAt, :startExpectedAt, :endExpectedAt, " +
                              ":productIds, :userCreatedIds, :userCompletedIds, :userCancelledIds, :tenantId, :page, :size)}";

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("filterJson", filterJson)
                    .addValue("keyword", keyword)
                    .addValue("statuses", statuses)
                    .addValue("received_statuses", receivedStatus)
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

            return namedParameterJdbcTemplate.query(sqlQuery, params, new GRNGetListResponseMapper());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error executing query: " + e.getMessage());
        }
    }

    @Override
    public int countTotalGRN(String filterJson, String keyword, String statuses, String receivedStatus,
                             String supplierIds, String productIds, LocalDate startCreatedAt, LocalDate endCreatedAt,
                             LocalDate startExpectedAt, LocalDate endExpectedAt, String userCreatedIds,
                             String userCompletedIds, String userCancelledIds, String tenantId) {
        String sql = "SELECT COUNT(DISTINCT g.id) FROM grns g LEFT JOIN grn_products gp ON gp.grn_id = g.id WHERE 1 = 1 AND g.tenant_id = :tenantId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filterJson", filterJson)
                .addValue("keyword", keyword)
                .addValue("statuses", statuses)
                .addValue("received_statuses", receivedStatus)
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


        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (g.id LIKE :keyword OR g.sub_id LIKE :keyword OR g.tags LIKE :keyword)";
        }
        if (statuses != null && !statuses.isEmpty()) {
            sql += " AND FIND_IN_SET(g.status, :statuses)";
        }
        if (receivedStatus != null && !receivedStatus.isEmpty()) {
            sql += " AND FIND_IN_SET(g.received_status, :received_statuses)";
        }
        if (supplierIds != null && !supplierIds.isEmpty()) {
            sql += " AND FIND_IN_SET(g.supplier_id, :supplierIds)";
        }
        if (productIds != null && !productIds.isEmpty()) {
            sql += " AND FIND_IN_SET(gp.product_id, :productIds)";
        }
        if (startCreatedAt != null && endCreatedAt != null) {
            sql += " AND g.created_at BETWEEN TIMESTAMP(:startCreatedAt, '00:00:00') AND TIMESTAMP(:endCreatedAt, '23:59:59')";
        }
        if (startCreatedAt != null && endCreatedAt == null) {
            sql += " AND g.created_at >= TIMESTAMP(:startCreatedAt, '00:00:00')";
        }
        if (startCreatedAt == null && endCreatedAt != null) {
            sql += " AND g.created_at <= TIMESTAMP(:endCreatedAt, '23:59:59')";
        }
        if (startExpectedAt != null && endExpectedAt != null) {
            sql += " AND g.expected_at BETWEEN TIMESTAMP(:startExpectedAt, '00:00:00') AND TIMESTAMP(:endExpectedAt, '23:59:59')";
        }
        if (startExpectedAt != null && endExpectedAt == null) {
            sql += " AND g.expected_at >= TIMESTAMP(:startExpectedAt, '00:00:00')";
        }
        if (startExpectedAt == null && endExpectedAt != null) {
            sql += " AND g.expected_at <= TIMESTAMP(:endExpectedAt, '23:59:59')";
        }
        if (userCreatedIds != null) {
            sql += " AND FIND_IN_SET(g.user_created_id, :userCreatedIds)";
        }
        if (userCompletedIds != null) {
            sql += " AND FIND_IN_SET(g.user_completed_id, :userCompletedIds)";
        }
        if (userCancelledIds != null) {
            sql += " AND FIND_IN_SET(g.user_cancelled_id, :userCancelledIds)";
        }

        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    public class GRNGetListResponseMapper implements RowMapper<GRNGetListResponse> {

        @Override
        public GRNGetListResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            GRNGetListResponse response = new GRNGetListResponse();

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
                    case "grn_id":
                        response.setId(rs.getString("grn_id"));
                        break;
                    case "grn_sub_id":
                        response.setSubId(rs.getString("grn_sub_id"));
                        break;
                    case "grn_status":
                        response.setStatus(GRNStatus.valueOf(rs.getString("grn_status")));
                        break;
                    case "grn_receive_status":
                        response.setReceiveStatus(GRNReceiveStatus.valueOf(rs.getString("grn_receive_status")));
                        break;
                    case "grn_payment_status":
                        response.setPaymentStatus(GRNPaymentStatus.valueOf(rs.getString("grn_payment_status")));
                        break;
                    case "grn_return_status":
                        response.setReturnStatus(GRNReturnStatus.valueOf(rs.getString("grn_return_status")));
                        break;
                    case "grn_refund_status":
                        response.setRefundStatus(GRNRefundStatus.valueOf(rs.getString("grn_refund_status")));
                        break;
                    case "grn_received_at":
                        response.setReceivedAt(rs.getTimestamp("grn_received_at").toLocalDateTime());
                        break;
                    case "grn_expected_at":
                        response.setExpectedAt(rs.getTimestamp("grn_expected_at").toLocalDateTime());
                        break;
                    case "grn_cancelled_at":
                        response.setCancelledAt(rs.getTimestamp("grn_cancelled_at").toLocalDateTime());
                        break;
                    case "grn_payment_at":
                        response.setPaymentAt(rs.getTimestamp("grn_payment_at").toLocalDateTime());
                        break;
                    case "grn_total_received_quantity":
                        response.setTotalReceivedQuantity(rs.getBigDecimal("grn_total_received_quantity"));
                        break;
                    case "grn_total_value":
                        response.setTotalValue(rs.getBigDecimal("grn_total_value"));
                        break;
                    case "grn_supplier_name":
                        response.setSupplierName(rs.getString("grn_supplier_name"));
                        break;
                    case "grn_supplier_sub_id":
                        response.setSupplierSubId(rs.getString("grn_supplier_sub_id"));
                        break;
                    case "grn_supplier_phone":
                        response.setSupplierPhone(rs.getString("grn_supplier_phone"));
                        break;
                    case "grn_supplier_email":
                        response.setSupplierEmail(rs.getString("grn_supplier_email"));
                        break;
                    case "grn_user_created_name":
                        response.setUserCreatedName(rs.getString("grn_user_created_name"));
                        break;
                    case "grn_user_completed_name":
                        response.setUserCompletedName(rs.getString("grn_user_completed_name"));
                        break;
                    case "grn_user_cancelled_name":
                        response.setUserCancelledName(rs.getString("grn_user_cancelled_name"));
                        break;
                    case "grn_note":
                        response.setNote(rs.getString("grn_note"));
                        break;
                    case "grn_tags":
                        response.setTags(rs.getString("grn_tags"));
                        break;
                    case "grn_created_at":
                        response.setCreatedAt(rs.getTimestamp("grn_created_at").toLocalDateTime());
                        break;
                    case "grn_updated_at":
                        response.setUpdatedAt(rs.getTimestamp("grn_updated_at").toLocalDateTime());
                        break;
                    case "include_grn_order_sub_id":
                        response.setOrderSubId(rs.getString("include_grn_order_sub_id"));
                        break;
                    default:
                        // Không làm gì cả nếu cột không tồn tại trong đối tượng
                        break;
                }
            }

            return response;
        }
    }
}
