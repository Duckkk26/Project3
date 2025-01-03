package com.sapo.mock_project.inventory_receipt.services.user;

import com.sapo.mock_project.inventory_receipt.components.AuthHelper;
import com.sapo.mock_project.inventory_receipt.components.JwtTokenUtil;
import com.sapo.mock_project.inventory_receipt.components.LocalizationUtils;
import com.sapo.mock_project.inventory_receipt.constants.MessageExceptionKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageKeys;
import com.sapo.mock_project.inventory_receipt.constants.MessageValidateKeys;
import com.sapo.mock_project.inventory_receipt.dtos.request.user.*;
import com.sapo.mock_project.inventory_receipt.dtos.response.Pagination;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseObject;
import com.sapo.mock_project.inventory_receipt.dtos.response.ResponseUtil;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.GetListAccountResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.LoginResponse;
import com.sapo.mock_project.inventory_receipt.dtos.response.user.UserDetailResponse;
import com.sapo.mock_project.inventory_receipt.entities.User;
import com.sapo.mock_project.inventory_receipt.exceptions.DataNotFoundException;
import com.sapo.mock_project.inventory_receipt.exceptions.ExpiredTokenException;
import com.sapo.mock_project.inventory_receipt.mappers.UserMapper;
import com.sapo.mock_project.inventory_receipt.repositories.user.UserRepository;
import com.sapo.mock_project.inventory_receipt.services.MailService;
import com.sapo.mock_project.inventory_receipt.services.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Service xử lý các chức năng liên quan đến người dùng như đăng ký, đăng nhập, làm mới token và thay đổi mật khẩu.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;
    private final AuthHelper authHelper;

    private final MailService mailService;

    /**
     * Tạo tài khoản mới cho người dùng.
     *
     * @param request đối tượng chứa thông tin đăng ký tài khoản.
     * @return ResponseEntity chứa kết quả thực hiện thao tác.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> createAccount(RegisterAccountRequest request) {
        try {
            if (userRepository.existsByPhone(request.getPhone())) {
                // Nếu số điện thoại đã tồn tại, trả về lỗi xác thực
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.USER_PHONE_EXISTED));
            }

            User newAccount = userMapper.mapToEntity(request);
            newAccount.setPassword(passwordEncoder.encode(request.getPassword()));
            newAccount.setActive(false);
            newAccount.setLastChangePass(new Date(System.currentTimeMillis()));
            newAccount.setTenantId(UUID.randomUUID().toString().substring(0, 8));

            userRepository.save(newAccount);

            if (newAccount.getId() != null) {
                mailService.sendConfirmLink(newAccount.getEmail(), newAccount.getId(), UUID.randomUUID().toString());
            }

            // Trả về phản hồi thành công với mã trạng thái 201
            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_CREATE_ADMIN_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Đăng nhập người dùng và trả về token.
     *
     * @param request đối tượng chứa thông tin đăng nhập của người dùng.
     * @return ResponseEntity chứa thông tin đăng nhập và token.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> loginAccount(LoginAccountRequest request) {
        try {
            Optional<User> existingUserOptional = userRepository.findByPhone(request.getPhone());
            if (existingUserOptional.isEmpty()) {
                throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND_BY_PHONE));
            }

            User existingUser = existingUserOptional.get();
            if (!existingUser.isActive()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_EMAIL_NOT_CONFIRMED));
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword());
            authenticationManager.authenticate(authenticationToken);

            String token = jwtTokenUtil.generateToken(existingUser);
            String refreshToken = jwtTokenUtil.generateRefreshToken(existingUser);

            LoginResponse loginResponse = LoginResponse.builder()
                    .userId(existingUser.getId())
                    .fullName(existingUser.getFullName())
                    .phone(existingUser.getPhone())
                    .token(token)
                    .refreshToken(refreshToken)
                    .tokenType(jwtTokenUtil.getTokenType())
                    .role(existingUser.getRole())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_SUCCESSFULLY), loginResponse);
        } catch (BadCredentialsException e) {
            // Xử lý lỗi đăng nhập, ví dụ như mật khẩu sai
            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_WRONG_PHONE_OR_PASSWORD));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Lấy thông tin chi tiết người dùng từ token hiện tại.
     *
     * @return ResponseEntity chứa thông tin chi tiết người dùng.
     */
    @Override
    public ResponseEntity<ResponseObject<Object>> getUserDetailByToken() {
        User user = authHelper.getUser();

        UserDetailResponse userDetail = userMapper.mapToResponse(user);

        return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_DETAIL_SUCCESSFULLY), userDetail);
    }

    /**
     * Làm mới token cho người dùng.
     *
     * @param request đối tượng chứa refresh token của người dùng.
     * @return ResponseEntity chứa token mới hoặc thông báo lỗi.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            final String id = jwtTokenUtil.extractId(refreshToken);
            if (id != null) {
                Optional<User> userDetails = userRepository.findByIdAndTenantId(id, authHelper.getUser().getTenantId());
                if (userDetails.isEmpty() || !jwtTokenUtil.validateToken(refreshToken, userDetails.get())) {
                    throw new ExpiredTokenException(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_TOKEN_EXPIRED));
                }

                String token = jwtTokenUtil.generateToken(userDetails.get());
                String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails.get());

                LoginResponse loginResponse = LoginResponse.builder()
                        .userId(userDetails.get().getId())
                        .fullName(userDetails.get().getFullName())
                        .phone(userDetails.get().getPhone())
                        .token(token)
                        .refreshToken(newRefreshToken)
                        .tokenType(jwtTokenUtil.getTokenType())
                        .role(userDetails.get().getRole())
                        .build();

                return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_REFRESH_TOKEN_SUCCESSFULLY), loginResponse);
            }

            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_TOKEN_INVALID));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    /**
     * Thay đổi mật khẩu của người dùng hiện tại.
     *
     * @param request đối tượng chứa thông tin yêu cầu thay đổi mật khẩu.
     * @return ResponseEntity chứa thông báo kết quả của việc thay đổi mật khẩu.
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> changePasswordAccount(ChangePasswordRequest request) {
        try {
            User existingUser = authHelper.getUser();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(existingUser.getUsername(),
                    request.getOldPassword());
            authenticationManager.authenticate(authenticationToken);

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                // Kiểm tra mật khẩu mới và xác nhận mật khẩu không khớp
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.USER_PASSWORD_NOT_MATCH));
            }

            existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            existingUser.setLastChangePass(new Date(System.currentTimeMillis()));

            userRepository.save(existingUser);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_CHANGE_PASSWORD_SUCCESSFULLY));
        } catch (BadCredentialsException e) {
            // Xử lý lỗi khi mật khẩu cũ không đúng
            return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_OLD_PASSWORD_INCORRECT));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> adminCreateAccount(AdminCreateStaffRequest request) {
        try {
            if (userRepository.existsByPhoneAndTenantId(request.getPhone(), authHelper.getUser().getTenantId())) {
                return ResponseUtil.errorValidationResponse(localizationUtils.getLocalizedMessage(MessageValidateKeys.USER_PHONE_EXISTED));
            }

            User newAccount = User.builder()
                    .fullName(request.getFullName())
                    .phone(request.getPhone())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .isActive(true)
                    .build();

            userRepository.save(newAccount);

            return ResponseUtil.success201Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_CREATE_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getListAccount(GetListAccountRequest request, int page, int size) {
        try {
            UserSpecification specification = new UserSpecification(request, authHelper.getUser().getTenantId());
            Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "fullName");

            Page<User> userPage = userRepository.findAll(specification, pageable);

            List<GetListAccountResponse> userList = userPage.getContent().stream()
                    .map(userMapper::mapToGetListResponse)
                    .toList();

            Pagination pagination = Pagination.builder()
                    .data(userList)
                    .totalPage(userPage.getTotalPages())
                    .totalItems(userPage.getTotalElements())
                    .build();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_ALL_SUCCESSFULLY), pagination);
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> adminUpdateAccount(String accountId, AdminUpdateAccountRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByIdAndTenantId(accountId, authHelper.getUser().getTenantId());
            if (userOptional.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND));
            }

            User user = userOptional.get();
            user.setRole(request.getRole());
            user.setActive(request.isActive());

            userRepository.save(user);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_UPDATE_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<ResponseObject<Object>> deleteAccount(String accountId) {
        try {
            Optional<User> userOptional = userRepository.findByIdAndTenantId(accountId, authHelper.getUser().getTenantId());
            if (userOptional.isEmpty()) {
                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND));
            }

            User account = userOptional.get();
            account.setDeleted(true);

            userRepository.save(account);

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_DELETE_SUCCESSFULLY));
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getListName(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, "fullName");
            Page<Object[]> userPage = userRepository.findAllFullNameAndTenantId(authHelper.getUser().getTenantId(), pageable);

            List<Map<String, String>> nameList = userPage.getContent().stream()
                    .map(objects -> {
                        Map<String, String> nameMap = Map.of(
                                "id", (String) objects[0],
                                "full_name", (String) objects[1]
                        );

                        return nameMap;
                    })
                    .toList();

            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.USER_GET_ALL_SUCCESSFULLY), nameList);
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ModelAndView confirmEmail(String userId, String verifyCode) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
//            if (userOptional.isEmpty()) {
//                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_NOT_FOUND));
//            }

            User user = userOptional.get();
//            if (user.isActive()) {
//                return ResponseUtil.error400Response(localizationUtils.getLocalizedMessage(MessageExceptionKeys.USER_EMAIL_ALREADY_CONFIRMED));
//            }

            user.setActive(true);

            // Trả về ModelAndView với file HTML
            ModelAndView modelAndView = new ModelAndView("confirmation-success");
            modelAndView.addObject("message", localizationUtils.getLocalizedMessage(MessageKeys.USER_CONFIRM_EMAIL_SUCCESSFULLY));
            return modelAndView;

        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
//            return ResponseUtil.error500Response(e.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<ResponseObject<Object>> getDashboard() {
        try {
            List<Object[]> dashboardData = userRepository.getDashboardData(authHelper.getUser().getTenantId());

            Map<String, Object> dashboardMap = Map.of(
                    "total_income", dashboardData.get(0)[0],
                    "total_expense", dashboardData.get(0)[1],
                    "count_order", dashboardData.get(0)[2],
                    "count_grn", dashboardData.get(0)[3],
                    "count_gin", dashboardData.get(0)[4],
                    "count_product", dashboardData.get(0)[5],
                    "sum_quantity", dashboardData.get(0)[6]
            );

            // Xử lý logic lấy dữ liệu dashboard
            return ResponseUtil.success200Response(localizationUtils.getLocalizedMessage(MessageKeys.GET_DASHBOARD_SUCCESSFULLY), dashboardMap);
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseUtil.error500Response(e.getMessage());
        }
    }
}
