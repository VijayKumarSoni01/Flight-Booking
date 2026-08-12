import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/api",

  headers: {
    "Content-Type": "application/json",
  },
});

// Add JWT token automatically
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");

    console.log("========== AXIOS REQUEST ==========");

    console.log("URL : ", config.baseURL + config.url);

    console.log("TOKEN FROM STORAGE : ", token);

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;

      console.log("AUTH HEADER ADDED : ", config.headers.Authorization);
    } else {
      console.log("NO ACCESS TOKEN FOUND");
    }

    console.log("====================================");

    return config;
  },

  (error) => {
    return Promise.reject(error);
  },
);

// Response interceptor
axiosInstance.interceptors.response.use(
  (response) => {
    console.log("========== API RESPONSE ==========");

    console.log("STATUS : ", response.status);

    console.log(response.data);

    console.log("==================================");

    return response;
  },

  (error) => {
    console.log("========== API ERROR ==========");

    console.log("STATUS : ", error.response?.status);

    console.log("DATA : ", error.response?.data);

    console.log("MESSAGE : ", error.message);

    console.log("===============================");

    return Promise.reject(error);
  },
);

export default axiosInstance;
