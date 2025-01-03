import { loginAccount } from "../service/UserAPI";

export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAIL = 'LOGIN_FAIL';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAIL = 'LOGOUT_FAIL';

export const loginSuccess = () => {
    return {
        type: LOGIN_SUCCESS
    }
};

export const loginFail = (error) => {
    return {
        type: LOGIN_FAIL,
        payload: error
    }
};

export const logoutSuccess = () => {
    return {
        type: LOGOUT_SUCCESS
    }
}

export const logoutFail = (error) => {
    return {
        type: LOGOUT_FAIL,
        payload: error
    }
}

export const login = (phone, password) => {
    return async (dispatch) => {
        const response = await loginAccount(phone, password);
        
        localStorage.setItem('userId', response.data.user_id)
        localStorage.setItem('fullName', response.data.full_name)
        localStorage.setItem('phone', response.data.phone)
        localStorage.setItem('token', response.data.token);
        localStorage.setItem("refreshToken", response.data.refresh_token);
        localStorage.setItem('tokenType', response.data.token_type)
        localStorage.setItem('role', response.data.role)
        dispatch(loginSuccess());
    }
}

export const logout = () => {
    return (dispatch) => {
        localStorage.clear();
        dispatch(logoutSuccess());
    }
}