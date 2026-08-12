import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import { getFlightById } from "../../api/flightApi";

import { isLoggedIn } from "../../utils/auth";

import "../../styles/flightDetails.css";

function formatTime(value) {
  if (!value) return "--:--";

  return new Date(value).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
}

function formatDate(value) {
  if (!value) return "";

  return new Date(value).toLocaleDateString("en-IN", {
    day: "numeric",
    month: "short",
    year: "numeric",
  });
}

function formatDuration(minutes) {
  if (!minutes) return "--";

  const hours = Math.floor(minutes / 60);

  const mins = minutes % 60;

  return `${hours}h ${mins}m`;
}

function FlightDetails() {
  const { id } = useParams();

  const navigate = useNavigate();

  const [flight, setFlight] = useState(null);

  const [loading, setLoading] = useState(true);

  const [selectedFare, setSelectedFare] = useState(null);

  useEffect(() => {
    loadFlight();
  }, [id]);

  const loadFlight = async () => {
    try {
      setLoading(true);

      const response = await getFlightById(id);

      console.log("FLIGHT RESPONSE:", response.data);

      setFlight(response.data.data || response.data);
    } catch (error) {
      console.error("Flight loading error", error);
    } finally {
      setLoading(false);
    }
  };

  const continueBooking = () => {
    if (!selectedFare) {
      alert("Please select fare");

      return;
    }

    const bookingData = {
      flightId: flight.id,

      flight: flight,

      // Contains:
      // id
      // cabinClass
      // price

      fare: selectedFare,
    };

    console.log("BOOKING DATA:", bookingData);

    if (!isLoggedIn()) {
      navigate("/login", {
        state: {
          redirect: "/booking",

          bookingData,
        },
      });

      return;
    }

    navigate("/booking", {
      state: bookingData,
    });
  };

  if (loading) {
    return <div className="details-loading">Loading flight details...</div>;
  }

  if (!flight) {
    return <div className="empty-box">Flight not found</div>;
  }

  /*
      UI DISPLAY NAME
      =================

      name:
      Premium Economy


      BACKEND ENUM
      =============

      cabinClass:
      PREMIUM_ECONOMY

  */

  const fares = [
    {
      id: 1,

      name: "Economy",

      cabinClass: "ECONOMY",

      price: flight.economyPrice,

      icon: "💺",
    },

    {
      id: 2,

      name: "Premium Economy",

      cabinClass: "PREMIUM_ECONOMY",

      price: flight.premiumEconomyPrice,

      icon: "⭐",
    },

    {
      id: 3,

      name: "Business",

      cabinClass: "BUSINESS",

      price: flight.businessPrice,

      icon: "🛋️",
    },

    {
      id: 4,

      name: "First Class",

      cabinClass: "FIRST",

      price: flight.firstPrice,

      icon: "👑",
    },
  ].filter((fare) => fare.price !== null && fare.price !== undefined);

  return (
    <div className="flight-details-page">
      {/* HEADER */}

      <div className="details-card flight-header-card">
        <div className="airline-header">
          <div className="airline-brand">
            <div className="big-logo">✈</div>

            <div>
              <h2>{flight.airlineName}</h2>

              <p>
                Flight No:
                <b>{flight.flightNumber}</b>
              </p>
            </div>
          </div>

          <span className="status-badge">Available</span>
        </div>

        <div className="journey">
          <div className="airport">
            <h1>{flight.originAirportCode}</h1>

            <p>{formatTime(flight.departureTime)}</p>

            <small>Departure</small>
          </div>

          <div className="journey-line">
            <span>✈</span>

            <p>{formatDuration(flight.durationMinutes)}</p>
          </div>

          <div className="airport">
            <h1>{flight.destinationAirportCode}</h1>

            <p>{formatTime(flight.arrivalTime)}</p>

            <small>Arrival</small>
          </div>
        </div>

        <div className="flight-info">
          <div>
            📅
            {formatDate(flight.departureTime)}
          </div>

          <div>⏱{formatDuration(flight.durationMinutes)}</div>

          <div>
            🛫
            {flight.departureTerminal || "Terminal"}
          </div>
        </div>
      </div>

      {/* FARES */}

      <div className="details-card">
        <h2>Choose Your Fare</h2>

        <div className="fare-grid">
          {fares.map((fare) => (
            <div
              key={fare.id}
              className={`fare-card ${
                selectedFare?.id === fare.id ? "selected" : ""
              }`}
              onClick={() => setSelectedFare(fare)}
            >
              <div className="fare-icon">{fare.icon}</div>

              <h3>{fare.name}</h3>

              <h2>₹{Number(fare.price).toLocaleString("en-IN")}</h2>

              <button type="button">Select</button>
            </div>
          ))}
        </div>
      </div>

      {/* BAGGAGE */}

      <div className="details-card">
        <h2>Baggage Allowance</h2>

        {flight.baggagePolicies?.length > 0 ? (
          flight.baggagePolicies.map((bag) => (
            <div className="baggage-card" key={bag.id}>
              <h3>{bag.cabinClass}</h3>

              <p>Cabin Baggage</p>

              <strong>{bag.cabinBaggageKg} KG</strong>

              <p>Check-in</p>

              <strong>{bag.checkinBaggageKg} KG</strong>
            </div>
          ))
        ) : (
          <p>No baggage information</p>
        )}
      </div>

      <div className="booking-footer">
        <button disabled={!selectedFare} onClick={continueBooking}>
          Continue Booking
        </button>
      </div>
    </div>
  );
}

export default FlightDetails;
