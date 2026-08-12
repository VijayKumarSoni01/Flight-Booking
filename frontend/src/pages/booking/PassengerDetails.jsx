import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import { createBooking } from "../../api/bookingApi";

import "../../styles/passenger.css";

function PassengerDetails() {
  const navigate = useNavigate();

  const location = useLocation();

  const { flight, fare } = location.state || {};

  const [loading, setLoading] = useState(false);

  const [contact, setContact] = useState({
    email: "",
    phone: "",
  });

  const createEmptyPassenger = () => ({
    title: "",
    firstName: "",
    middleName: "",
    lastName: "",
    dateOfBirth: "",
    gender: "",
    passengerType: "ADULT",
    nationality: "",
    passportNumber: "",
    passportExpiry: "",
    passportIssuingCountry: "",
    seatPreference: null,
    mealPreference: "NONE",
    specialAssistance: null,
  });

  const [passengers, setPassengers] = useState([createEmptyPassenger()]);

  const handleContactChange = (e) => {
    setContact({
      ...contact,
      [e.target.name]: e.target.value,
    });
  };

  const handlePassengerChange = (index, e) => {
    const updated = [...passengers];

    updated[index] = {
      ...updated[index],
      [e.target.name]: e.target.value,
    };

    setPassengers(updated);
  };

  const addPassenger = () => {
    setPassengers([...passengers, createEmptyPassenger()]);
  };

  const removePassenger = (index) => {
    if (passengers.length === 1) return;

    setPassengers(passengers.filter((_, i) => i !== index));
  };

  const validateForm = () => {
    if (!contact.email || !contact.phone) {
      alert("Please enter contact details");

      return false;
    }

    for (const p of passengers) {
      if (
        !p.title ||
        !p.firstName ||
        !p.lastName ||
        !p.dateOfBirth ||
        !p.gender ||
        !p.nationality
      ) {
        alert("Please complete passenger details");

        return false;
      }
    }

    return true;
  };

  const continuePayment = async () => {
    if (!validateForm()) return;

    if (loading) return;

    const bookingRequest = {
      flightId: flight.id,

      // send only if CreateBookingRequest has fareId
      fareId: fare.id,

      // selected fare amount
      totalAmount: Number(fare.price),

      cabinClass: fare.cabinClass,

      contactEmail: contact.email,

      contactPhone: contact.phone,

      paymentMethod: "UPI",

      specialRequest: "",

      couponCode: "",

      passengers: passengers.map((p) => ({
        title: p.title,

        firstName: p.firstName,

        middleName: p.middleName || null,

        lastName: p.lastName,

        dateOfBirth: p.dateOfBirth,

        gender: p.gender,

        passengerType: p.passengerType,

        nationality: p.nationality,

        passportNumber: p.passportNumber || null,

        passportExpiry: p.passportExpiry || null,

        passportIssuingCountry: p.passportIssuingCountry || null,

        seatPreference: p.seatPreference || null,

        mealPreference: p.mealPreference || "NONE",

        specialAssistance: p.specialAssistance || null,
      })),
    };

    console.log(
      "FINAL BOOKING REQUEST",
      JSON.stringify(bookingRequest, null, 2),
    );

    try {
      setLoading(true);

      const response = await createBooking(bookingRequest);

      console.log("BOOKING RESPONSE", response.data);

      const booking = response.data.data || response.data;

      navigate("/payment", {
        state: {
          booking,
          flight,
          fare,
        },
      });
    } catch (error) {
      console.error("BOOKING ERROR", error.response?.data || error);

      alert(error.response?.data?.message || "Booking creation failed");
    } finally {
      setLoading(false);
    }
  };

  if (!flight || !fare) {
    return (
      <div className="empty-box">
        <h3>Booking session expired</h3>

        <button onClick={() => navigate("/")}>Search Flight</button>
      </div>
    );
  }

  return (
    <div className="container mt-5">
      <h2>Passenger Details</h2>

      <div className="booking-card">
        <h4>Contact Information</h4>

        <input
          className="form-control mb-3"
          type="email"
          name="email"
          placeholder="Email"
          value={contact.email}
          onChange={handleContactChange}
        />

        <input
          className="form-control"
          name="phone"
          placeholder="Phone Number"
          value={contact.phone}
          onChange={handleContactChange}
        />
      </div>

      {passengers.map((p, index) => (
        <div className="booking-card" key={index}>
          <div className="d-flex justify-content-between">
            <h4>Passenger {index + 1}</h4>

            {passengers.length > 1 && (
              <button
                className="btn btn-danger btn-sm"
                onClick={() => removePassenger(index)}
              >
                Remove
              </button>
            )}
          </div>

          <div className="row g-3">
            <div className="col-md-3">
              <select
                className="form-control"
                name="title"
                value={p.title}
                onChange={(e) => handlePassengerChange(index, e)}
              >
                <option value="">Title</option>

                <option value="MR">MR</option>

                <option value="MS">MS</option>

                <option value="MRS">MRS</option>
              </select>
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                name="firstName"
                placeholder="First Name"
                value={p.firstName}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                name="lastName"
                placeholder="Last Name"
                value={p.lastName}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>

            <div className="col-md-3">
              <input
                type="date"
                className="form-control"
                name="dateOfBirth"
                value={p.dateOfBirth}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>
          </div>

          <div className="row g-3 mt-2">
            <div className="col-md-3">
              <select
                className="form-control"
                name="gender"
                value={p.gender}
                onChange={(e) => handlePassengerChange(index, e)}
              >
                <option value="">Gender</option>

                <option value="MALE">MALE</option>

                <option value="FEMALE">FEMALE</option>
              </select>
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                name="nationality"
                placeholder="Nationality"
                value={p.nationality}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>

            <div className="col-md-3">
              <input
                className="form-control"
                name="passportNumber"
                placeholder="Passport Number"
                value={p.passportNumber}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>

            <div className="col-md-3">
              <input
                type="date"
                className="form-control"
                name="passportExpiry"
                value={p.passportExpiry}
                onChange={(e) => handlePassengerChange(index, e)}
              />
            </div>
          </div>
        </div>
      ))}

      <button className="btn btn-outline-primary" onClick={addPassenger}>
        + Add Passenger
      </button>

      <div className="booking-card mt-4">
        <h3>Flight Summary</h3>

        <p>
          {flight.airlineName} - {flight.flightNumber}
        </p>

        <p>
          Fare :<b>{fare.name}</b>
        </p>

        <h2>₹ {Number(fare.price).toLocaleString("en-IN")}</h2>

        <button
          className="continue-btn"
          disabled={loading}
          onClick={continuePayment}
        >
          {loading ? "Creating Booking..." : "Continue Payment"}
        </button>
      </div>
    </div>
  );
}

export default PassengerDetails;
