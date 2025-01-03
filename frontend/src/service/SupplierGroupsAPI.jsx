import axiosInstance from "./axiosInstance";

export const getListSupplierGroups = async (page, size, bodyJson) => {
  try {
    const response = await axiosInstance.post(
      `supplier-groups/all.json?page=${page}&size=${size}`,
      bodyJson
    );
    return response.data;
  } catch (err) {
    console.log(err);
    throw err;
  }
};

export const createSupplierGroup = async (dataBody) => {
  try {
    const response = await axiosInstance.post(
      `supplier-groups/create.json`,
      dataBody
    );
    return response.data;
  } catch (err) {
    console.log(err);
    throw err;
  }
};

export const updateSupplierGroup = async (id, data) => {
  try {
    const response = await axiosInstance.put(
      `/supplier-groups/update.json/${id}`,
      data
    );
    return response.data;
  } catch (err) {
    console.log(err);
    throw err;
  }
};
