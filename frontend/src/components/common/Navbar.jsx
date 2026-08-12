import React, { useContext } from "react";

import { NavLink, Link, useNavigate } from "react-router-dom";

import { AuthContext } from "../../auth/AuthContext";

import "../../styles/navbar.css";

function Navbar() {
  const navigate = useNavigate();

  const { user, logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout();

    navigate("/");
  };

  return (
    <nav className="main-navbar">
      <div className="container navbar-container">
        {/* LOGO */}

        <Link to="/" className="brand">
          ✈ FlightBooking
        </Link>

        {/* MENU */}

        <div className="nav-links">
          <NavLink
            to="/"
            className={({ isActive }) => (isActive ? "active-link" : "")}
          >
            Home
          </NavLink>

          <NavLink
            to="/flights"
            className={({ isActive }) => (isActive ? "active-link" : "")}
          >
            Flights
          </NavLink>

          <NavLink
            to="/offers"
            className={({ isActive }) => (isActive ? "active-link" : "")}
          >
            Offers
          </NavLink>

          {user && (
            <NavLink
              to="/bookings"
              className={({ isActive }) => (isActive ? "active-link" : "")}
            >
              My Bookings
            </NavLink>
          )}
        </div>

        {/* RIGHT SIDE ACTIONS */}

        <div className="nav-actions">
          {user ? (
            <>
              <button
                className="profile-btn"
                onClick={() => navigate("/profile")}
              >
                👤 Profile
              </button>

              <button className="logout-btn" onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <button className="login-btn" onClick={() => navigate("/login")}>
                Sign In
              </button>

              <button
                className="signup-btn"
                onClick={() => navigate("/register")}
              >
                Sign Up
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
