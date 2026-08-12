import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import { registerUser } from "../../api/authApi";

import "../../styles/auth.css";

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: "",
    firstName: "",
    middleName: "",
    lastName: "",
    username: "",
    email: "",
    phoneNumber: "",
    password: "",
    gender: "",
    dateOfBirth: "",
    nationality: "",
    addressLine1: "",
    addressLine2: "",
    city: "",
    state: "",
    country: "",
    pinCode: "",
  });

  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    setFormData({
      ...formData,

      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await registerUser(formData);

      console.log("Register Response:", response.data);

      setMessage("Registration successful. Please verify your email.");
    } catch (error) {
      console.error("Registration Error:", error);

      setMessage(error.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="auth-page">
      {/* LEFT IMAGE */}

      <div className="auth-image">
        <div className="auth-overlay">
          <h1>✈ FlightBooking</h1>

          <p>Create your account and start your journey.</p>
        </div>
      </div>

      {/* REGISTER FORM */}

      <div className="auth-container register-container">
        <div className="auth-card register-card">
          <h2>Create Account</h2>

          <p className="auth-subtitle">Join FlightBooking today</p>

          {message && <div className="alert alert-info">{message}</div>}

          <form onSubmit={handleSubmit}>
            {/* PERSONAL INFORMATION */}

            <h5 className="form-section-title">Personal Information</h5>

            <div className="two-input">
              <input
                className="auth-input"
                name="title"
                placeholder="Title (Mr/Ms)"
                value={formData.title}
                onChange={handleChange}
              />

              <select
                className="auth-input"
                name="gender"
                value={formData.gender}
                onChange={handleChange}
              >
                <option value="">Gender</option>

                <option value="MALE">Male</option>

                <option value="FEMALE">Female</option>

                <option value="OTHER">Other</option>
              </select>
            </div>

            <div className="two-input">
              <input
                className="auth-input"
                name="firstName"
                placeholder="First Name"
                value={formData.firstName}
                onChange={handleChange}
                required
              />

              <input
                className="auth-input"
                name="lastName"
                placeholder="Last Name"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </div>

            <input
              className="auth-input"
              name="middleName"
              placeholder="Middle Name (Optional)"
              value={formData.middleName}
              onChange={handleChange}
            />

            <input
              className="auth-input"
              type="date"
              name="dateOfBirth"
              value={formData.dateOfBirth}
              onChange={handleChange}
            />

            <input
              className="auth-input"
              name="nationality"
              placeholder="Nationality"
              value={formData.nationality}
              onChange={handleChange}
            />

            {/* ACCOUNT */}

            <h5 className="form-section-title">Account Information</h5>

            <input
              className="auth-input"
              name="username"
              placeholder="Username"
              value={formData.username}
              onChange={handleChange}
              required
            />

            <input
              className="auth-input"
              type="email"
              name="email"
              placeholder="Email Address"
              value={formData.email}
              onChange={handleChange}
              required
            />

            <input
              className="auth-input"
              name="phoneNumber"
              placeholder="Phone Number"
              value={formData.phoneNumber}
              onChange={handleChange}
              required
            />

            <input
              className="auth-input"
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
            />

            {/* ADDRESS */}

            <h5 className="form-section-title">Address Information</h5>

            <input
              className="auth-input"
              name="addressLine1"
              placeholder="Address Line 1"
              value={formData.addressLine1}
              onChange={handleChange}
            />

            <input
              className="auth-input"
              name="addressLine2"
              placeholder="Address Line 2"
              value={formData.addressLine2}
              onChange={handleChange}
            />

            <div className="two-input">
              <input
                className="auth-input"
                name="city"
                placeholder="City"
                value={formData.city}
                onChange={handleChange}
              />

              <input
                className="auth-input"
                name="state"
                placeholder="State"
                value={formData.state}
                onChange={handleChange}
              />
            </div>

            <div className="two-input">
              <input
                className="auth-input"
                name="country"
                placeholder="Country"
                value={formData.country}
                onChange={handleChange}
              />

              <input
                className="auth-input"
                name="pinCode"
                placeholder="PIN Code"
                value={formData.pinCode}
                onChange={handleChange}
              />
            </div>

            <button className="auth-btn" type="submit">
              Create Account
            </button>
          </form>

          <p className="switch-text">
            Already have an account?
            <span onClick={() => navigate("/login")}>Sign In</span>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Register;
