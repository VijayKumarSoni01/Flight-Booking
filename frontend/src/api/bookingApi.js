import axiosInstance from "./axiosInstance";


// Create Booking
export const createBooking = (data) => {

    return axiosInstance.post(
        "/private/user/bookings",
        data
    );

};



// Confirm Booking After Payment
export const confirmBooking = (bookingId) => {

    return axiosInstance.post(
        `/internal/bookings/${bookingId}/confirm`
    );

};