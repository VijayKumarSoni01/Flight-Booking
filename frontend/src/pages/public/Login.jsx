import React, { useState } from "react";

import { useNavigate, useLocation } from "react-router-dom";

import { loginUser } from "../../api/authApi";

import { useAuth } from "../../auth/AuthContext";

import "../../styles/auth.css";

function Login() {
  const navigate = useNavigate();

  const location = useLocation();

  const { login } = useAuth();

  const [formData, setFormData] = useState({
    identifier: "",

    password: "",
  });

  const [message, setMessage] = useState("");

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,

      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setLoading(true);

    setMessage("");

    try {
      const response = await loginUser(formData);

      console.log("Login Response:", response.data);

      const accessToken = response.data.data.accessToken;

      const refreshToken = response.data.data.refreshToken;

      if (!accessToken) {
        throw new Error("Access token not received");
      }

      // Save token

      login(accessToken, refreshToken);

      setMessage("Login successful");

      setTimeout(() => {
        const redirect = location.state?.redirect || "/";

        const bookingData = location.state?.bookingData;

        navigate(
          redirect,

          {
            state: bookingData,
          },
        );
      }, 500);
    } catch (error) {
      console.error("Login Error:", error);

      setMessage(
        error.response?.data?.message || error.message || "Invalid credentials",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-image">
        <div className="auth-overlay">
          <h1>✈ FlightBooking</h1>

          <p>Explore the world with comfortable and affordable flights.</p>
        </div>
      </div>

      <div className="auth-container">
        <div className="auth-card">
          <h2>Welcome Back</h2>

          <p className="auth-subtitle">Login to continue your journey</p>

          {message && <div className="alert alert-info">{message}</div>}

          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label>Email or Phone Number</label>

              <input
                type="text"
                name="identifier"
                placeholder="Enter email or phone number"
                value={formData.identifier}
                onChange={handleChange}
                required
              />
            </div>

            <div className="input-group">
              <label>Password</label>

              <input
                type="password"
                name="password"
                placeholder="Enter password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            <div className="forgot-password">Forgot Password?</div>

            <button type="submit" className="auth-btn" disabled={loading}>
              {loading ? "Logging in..." : "Sign In"}
            </button>
          </form>

          <p className="switch-text">
            Don't have an account?
            <span onClick={() => navigate("/register")}>Create Account</span>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
