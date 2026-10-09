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

// Get My Bookings
export const getMyBookings = (page = 0, size = 10) => {
    return axiosInstance.get(
        "/private/user/bookings/my-bookings",
        {
            params: {
                page,
                size
            }
        }
    );
};

// Get Booking Details
export const getBooking = (bookingId) => {
    return axiosInstance.get(
        `/private/user/bookings/${bookingId}`
    );
};