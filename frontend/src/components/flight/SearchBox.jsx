import React, { useEffect, useState } from "react";

import "../../styles/searchBox.css";

function SearchBox({ initialValues, onSearch }) {
  const [tripType, setTripType] = useState(
    initialValues?.tripType || "ONE_WAY",
  );

  const [formData, setFormData] = useState({
    source: initialValues?.source || "",

    destination: initialValues?.destination || "",

    departureDate: initialValues?.departureDate || "",

    returnDate: initialValues?.returnDate || "",

    passengers: initialValues?.passengers || 1,
  });

  useEffect(() => {
    if (initialValues) {
      setFormData({
        source: initialValues.source || "",

        destination: initialValues.destination || "",

        departureDate: initialValues.departureDate || "",

        returnDate: initialValues.returnDate || "",

        passengers: initialValues.passengers || 1,
      });
    }
  }, [initialValues]);

  const handleChange = (e) => {
    setFormData({
      ...formData,

      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!formData.source || !formData.destination || !formData.departureDate) {
      alert("Please enter From, To and Departure date");

      return;
    }

    onSearch({
      ...formData,

      tripType,
    });
  };

  return (
    <form className="flight-search-card" onSubmit={handleSubmit}>
      {/* TABS */}

      <div className="booking-tabs">
        <button type="button" className="active-tab">
          Flights
        </button>

        <button type="button">Hotels</button>

        <button type="button">Offers</button>
      </div>

      {/* TRIP TYPE */}

      <div className="trip-options">
        <label>
          <input
            type="radio"
            checked={tripType === "ONE_WAY"}
            onChange={() => setTripType("ONE_WAY")}
          />
          One Way
        </label>

        <label>
          <input
            type="radio"
            checked={tripType === "ROUND_TRIP"}
            onChange={() => setTripType("ROUND_TRIP")}
          />
          Round Trip
        </label>
      </div>

      <div className="search-fields">
        <div className="field">
          <label>From</label>

          <input
            type="text"
            name="source"
            value={formData.source}
            onChange={handleChange}
            placeholder="Enter airport code (DEL)"
          />
        </div>

        <div className="field">
          <label>To</label>

          <input
            type="text"
            name="destination"
            value={formData.destination}
            onChange={handleChange}
            placeholder="Enter airport code (BOM)"
          />
        </div>

        <div className="field">
          <label>Departure</label>

          <input
            type="date"
            name="departureDate"
            value={formData.departureDate}
            onChange={handleChange}
          />
        </div>

        {tripType === "ROUND_TRIP" && (
          <div className="field">
            <label>Return</label>

            <input
              type="date"
              name="returnDate"
              value={formData.returnDate}
              onChange={handleChange}
            />
          </div>
        )}

        <div className="field">
          <label>Passengers</label>

          <select
            name="passengers"
            value={formData.passengers}
            onChange={handleChange}
          >
            <option value="1">1 Passenger</option>

            <option value="2">2 Passengers</option>

            <option value="3">3 Passengers</option>

            <option value="4">4 Passengers</option>
          </select>
        </div>

        <button type="submit" className="search-flight-btn">
          Search Flights
        </button>
      </div>
    </form>
  );
}

export default SearchBox;
