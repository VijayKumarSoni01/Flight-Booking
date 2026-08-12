import React from "react";

import { useLocation, useNavigate } from "react-router-dom";

import "../../styles/bookingSuccess.css";

function BookingSuccess() {
  const location = useLocation();

  const navigate = useNavigate();

  const { booking, flight } = location.state || {};

  if (!booking) {
    return (
      <div className="success-page">
        <div className="success-card">
          <h2>Booking information not found</h2>

          <button className="primary-btn" onClick={() => navigate("/")}>
            Go Home
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="success-page">
      <div className="success-card">
        <div className="success-icon">✓</div>

        <h1>Payment Successful</h1>

        <p className="success-message">
          Your flight booking has been confirmed.
        </p>

        <div className="booking-info">
          <div>
            <span>Booking ID</span>

            <strong>{booking.bookingId || "--"}</strong>
          </div>

          <div>
            <span>Booking Reference</span>

            <strong>{booking.bookingReference || "--"}</strong>
          </div>

          <div>
            <span>PNR</span>

            <strong>{booking.pnr || "Generating..."}</strong>
          </div>

          <div>
            <span>Amount Paid</span>

            <strong>
              ₹
              {booking.totalFare
                ? Number(booking.totalFare).toLocaleString("en-IN")
                : "--"}
            </strong>
          </div>

          <div>
            <span>Status</span>

            <strong>{booking.bookingStatus || "--"}</strong>
          </div>
        </div>

        {flight && (
          <div className="flight-confirmation">
            <h3>Flight Details</h3>

            <p>
              ✈ {flight.airlineName}
              {" - "}
              {flight.flightNumber}
            </p>

            <p>
              {flight.sourceAirport || flight.originAirportCode}

              {" → "}

              {flight.destinationAirport || flight.destinationAirportCode}
            </p>

            {flight.departureTime && (
              <p>
                Departure: {new Date(flight.departureTime).toLocaleString()}
              </p>
            )}
          </div>
        )}

        <div className="success-actions">
          <button
            className="primary-btn"
            onClick={() => navigate("/my-bookings")}
          >
            View My Bookings
          </button>

          <button className="secondary-btn" onClick={() => navigate("/")}>
            Book Another Flight
          </button>
        </div>
      </div>
    </div>
  );
}

export default BookingSuccess;
