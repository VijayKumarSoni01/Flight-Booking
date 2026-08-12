import React from "react";

function formatTime(value) {
  if (!value) return "--:--";

  try {
    return new Date(value).toLocaleTimeString([], {
      hour: "2-digit",

      minute: "2-digit",
    });
  } catch (error) {
    return value;
  }
}

function FlightCard({ flight, onSelect }) {
  console.log("Flight Card Data:", flight);

  /*
       PRICE FROM DTO

       Backend:
       economyPrice
       premiumEconomyPrice
       businessPrice
       firstPrice
    */

  const price = flight.economyPrice ?? 0;

  const currency = flight.currency ?? "INR";

  const airline = flight.airlineName ?? "Airline";

  const source = flight.originAirportCode ?? "--";

  const destination = flight.destinationAirportCode ?? "--";

  const duration = flight.durationMinutes
    ? `${Math.floor(flight.durationMinutes / 60)}h ${
        flight.durationMinutes % 60
      }m`
    : "--";

  return (
    <div className="flight-card">
      {/* AIRLINE DETAILS */}

      <div className="airline-section">
        <div className="airline-logo">✈️</div>

        <div>
          <h5>{airline}</h5>

          <p>{flight.flightNumber || "--"}</p>
        </div>
      </div>

      {/* JOURNEY DETAILS */}

      <div className="time-section">
        <div className="time-box">
          <h3>{formatTime(flight.departureTime)}</h3>

          <p>{source}</p>
        </div>

        <div className="duration">
          <span>{duration}</span>

          <hr />

          <small>Non Stop</small>
        </div>

        <div className="time-box">
          <h3>{formatTime(flight.arrivalTime)}</h3>

          <p>{destination}</p>
        </div>
      </div>

      {/* FARE DETAILS */}

      <div className="fare-section">
        <h3>
          {currency === "INR" && "₹"}

          {Number(price).toLocaleString("en-IN")}
        </h3>

        <p>ECONOMY</p>

        <button className="btn-primary" onClick={onSelect}>
          Select Flight
        </button>
      </div>
    </div>
  );
}

export default FlightCard;
