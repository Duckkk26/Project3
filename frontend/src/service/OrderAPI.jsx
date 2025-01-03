import axiosInstance from "./axiosInstance";

export const createNewOrder = async (data) => {
    try {
        const response = await axiosInstance.post(`/orders/create.json`, data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getAllOrders = async (page, size, filterName, filterJson, body) => {
    try {
        const response = await axiosInstance.post(`/orders/filter.json?page=${page}&size=${size}`, body,
            {
                headers: {
                    [filterName]: filterJson // Thiết lập giá trị của filterName vào header
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getOrderById = async (id) => {
    try {
        const response = await axiosInstance.get(`/orders/detail.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getDataExport = async (mode, dataBody) => {
    try {
        const response = await axiosInstance.post(`/orders/export-data.json?mode=${mode}`, dataBody);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const putUpdateOrder = async (id, data) => {
    try {
        const response = await axiosInstance.put(`/orders/update.json/${id}`, data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const cancelOrder = async (id) => {
    try {
        const response = await axiosInstance.put(`/orders/cancel.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}