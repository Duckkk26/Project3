package com.sapo.mock_project.inventory_receipt.services.user;

import com.sapo.mock_project.inventory_receipt.dtos.request.user.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface UserService {
    ResponseEntity<ResponseObject<Object>> createAccount(RegisterAccountRequest request);

    ResponseEntity<ResponseObject<Object>> loginAccount(LoginAccountRequest request);

    ResponseEntity<ResponseObject<Object>> getUserDetailByToken();

    ResponseEntity<ResponseObject<Object>> refreshToken(RefreshTokenRequest request);

    ResponseEntity<ResponseObject<Object>> changePasswordAccount(ChangePasswordRequest request);

    ResponseEntity<ResponseObject<Object>> adminCreateAccount(AdminCreateStaffRequest request);

    ResponseEntity<ResponseObject<Object>> getListAccount(GetListAccountRequest request, int page, int size);

    ResponseEntity<ResponseObject<Object>> adminUpdateAccount(String accountId, AdminUpdateAccountRequest request);

    ResponseEntity<ResponseObject<Object>> deleteAccount(String accountId);

    ResponseEntity<ResponseObject<Object>> getListName(int page, int size);

    ModelAndView confirmEmail(String userId, String verifyCode);

    ResponseEntity<ResponseObject<Object>> getDashboard();
}
