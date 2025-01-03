// src/api/axiosInstance.js
import axios from 'axios';
import API_CONFIG from '../config/ApiConfig';
import { refreshToken as refreshAccessToken } from './UserAPI';
import { logout } from '../actions/auth';
import { toast } from 'react-toastify';
import Notification from '../components/Notification/Notification';

let isRefreshing = false; 
let failedQueue = [];

const processQueue = (error, token = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });
    failedQueue = [];
};

const axiosInstance = axios.create({
    baseURL: API_CONFIG.API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
        'Accept-Language': 'vi',
    },
    withCredentials: true // Thêm thuộc tính này để gửi cookie
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
    }
);

axiosInstance.interceptors.response.use(
    (response) => response, // Nếu phản hồi thành công, trả về phản hồi
    async (error) => {
        const originalRequest = error.config;

        // Nếu lỗi là 401 và chưa thực hiện việc refresh token
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then((token) => {
                    originalRequest.headers.Authorization = token;
                    return axiosInstance(originalRequest);
                }).catch((err) => {
                    return Promise.reject(err);
                });
            }

            originalRequest._retry = true;
            isRefreshing = true;

            try {
                const refreshTokenResponse = await refreshAccessToken(); // Gọi refresh token API

                const { full_name, phone, refresh_token, role, token, token_type, user_id  } = refreshTokenResponse.data;

                // Cập nhật token và refresh token vào localStorage
                localStorage.setItem('token', token);
                localStorage.setItem('refreshToken', refresh_token);
                localStorage.setItem('fullName', full_name);
                localStorage.setItem('phone', phone);
                localStorage.setItem('role', role);
                localStorage.setItem('tokenType', token_type);
                localStorage.setItem('userId', user_id);

                processQueue(null, newToken);

                originalRequest.headers.Authorization = newToken;
                return axiosInstance(originalRequest); // Thực hiện lại request ban đầu
            } catch (refreshError) {
                processQueue(refreshError, null);
                toast(<Notification 
                        type={"error"} 
                        withIcon 
                        message={'Phiên đăng nhập hết hạn, vui lòng đăng nhập lại'} 
                    />,
                    {
                        autoClose: 4000,
                        closeButton: false,
                        hideProgressBar: true,
                    }
                )
                logout();
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }
        
        toast(<Notification 
                type={"error"} 
                withIcon 
                message={error.response.data.message} 
            />,
            {
                autoClose: 4000,
                closeButton: false,
                hideProgressBar: true,
            }
        )
        return Promise.reject(error); // Nếu là lỗi khác, trả về lỗi
    }
);

export default axiosInstance;