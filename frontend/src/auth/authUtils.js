export const getAccessToken = () => {

    return localStorage.getItem(
        "accessToken"
    );

};



export const getRefreshToken = () => {

    return localStorage.getItem(
        "refreshToken"
    );

};



export const isAuthenticated = () => {

    return !!getAccessToken();

};



export const logout = () => {

    localStorage.removeItem(
        "accessToken"
    );


    localStorage.removeItem(
        "refreshToken"
    );

};