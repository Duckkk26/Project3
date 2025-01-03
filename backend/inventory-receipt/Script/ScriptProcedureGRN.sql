DELIMITER $$

CREATE PROCEDURE filter_grns(
    IN p_filter_json JSON,
    IN p_keyword VARCHAR(255),
    IN p_statuses VARCHAR(255),
    IN p_received_statuses VARCHAR(255),
    IN p_supplier_ids VARCHAR(255),
    IN p_start_created_at DATE,
    IN p_end_created_at DATE,
    IN p_start_expected_at DATE,
    IN p_end_expected_at DATE,
    IN p_product_ids VARCHAR(255),
    IN p_user_created_ids VARCHAR(255),
    IN p_user_completed_ids VARCHAR(255),
    IN p_user_cancelled_ids VARCHAR(255),
    IN p_tenant_id VARCHAR(255),
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE sql_query TEXT;
    DECLARE sql_pagination TEXT;
    DECLARE offset INT DEFAULT (p_page - 1) * p_size;

    -- Các cờ để kiểm tra xem trường nào cần đưa vào trong truy vấn
    DECLARE include_grn_status BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_receive_status BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_payment_status BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_return_status BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_refund_status BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_received_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_expected_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_cancelled_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_completed_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_payment_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_total_received_quantity BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_total_value BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_supplier_name BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_supplier_sub_id BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_supplier_phone BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_supplier_email BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_user_created_name BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_user_completed_name BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_user_cancelled_name BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_note BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_tags BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_created_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_updated_at BOOLEAN DEFAULT FALSE;
    DECLARE include_grn_order_sub_id BOOLEAN DEFAULT FALSE;

    -- Kiểm tra các điều kiện lọc từ JSON
    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_status')) = 'true' THEN
        SET include_grn_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_receive_status')) = 'true' THEN
        SET include_grn_receive_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_payment_status')) = 'true' THEN
        SET include_grn_payment_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_return_status')) = 'true' THEN
        SET include_grn_return_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_refund_status')) = 'true' THEN
        SET include_grn_refund_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_received_at')) = 'true' THEN
        SET include_grn_received_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_expected_at')) = 'true' THEN
        SET include_grn_expected_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_cancelled_at')) = 'true' THEN
        SET include_grn_cancelled_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_payment_at')) = 'true' THEN
        SET include_grn_payment_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_total_received_quantity')) = 'true' THEN
        SET include_grn_total_received_quantity = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_total_value')) = 'true' THEN
        SET include_grn_total_value = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_supplier_name')) = 'true' THEN
        SET include_grn_supplier_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_supplier_sub_id')) = 'true' THEN
        SET include_grn_supplier_sub_id = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_supplier_phone')) = 'true' THEN
        SET include_grn_supplier_phone = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_supplier_email')) = 'true' THEN
        SET include_grn_supplier_email = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_user_created_name')) = 'true' THEN
        SET include_grn_user_created_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_user_completed_name')) = 'true' THEN
        SET include_grn_user_completed_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_user_cancelled_name')) = 'true' THEN
        SET include_grn_user_cancelled_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_note')) = 'true' THEN
        SET include_grn_note = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_tags')) = 'true' THEN
        SET include_grn_tags = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_created_at')) = 'true' THEN
        SET include_grn_created_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_updated_at')) = 'true' THEN
        SET include_grn_updated_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.grn_order_sub_id')) = 'true' THEN
        SET include_grn_order_sub_id = TRUE;
END IF;

    -- Khởi tạo truy vấn SELECT
    SET @sql_query = 'SELECT g.id AS grn_id, g.sub_id AS grn_sub_id';

    -- Xây dựng phần SELECT dựa trên cờ
    IF include_grn_status THEN
        SET @sql_query = CONCAT(@sql_query, ', g.status AS grn_status');
END IF;

    IF include_grn_receive_status THEN
        SET @sql_query = CONCAT(@sql_query, ', g.received_status AS grn_receive_status');
END IF;

    IF include_grn_payment_status THEN
        SET @sql_query = CONCAT(@sql_query, ', g.payment_status AS grn_payment_status');
END IF;

    IF include_grn_return_status THEN
        SET @sql_query = CONCAT(@sql_query, ', g.return_status AS grn_return_status');
END IF;

    IF include_grn_refund_status THEN
        SET @sql_query = CONCAT(@sql_query, ', g.refund_status AS grn_refund_status');
END IF;

    IF include_grn_received_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.received_at AS grn_received_at');
END IF;

    IF include_grn_expected_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.expected_delivery_at AS grn_expected_at');
END IF;

    IF include_grn_cancelled_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.cancelled_at AS grn_cancelled_at');
END IF;

    IF include_grn_payment_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.payment_at AS grn_payment_at');
END IF;

    IF include_grn_total_received_quantity THEN
        SET @sql_query = CONCAT(@sql_query, ', g.total_received_quantity AS grn_total_received_quantity');
END IF;

    IF include_grn_total_value THEN
        SET @sql_query = CONCAT(@sql_query, ', g.total_value AS grn_total_value');
END IF;

    IF include_grn_supplier_name THEN
        SET @sql_query = CONCAT(@sql_query, ', s.name AS grn_supplier_name');
END IF;

    IF include_grn_supplier_sub_id THEN
        SET @sql_query = CONCAT(@sql_query, ', s.sub_id AS grn_supplier_sub_id');
END IF;

    IF include_grn_supplier_phone THEN
        SET @sql_query = CONCAT(@sql_query, ', s.phone AS grn_supplier_phone');
END IF;

    IF include_grn_supplier_email THEN
        SET @sql_query = CONCAT(@sql_query, ', s.email AS grn_supplier_email');
END IF;

    IF include_grn_user_created_name THEN
        SET @sql_query = CONCAT(@sql_query, ', u_created.full_name AS grn_user_created_name');
END IF;

    IF include_grn_user_completed_name THEN
        SET @sql_query = CONCAT(@sql_query, ', u_completed.full_name AS grn_user_completed_name');
END IF;

    IF include_grn_user_cancelled_name THEN
        SET @sql_query = CONCAT(@sql_query, ', u_cancelled.full_name AS grn_user_cancelled_name');
END IF;

    IF include_grn_note THEN
        SET @sql_query = CONCAT(@sql_query, ', g.note AS grn_note');
END IF;

    IF include_grn_tags THEN
        SET @sql_query = CONCAT(@sql_query, ', g.tags AS grn_tags');
END IF;

    IF include_grn_created_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.created_at AS grn_created_at');
END IF;

    IF include_grn_updated_at THEN
        SET @sql_query = CONCAT(@sql_query, ', g.updated_at AS grn_updated_at');
END IF;

    IF include_grn_order_sub_id THEN
        SET @sql_query = CONCAT(@sql_query, ', o.sub_id AS grn_order_sub_id');
END IF;

    -- Thêm phần FROM và JOIN
    SET @sql_query = CONCAT(@sql_query, ' FROM grns g LEFT JOIN grn_products gp ON g.id = gp.grn_id');

    IF include_grn_supplier_name OR include_grn_supplier_sub_id OR include_grn_supplier_phone OR include_grn_supplier_email THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN suppliers s ON g.supplier_id = s.id');
END IF;

    IF include_grn_user_created_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users u_created ON g.user_created_id = u_created.id');
END IF;

    IF include_grn_user_cancelled_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users u_cancelled ON g.user_cancelled_id = u_cancelled.id');
END IF;

    IF include_grn_user_completed_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users u_completed ON g.user_completed_id = u_completed.id');
END IF;

    IF include_grn_order_sub_id THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN orders o ON g.order_id = o.id');
END IF;

    SET @sql_query = CONCAT(@sql_query, ' WHERE 1=1 AND g.tenant_id = "', p_tenant_id, '"');

    -- Thêm điều kiện tìm kiếm và lọc theo keyword
    IF p_keyword IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND (g.sub_id LIKE '%', p_keyword, '%')');
END IF;

    -- Lọc theo trạng thái, nhà cung cấp, người dùng
    IF p_statuses IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.status, "', p_statuses, '")');
END IF;

    IF p_received_statuses IS NOT NULL THEN
		SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.received_status, "', p_received_statuses, '")');
END IF;

    IF p_supplier_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.supplier_id, "', p_supplier_ids, '")');
END IF;

    IF p_start_created_at IS NOT NULL AND p_end_created_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.created_at BETWEEN TIMESTAMP("', p_start_created_at, '", "00:00:00") AND TIMESTAMP("', p_end_created_at, '", "23:59:59")');
END IF;

    IF p_start_created_at IS NOT NULL AND p_end_created_at IS NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.created_at >= TIMESTAMP("', p_start_created_at, '", "00:00:00")');
END IF;

    IF p_start_created_at IS NULL AND p_end_created_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.created_at <= TIMESTAMP("', p_end_created_at, '", "23:59:59")');
END IF;

    IF p_start_expected_at IS NOT NULL AND p_end_expected_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.expected_at BETWEEN TIMESTAMP("', p_start_expected_at, '", "00:00:00") AND TIMESTAMP("', p_end_expected_at, '", "23:59:59")');
END IF;

    IF p_start_expected_at IS NOT NULL AND p_end_expected_at IS NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.expected_at >= TIMESTAMP("', p_start_expected_at, '", "00:00:00")');
END IF;

    IF p_start_expected_at IS NULL AND p_end_expected_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND g.expected_at <= TIMESTAMP("', p_end_expected_at, '", "23:59:59")');
END IF;

	IF p_product_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(gp.product_id, "', p_product_ids, '")');
END IF;

    IF p_user_created_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.user_created_id, "', p_user_created_ids, '")');
END IF;

    IF p_user_completed_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.user_completed_id, "', p_user_completed_ids, '")');
END IF;

    IF p_user_cancelled_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(g.user_cancelled_id, "', p_user_cancelled_ids, '")');
END IF;

    -- Thêm phân trang
    SET @sql_query = CONCAT(@sql_query, ' GROUP BY g.id');
    SET sql_pagination = CONCAT(' LIMIT ', p_size, ' OFFSET ', offset);
    SET @sql_query = CONCAT(@sql_query, sql_pagination);

    -- Chuẩn bị và thực thi truy vấn
PREPARE stmt FROM @sql_query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$

DELIMITER ;