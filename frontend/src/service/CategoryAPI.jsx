import axiosInstance from "./axiosInstance";

export const getListCategory = async (page, size, dataFilter) => {
    try {
        const response = await axiosInstance.post(`categories/all.json?page=${page}&size=${size}`, 
            dataFilter
        );
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const createCategory = async (dataBody) => {
    try {
        const response = await axiosInstance.post(`categories/create.json`, 
            dataBody
        );
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}
