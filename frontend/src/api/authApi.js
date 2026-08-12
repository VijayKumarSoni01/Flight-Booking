import axiosInstance from "./axiosInstance";



export const registerUser = (data) => {

    return axiosInstance.post(
        "/public/register",
        data
    );

};

export const loginUser = (data) => {

    return axiosInstance.post(
        "/public/login",
        data
    );

};





export const verifyEmail = (token) => {


    return axiosInstance.get(

        `/public/verify-email?token=${token}`

    );

};


export const resendVerificationEmail = (email) => {


    return axiosInstance.post(

        "/public/resend-verification-email",

        {
            email
        }

    );

};



export const forgotPassword = (data) => {


    return axiosInstance.post(

        "/public/forgot-password",

        data

    );

};



export const resetPassword = (data) => {


    return axiosInstance.post(

        "/public/reset-password",

        data

    );

};




export const refreshAccessToken = (refreshToken) => {


    return axiosInstance.post(

        "/public/refresh-token",

        {
            refreshToken
        }

    );

};



export const restoreAccount = (token) => {


    return axiosInstance.post(

        `/public/restore-account?token=${token}`

    );

};