import axiosInstance from "./axiosInstance";

export const getProductList = async (page, size, filterName, filterJson, bodyJson ) => {
    try {
        const response = await axiosInstance.post(`products/filter.json?page=${page}&size=${size}`,
            // Payload của yêu cầu (body)
            bodyJson
        , {
            headers: {
                [filterName]: filterJson // Thiết lập giá trị của filterName vào header
            }
        });
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const quickGetProductList = async (page, size, keyword) => {
    try {
        const response = await axiosInstance.get(`products/quick-get-list.json?page=${page}&size=${size}&keyword=${keyword}`);
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const quickCreateProduct = async (dataBody) => {
    try {
        const response = await axiosInstance.post(`products/quick-create.json`, 
            dataBody
        );
        return response.data;
    } catch (err) {
        console.log(err);
        throw err;
    }
}

export const getProductById = async (productId) => {
    try {
        const response = await axiosInstance.get(`products/detail.json/${productId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}

export const deleteProductById = async (productId) => {
    try {
        const response = await axiosInstance.delete(`products/delete.json/${productId}`);
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
}