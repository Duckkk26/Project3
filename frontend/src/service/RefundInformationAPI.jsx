import axiosInstance from "./axiosInstance";

export const getListByGRN = async (grnId) => {
    try {
        const response = await axiosInstance.get(`/refund-informations/all.json/${grnId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const createReturn = async (data) => {
    try {
        const response = await axiosInstance.post("/refund-informations/create.json", data);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}