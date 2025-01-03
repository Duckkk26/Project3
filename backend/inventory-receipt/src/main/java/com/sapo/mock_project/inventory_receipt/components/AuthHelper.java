package com.sapo.mock_project.inventory_receipt.components;

import com.sapo.mock_project.inventory_receipt.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Lớp AuthHelper cung cấp các phương thức để lấy thông tin người dùng hiện tại từ
 * Spring Security Context.
 *
 * <p>Lớp này được đánh dấu với annotation {@code @Service} để Spring quản lý và có thể
 * dễ dàng sử dụng ở các lớp khác thông qua dependency injection.</p>
 */
@Service
public class AuthHelper {

    /**
     * Phương thức getUser lấy đối tượng User hiện đang đăng nhập từ Spring Security Context.
     *
     * @return đối tượng User hiện tại, được lấy từ authentication principal.
     *
     * @throws ClassCastException nếu principal không thể ép kiểu thành User.
     */
    public User getUser() {
        // Lấy thông tin xác thực từ Security Context của Spring.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Trả về đối tượng User từ authentication principal.
        return (User) authentication.getPrincipal();
    }
}
