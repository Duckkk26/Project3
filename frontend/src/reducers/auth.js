import {
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT_SUCCESS
} from '../actions/auth.js'

const isAuthenticated = JSON.parse(localStorage.getItem('isAuthenticated')); // Đảm bảo đọc đúng kiểu dữ liệu

const authReducer = (state = {
    isAuthenticated: isAuthenticated,
    errorMessage: ''
}, action) => {
    switch (action.type) {
        case LOGIN_SUCCESS:
            localStorage.setItem('isAuthenticated', true); // Cập nhật localStorage sau khi đăng nhập
            return {
                ...state,
                isAuthenticated: true
            };
        case LOGIN_FAIL:
            localStorage.setItem('isAuthenticated', false); // Cập nhật lại nếu thất bại
            return {
                ...state,
                isAuthenticated: false,
                errorMessage: action.payload
            };
        case LOGOUT_SUCCESS:
            localStorage.removeItem('isAuthenticated'); // Xóa khi đăng xuất
            return {
                ...state,
                isAuthenticated: false
            };
        default:
            return state;
    }
}


export default authReducer;