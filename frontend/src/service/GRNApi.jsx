import axiosInstance from "./axiosInstance";
export const createNewGRN = async (data) => {
    try {
        const response = await axiosInstance.post(`/grns/create.json`, data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}


export const getGRNs = async (page, size, filterName, filterJson, data) => {
    try {

        const response = await axiosInstance.post(`/grns/filter.json?page=${page}&size=${size}`, data, {
            headers: {
                [filterName]: filterJson, // Thiết lập giá trị của filterName vào header
            }
        });
        return response.data;
    } catch (error) {
        console.error(error.message);
        throw error;
    }
}

export const getAllByOrder = async (orderId, page, size) => {
    try {
        const response = await axiosInstance.get(`/grns/order-all.json/${orderId}?page=${page}&size=${size}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getGRNById = async (grnId) => {
    try {
        const response = await axiosInstance.get(`/grns/detail.json/${grnId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const getDataExport = async (mode, dataBody) => {
    try {
        const response = await axiosInstance.post(`/grns/export-data.json?mode=${mode}`, dataBody);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const deleteGRN = async (grnId) => {
    try {
        const response = await axiosInstance.delete(`/grns/delete.json/${grnId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const importGRN = async (id) => {
    try {
        const response = await axiosInstance.put(`/grns/import.json/${id}`, );
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}