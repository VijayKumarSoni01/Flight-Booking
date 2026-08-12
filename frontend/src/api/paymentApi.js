import axiosInstance from "./axiosInstance";

export const createPaymentOrder = (data) => {
  return axiosInstance.post("/private/payments", data);
};

export const verifyPayment = (data) => {
  return axiosInstance.post("/private/payments/verify", data);
};
