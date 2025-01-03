import axiosInstance from "./axiosInstance";
import Cookies from 'js-cookie'
export const createNewGIN = async (data) => {
    try {
        const response = await axiosInstance.post(`/gins/create.json`, data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}


export const getGINs = async (page, size, filterName, filterJson, data) => {
    try {
        const response = await axiosInstance.post(`/gins/filter.json?page=${page}&size=${size}`, data, {
            headers: {
                [filterName]: filterJson, 
            }
        }
        );
        return response.data;
    } catch (error) {
        console.error(error.message);
        throw error;
    }
}

export const getGINDetail = async (id) => {
    try {
        const response = await axiosInstance.get(`/gins/detail.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const updateGIN = async (data, id) => {
    try {
        const response = await axiosInstance.put(`/gins/update.json/${id}`, data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const exportData = async (mode, bodyJson) => {
    try {
        const response = await axiosInstance.post(`/gins/export-data.json?mode=${mode}`, bodyJson);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const deleteGIN = async (id) => {
    try {
        const response = await axiosInstance.delete(`/gins/delete.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const balanceGIN = async (id) => {
    try {
        const response = await axiosInstance.put(`/gins/balance.json/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}