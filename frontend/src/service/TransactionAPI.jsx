import axiosInstance from "./axiosInstance";

export const getListTransaction = async (page, size, sort, sortField, filterName, filterJson, dataFilter) => {
    try {
        const response = await axiosInstance.post(`transactions/filter.json?page=${page}&size=${size}&sort=${sort}&sort_field=${sortField}`,
            dataFilter,
            {
                headers: {
                    [filterName]: filterJson
                }
            }
        );
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const createTransaction = async (transaction) => {
    try {
        const response = await axiosInstance.post(`transactions/create.json`, transaction);
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const getTotalReport = async (page, size, dataFilter) => {
    try {
        const response = await axiosInstance.post(`transactions/total.json?page=${page}&size=${size}`, dataFilter);
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const paymentGRN = async (dataBody) => {
    try {
        const response = await axiosInstance.post(`transactions/payment-grn.json`, dataBody);
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}