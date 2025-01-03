import axiosInstance from "./axiosInstance";

export const loginAccount = async (phone, password) => {
    try {
        const response = await axiosInstance.post(`/users/login.json`, {
            phone,
            password
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};

export const registerAccount = async (full_name, phone, password, role, email) => {
    try {
        const response = await axiosInstance.post(`/users/register.json`, {
            full_name,
            phone,
            password,
            role,
            email
        });
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getInforDetails = async () => {
    try {
        const response = await axiosInstance.get(`/users/detail.json`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const refreshToken = async () => {
    try {
        const response = await axiosInstance.post(`/users/refresh-token.json`,
            {
                refresh_token: localStorage.getItem('refreshToken')
            }
        );
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getListUser = async (page, limit, dataBody) => {
    try {
        const response = await axiosInstance.post(`/users/list.json?page=${page}&limit=${limit}`, dataBody);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const createNewUser = async (dataBody) => {
    try {
        const response = await axiosInstance.post(`/users/admin-create.json`, dataBody);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const deleteAccount = async (id) => {
    try {
        const response = await axiosInstance.delete(`/users/delete-account.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getDashboard = async () => {
    try {
        const response = await axiosInstance.get(`/users/dashboard.json`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}