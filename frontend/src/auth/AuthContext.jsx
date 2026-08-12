import React, { createContext, useContext, useState } from "react";

export const AuthContext = createContext();

function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  const login = (accessToken, refreshToken) => {
    // Store tokens

    localStorage.setItem("accessToken", accessToken);

    localStorage.setItem("refreshToken", refreshToken);

    setUser({
      accessToken,

      refreshToken,
    });
  };

  const logout = () => {
    localStorage.removeItem("accessToken");

    localStorage.removeItem("refreshToken");

    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,

        login,

        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  return useContext(AuthContext);
};

export default AuthProvider;
