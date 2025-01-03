DELIMITER $$

CREATE PROCEDURE filter_orders(
    IN p_filter_json JSON,
    IN p_keyword VARCHAR(255),
    IN p_status VARCHAR(255),
    IN p_supplier_ids VARCHAR(255),
    IN p_start_created_at DATE,
    IN p_end_created_at DATE,
    IN p_start_expected_at DATE,
    IN p_end_expected_at DATE,
    IN p_product_ids VARCHAR(255),
    IN p_user_created_ids VARCHAR(255),
    IN p_user_completed_ids VARCHAR(255),
    IN p_user_cancelled_ids VARCHAR(255),
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE sql_query TEXT;
    DECLARE sql_pagination TEXT;
    DECLARE offset INT DEFAULT (p_page - 1) * p_size;

    DECLARE include_order_created_at BOOLEAN DEFAULT FALSE;
    DECLARE include_order_status BOOLEAN DEFAULT FALSE;
    DECLARE include_supplier_name BOOLEAN DEFAULT FALSE;
    DECLARE include_user_created_name BOOLEAN DEFAULT FALSE;
    DECLARE include_order_total_quantity BOOLEAN DEFAULT FALSE;
    DECLARE include_order_total_price BOOLEAN DEFAULT FALSE;
    DECLARE include_supplier_sub_id BOOLEAN DEFAULT FALSE;
    DECLARE include_supplier_sub_phone BOOLEAN DEFAULT FALSE;
    DECLARE include_supplier_sub_email BOOLEAN DEFAULT FALSE;
    DECLARE include_user_cancelled_name BOOLEAN DEFAULT FALSE;
    DECLARE include_user_completed_name BOOLEAN DEFAULT FALSE;
    DECLARE include_user_ended_name BOOLEAN DEFAULT FALSE;
    DECLARE include_order_note BOOLEAN DEFAULT FALSE;
    DECLARE include_order_tags BOOLEAN DEFAULT FALSE;
    DECLARE include_order_expected_at BOOLEAN DEFAULT FALSE;
    DECLARE include_order_updated_at BOOLEAN DEFAULT FALSE;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_created_at')) = 'true' THEN
        SET include_order_created_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_status')) = 'true' THEN
        SET include_order_status = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.supplier_name')) = 'true' THEN
        SET include_supplier_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.user_created_name')) = 'true' THEN
        SET include_user_created_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_total_quantity')) = 'true' THEN
        SET include_order_total_quantity = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_total_price')) = 'true' THEN
        SET include_order_total_price = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.supplier_sub_id')) = 'true' THEN
        SET include_supplier_sub_id = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.supplier_sub_phone')) = 'true' THEN
        SET include_supplier_sub_phone = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.supplier_sub_email')) = 'true' THEN
        SET include_supplier_sub_email = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.user_cancelled_name')) = 'true' THEN
        SET include_user_cancelled_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.user_completed_name')) = 'true' THEN
        SET include_user_completed_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.user_ended_name')) = 'true' THEN
        SET include_user_ended_name = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_note')) = 'true' THEN
        SET include_order_note = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_tags')) = 'true' THEN
        SET include_order_tags = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_expected_at')) = 'true' THEN
        SET include_order_expected_at = TRUE;
END IF;

    IF JSON_UNQUOTE(JSON_EXTRACT(p_filter_json, '$.order_updated_at')) = 'true' THEN
        SET include_order_updated_at = TRUE;
END IF;

    SET @sql_query = 'SELECT o.id AS order_id, o.sub_id AS order_sub_id';

    IF include_order_created_at THEN
        SET @sql_query = CONCAT(@sql_query, ', o.created_at AS order_created_at');
END IF;

    IF include_order_status THEN
        SET @sql_query = CONCAT(@sql_query, ', o.status AS order_status');
END IF;

    IF include_supplier_name THEN
        SET @sql_query = CONCAT(@sql_query, ', s.name AS supplier_name');
END IF;

    IF include_user_created_name THEN
        SET @sql_query = CONCAT(@sql_query, ', uc.full_name AS user_created_name');
END IF;

    IF include_order_total_quantity THEN
        SET @sql_query = CONCAT(@sql_query, ', SUM(od.quantity) AS total_quantity');
END IF;

    IF include_order_total_price THEN
        SET @sql_query = CONCAT(@sql_query, ', o.total_price AS total_price');
END IF;

    IF include_supplier_sub_id THEN
        SET @sql_query = CONCAT(@sql_query, ', s.sub_id AS supplier_sub_id');
END IF;

    IF include_supplier_sub_phone THEN
        SET @sql_query = CONCAT(@sql_query, ', s.phone AS supplier_sub_phone');
END IF;

    IF include_supplier_sub_email THEN
        SET @sql_query = CONCAT(@sql_query, ', s.email AS supplier_sub_email');
END IF;

    IF include_user_cancelled_name THEN
        SET @sql_query = CONCAT(@sql_query, ', ucan.full_name AS user_cancelled_name');
END IF;

    IF include_user_completed_name THEN
        SET @sql_query = CONCAT(@sql_query, ', ucom.full_name AS user_completed_name');
END IF;

    IF include_user_ended_name THEN
        SET @sql_query = CONCAT(@sql_query, ', uend.full_name AS user_ended_name');
END IF;

    IF include_order_note THEN
        SET @sql_query = CONCAT(@sql_query, ', o.note AS order_note');
END IF;

    IF include_order_tags THEN
        SET @sql_query = CONCAT(@sql_query, ', o.tags AS order_tags');
END IF;

    IF include_order_expected_at THEN
        SET @sql_query = CONCAT(@sql_query, ', o.expected_at AS order_expected_at');
END IF;

    IF include_order_updated_at THEN
        SET @sql_query = CONCAT(@sql_query, ', o.updated_at AS order_updated_at');
END IF;

    SET @sql_query = CONCAT(@sql_query, ' FROM orders o LEFT JOIN order_details od ON o.id = od.order_id');

    IF (include_supplier_name OR include_supplier_sub_id OR include_supplier_sub_phone OR include_supplier_sub_email) THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN suppliers s ON o.supplier_id = s.id');
END IF;

    IF include_user_created_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users uc ON o.user_created_id = uc.id');
END IF;

    IF include_user_cancelled_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users ucan ON o.user_cancelled_id = ucan.id');
END IF;

    IF include_user_completed_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users ucom ON o.user_completed_id = ucom.id');
END IF;

    IF include_user_ended_name THEN
		SET @sql_query = CONCAT(@sql_query, ' LEFT JOIN users uend ON o.user_ended_id = uend.id');
END IF;

    SET @sql_query = CONCAT(@sql_query, ' WHERE 1=1');

    IF p_keyword IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND (o.sub_id LIKE ''%', p_keyword , '%'')');
END IF;

    IF p_status IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(o.status, "', p_status, '")');
END IF;

    IF p_supplier_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(o.supplier_id, "', p_supplier_ids, '")');
END IF;

    IF p_start_created_at IS NOT NULL AND p_end_created_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.created_at BETWEEN TIMESTAMP("', p_start_created_at, '", "00:00:00") AND TIMESTAMP("', p_end_created_at, '", "23:59:59")');
END IF;

    IF p_start_created_at IS NOT NULL AND p_end_created_at IS NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.created_at >= TIMESTAMP("', p_start_created_at, '", "00:00:00")');
END IF;

    IF p_start_created_at IS NULL AND p_end_created_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.created_at <= TIMESTAMP("', p_end_created_at, '", "23:59:59")');
END IF;

    IF p_start_expected_at IS NOT NULL AND p_end_expected_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.expected_at BETWEEN TIMESTAMP("', p_start_expected_at, '", "00:00:00") AND TIMESTAMP("', p_end_expected_at, '", "23:59:59")');
END IF;

    IF p_start_expected_at IS NOT NULL AND p_end_expected_at IS NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.expected_at >= TIMESTAMP("', p_start_expected_at, '", "00:00:00")');
END IF;

    IF p_start_expected_at IS NULL AND p_end_expected_at IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND o.expected_at <= TIMESTAMP("', p_end_expected_at, '", "23:59:59")');
END IF;

    IF p_product_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(od.product_id, "', p_product_ids, '")');
END IF;

    IF p_user_created_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(o.user_created_id, "', p_user_created_ids, '")');
END IF;

    IF p_user_completed_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(o.user_completed_id, "', p_user_completed_ids, '")');
END IF;

    IF p_user_cancelled_ids IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, ' AND FIND_IN_SET(o.user_cancelled_id, "', p_user_cancelled_ids, '")');
END IF;

    SET @sql_query = CONCAT(@sql_query, ' GROUP BY o.id');
    SET @sql_pagination = CONCAT(' LIMIT ', p_size, ' OFFSET ', offset);
    SET @sql_query = CONCAT(@sql_query, @sql_pagination);

PREPARE stmt FROM @sql_query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;
