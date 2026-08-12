import axiosInstance from "./axiosInstance";


export const searchFlights = (data) => {

    return axiosInstance.get(

        "/public/flights/search",

        {
            params:{
                source: data.source,
                destination: data.destination,
                date: data.departureDate
            }
        }

    );

};



export const getFlightById = (id) => {

    return axiosInstance.get(

        `/public/flights/${id}`

    );

};