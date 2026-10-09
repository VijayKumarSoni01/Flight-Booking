import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import { createBooking } from "../../api/bookingApi";
import { getSeatsByFlight } from "../../api/flightApi";

import "../../styles/passenger.css";

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const PHONE_REGEX = /^\d{10}$/;

const SEATS_PER_ROW = {
  ECONOMY: 6,
  PREMIUM_ECONOMY: 6,
  BUSINESS: 4,
  FIRST: 4,
};

function PassengerDetails() {
  const navigate = useNavigate();
  const location = useLocation();

  const { flight, fare } = location.state || {};

  const [loading, setLoading] = useState(false);
  const [seatLoading, setSeatLoading] = useState(false);
  const [seats, setSeats] = useState([]);
  const [formError, setFormError] = useState("");

  const [contact, setContact] = useState({ email: "", phone: "" });

  const createEmptyPassenger = () => ({
    id: crypto.randomUUID(),
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
    seatNumber: null,
    seatPreference: null,
    mealPreference: "NONE",
    specialAssistance: null,
  });

  const [passengers, setPassengers] = useState([createEmptyPassenger()]);

  useEffect(() => {
    if (!flight?.id) return;
    loadSeats();
  }, [flight]);

  const loadSeats = async () => {
    try {
      setSeatLoading(true);
      const response = await getSeatsByFlight(flight.id);
      setSeats(response.data || []);
    } catch (error) {
      console.error("Seat loading failed:", error);
      setSeats([]);
      setFormError("Unable to load seats. Please refresh and try again.");
    } finally {
      setSeatLoading(false);
    }
  };

  const handleContactChange = (e) => {
    setContact({ ...contact, [e.target.name]: e.target.value });
  };

  const handlePassengerChange = (index, e) => {
    const updated = [...passengers];
    updated[index] = { ...updated[index], [e.target.name]: e.target.value };
    setPassengers(updated);
  };

  const addPassenger = () => {
    setPassengers([...passengers, createEmptyPassenger()]);
  };

  const removePassenger = (index) => {
    if (passengers.length === 1) return;
    setPassengers(passengers.filter((_, i) => i !== index));
  };

  const handleSeatSelect = (passengerIndex, seat) => {
    if (seat.seatStatus !== "AVAILABLE") return;

    const alreadySelected = passengers.some(
      (passenger, index) =>
        index !== passengerIndex && passenger.seatNumber === seat.seatNumber,
    );

    if (alreadySelected) {
      setFormError(
        `Seat ${seat.seatNumber} is already selected by another passenger.`,
      );
      return;
    }

    setFormError("");

    const updated = [...passengers];

    if (updated[passengerIndex].seatNumber === seat.seatNumber) {
      updated[passengerIndex] = {
        ...updated[passengerIndex],
        seatNumber: null,
      };
    } else {
      updated[passengerIndex] = {
        ...updated[passengerIndex],
        seatNumber: seat.seatNumber,
      };
    }

    setPassengers(updated);
  };

  const getCabinSeats = () => {
    if (!fare?.cabinClass) return [];
    return seats.filter((seat) => seat.cabinClass === fare.cabinClass);
  };

  const validateForm = () => {
    if (!contact.email || !EMAIL_REGEX.test(contact.email)) {
      setFormError("Enter a valid email address.");
      return false;
    }

    if (!contact.phone || !PHONE_REGEX.test(contact.phone)) {
      setFormError("Enter a valid 10-digit phone number.");
      return false;
    }

    for (let i = 0; i < passengers.length; i++) {
      const p = passengers[i];

      if (
        !p.title ||
        !p.firstName ||
        !p.lastName ||
        !p.dateOfBirth ||
        !p.gender ||
        !p.nationality
      ) {
        setFormError(`Complete all required details for passenger ${i + 1}.`);
        return false;
      }

      if (!p.seatNumber) {
        setFormError(`Select a seat for passenger ${i + 1}.`);
        return false;
      }

      if (p.passportExpiry) {
        const expiryDate = new Date(p.passportExpiry);
        const today = new Date();

        today.setHours(0, 0, 0, 0);
        expiryDate.setHours(0, 0, 0, 0);

        if (expiryDate <= today) {
          setFormError(
            `Passport expiry must be in the future for passenger ${i + 1}.`,
          );
          return false;
        }
      }
    }

    setFormError("");
    return true;
  };

  const continuePayment = async () => {
    if (!validateForm()) return;
    if (loading) return;

    const bookingRequest = {
      flightId: flight.id,
      fareId: fare.id,
      totalAmount: totalAmount,
      cabinClass: fare.cabinClass,
      contactEmail: contact.email,
      contactPhone: contact.phone,
      paymentMethod: "UPI",
      specialRequest: "",
      couponCode: "",
      seatNumbers: passengers.map((p) => p.seatNumber).filter(Boolean),
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
        seatNumber: p.seatNumber || null,
        seatPreference: p.seatPreference || null,
        mealPreference: p.mealPreference || "NONE",
        specialAssistance: p.specialAssistance || null,
      })),
    };

    try {
      setLoading(true);

      const response = await createBooking(bookingRequest);
      const booking = response.data.data || response.data;

      navigate("/payment", { state: { booking, flight, fare } });
    } catch (error) {
      console.error("Booking creation failed:", error);
      setFormError(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "Booking creation failed. Please try again.",
      );
    } finally {
      setLoading(false);
    }
  };

  if (!flight || !fare) {
    return (
      <div className="passenger-page">
        <div className="empty-box">
          <h3>Booking session expired</h3>
          <p>Your flight selection couldn't be found. Start a new search.</p>
          <button className="btn-primary" onClick={() => navigate("/")}>
            Search flights
          </button>
        </div>
      </div>
    );
  }

  const cabinSeats = getCabinSeats();
  const perRow = SEATS_PER_ROW[fare.cabinClass] || 6;
  const half = Math.ceil(perRow / 2);

  const sortedSeats = [...cabinSeats].sort((a, b) => {
    const numberA = parseInt(a.seatNumber.replace(/\D/g, ""), 10);
    const numberB = parseInt(b.seatNumber.replace(/\D/g, ""), 10);
    return numberA - numberB;
  });

  const seatRows = [];
  for (let i = 0; i < sortedSeats.length; i += perRow) {
    seatRows.push(sortedSeats.slice(i, i + perRow));
  }

  const selectedSeatCount = passengers.filter((p) => p.seatNumber).length;
  const totalAmount = Number(fare.price) * passengers.length;

  return (
    <div className="passenger-page">
      <div className="booking-steps">
        <div className="step completed">
          <span className="step-num">1</span> Flight
        </div>
        <div className="step-line" />
        <div className="step active">
          <span className="step-num">2</span> Passengers
        </div>
        <div className="step-line" />
        <div className="step">
          <span className="step-num">3</span> Payment
        </div>
      </div>

      <div className="passenger-layout">
        <div className="passenger-main">
          <h2>Passenger details</h2>

          {formError && (
            <div className="form-error-banner">
              <span>{formError}</span>
              <button onClick={() => setFormError("")} aria-label="Dismiss">
                &times;
              </button>
            </div>
          )}

          <div className="booking-card">
            <h4>Contact information</h4>
            <p className="card-subtitle">
              Your ticket and booking updates will be sent here.
            </p>

            <div className="row g-3">
              <div className="col-md-6">
                <label htmlFor="contact-email">Email *</label>
                <input
                  id="contact-email"
                  className="form-control"
                  type="email"
                  name="email"
                  placeholder="you@example.com"
                  value={contact.email}
                  onChange={handleContactChange}
                  required
                />
              </div>

              <div className="col-md-6">
                <label htmlFor="contact-phone">Phone number *</label>
                <input
                  id="contact-phone"
                  className="form-control"
                  name="phone"
                  placeholder="10-digit mobile number"
                  value={contact.phone}
                  onChange={handleContactChange}
                  required
                />
              </div>
            </div>
          </div>

          {passengers.map((p, index) => (
            <div className="booking-card" key={p.id}>
              <div className="passenger-card-header">
                <h4>Passenger {index + 1}</h4>

                {passengers.length > 1 && (
                  <button
                    type="button"
                    className="btn-remove"
                    onClick={() => removePassenger(index)}
                  >
                    Remove
                  </button>
                )}
              </div>

              <div className="row g-3">
                <div className="col-md-2">
                  <label>Title *</label>
                  <select
                    className="form-control"
                    name="title"
                    value={p.title}
                    onChange={(e) => handlePassengerChange(index, e)}
                  >
                    <option value="">Select</option>
                    <option value="MR">Mr</option>
                    <option value="MS">Ms</option>
                    <option value="MRS">Mrs</option>
                  </select>
                </div>

                <div className="col-md-3">
                  <label>First name *</label>
                  <input
                    className="form-control"
                    name="firstName"
                    value={p.firstName}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>

                <div className="col-md-3">
                  <label>Last name *</label>
                  <input
                    className="form-control"
                    name="lastName"
                    value={p.lastName}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>

                <div className="col-md-2">
                  <label>Date of birth *</label>
                  <input
                    type="date"
                    className="form-control"
                    name="dateOfBirth"
                    value={p.dateOfBirth}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>

                <div className="col-md-2">
                  <label>Type *</label>
                  <select
                    className="form-control"
                    name="passengerType"
                    value={p.passengerType}
                    onChange={(e) => handlePassengerChange(index, e)}
                  >
                    <option value="ADULT">Adult</option>
                    <option value="CHILD">Child</option>
                    <option value="INFANT">Infant</option>
                  </select>
                </div>
              </div>

              <div className="row g-3 mt-2">
                <div className="col-md-2">
                  <label>Gender *</label>
                  <select
                    className="form-control"
                    name="gender"
                    value={p.gender}
                    onChange={(e) => handlePassengerChange(index, e)}
                  >
                    <option value="">Select</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                  </select>
                </div>

                <div className="col-md-3">
                  <label>Nationality *</label>
                  <input
                    className="form-control"
                    name="nationality"
                    value={p.nationality}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>

                <div className="col-md-3">
                  <label>Meal preference</label>
                  <select
                    className="form-control"
                    name="mealPreference"
                    value={p.mealPreference}
                    onChange={(e) => handlePassengerChange(index, e)}
                  >
                    <option value="NONE">No preference</option>
                    <option value="VEG">Vegetarian</option>
                    <option value="NON_VEG">Non-vegetarian</option>
                    <option value="JAIN">Jain</option>
                  </select>
                </div>

                <div className="col-md-2">
                  <label>Passport no.</label>
                  <input
                    className="form-control"
                    name="passportNumber"
                    value={p.passportNumber}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>

                <div className="col-md-2">
                  <label>Passport expiry</label>
                  <input
                    type="date"
                    className="form-control"
                    name="passportExpiry"
                    value={p.passportExpiry}
                    onChange={(e) => handlePassengerChange(index, e)}
                  />
                </div>
              </div>

              <div className="seat-selection mt-4">
                <div className="seat-selection-header">
                  <h5>Select seat *</h5>
                  <span className="cabin-badge">{fare.cabinClass}</span>
                </div>

                {seatLoading ? (
                  <p className="mt-3 text-muted">Loading seat map...</p>
                ) : cabinSeats.length === 0 ? (
                  <p className="mt-3 text-muted">
                    No seats available for this cabin.
                  </p>
                ) : (
                  <div className="cabin-map mt-3">
                    {seatRows.map((row, rowIndex) => (
                      <div className="seat-row" key={rowIndex}>
                        <span className="row-number">{rowIndex + 1}</span>

                        <div className="seat-row-seats">
                          {row
                            .slice(0, half)
                            .map((seat) =>
                              renderSeat(
                                seat,
                                index,
                                p,
                                passengers,
                                handleSeatSelect,
                              ),
                            )}
                        </div>

                        <div className="aisle" />

                        <div className="seat-row-seats">
                          {row
                            .slice(half)
                            .map((seat) =>
                              renderSeat(
                                seat,
                                index,
                                p,
                                passengers,
                                handleSeatSelect,
                              ),
                            )}
                        </div>
                      </div>
                    ))}
                  </div>
                )}

                <div className="seat-legend mt-3">
                  <span>
                    <span className="legend-box available-box" /> Available
                  </span>
                  <span>
                    <span className="legend-box selected-box" /> Selected
                  </span>
                  <span>
                    <span className="legend-box unavailable-box" /> Booked /
                    held
                  </span>
                </div>

                {p.seatNumber && (
                  <p className="mt-3 selected-seat-text">
                    Selected seat: <strong>{p.seatNumber}</strong>
                  </p>
                )}
              </div>
            </div>
          ))}

          <button
            type="button"
            className="btn-add-passenger"
            onClick={addPassenger}
          >
            + Add passenger
          </button>
        </div>

        <aside className="passenger-summary">
          <div className="summary-card">
            <h4>Flight summary</h4>

            <div className="summary-route">
              <span className="summary-code">{flight.source}</span>
              <svg
                viewBox="0 0 24 24"
                width="16"
                height="16"
                fill="currentColor"
                aria-hidden="true"
              >
                <path d="M21 16v-2l-8-5V3.5a1.5 1.5 0 0 0-3 0V9l-8 5v2l8-2.5V19l-2.5 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z" />
              </svg>
              <span className="summary-code">{flight.destination}</span>
            </div>

            <p className="summary-airline">
              {flight.airlineName} &middot; {flight.flightNumber}
            </p>

            <hr />

            <div className="summary-row">
              <span>Fare type</span>
              <strong>{fare.name}</strong>
            </div>

            <div className="summary-row">
              <span>Cabin</span>
              <strong>{fare.cabinClass}</strong>
            </div>

            <div className="summary-row">
              <span>Passengers</span>
              <strong>{passengers.length}</strong>
            </div>

            <div className="summary-row">
              <span>Seats selected</span>
              <strong>
                {selectedSeatCount} / {passengers.length}
              </strong>
            </div>

            {selectedSeatCount > 0 && (
              <p className="summary-seats">
                {passengers
                  .map((p) => p.seatNumber)
                  .filter(Boolean)
                  .join(", ")}
              </p>
            )}

            <hr />

            <div className="summary-total">
              <span>Total amount</span>
              <span className="summary-price">
                &#8377;{totalAmount.toLocaleString("en-IN")}
              </span>
            </div>

            <button
              type="button"
              className="btn-primary btn-continue"
              disabled={loading || seatLoading}
              onClick={continuePayment}
            >
              {loading ? "Creating booking..." : "Continue to payment"}
            </button>
          </div>
        </aside>
      </div>
    </div>
  );
}

function renderSeat(
  seat,
  passengerIndex,
  currentPassenger,
  passengers,
  handleSeatSelect,
) {
  const isAvailable = seat.seatStatus === "AVAILABLE";
  const isSelected = currentPassenger.seatNumber === seat.seatNumber;

  const selectedByOther = passengers.some(
    (passenger, i) =>
      i !== passengerIndex && passenger.seatNumber === seat.seatNumber,
  );

  const disabled = !isAvailable || selectedByOther;

  return (
    <button
      type="button"
      key={seat.id}
      disabled={disabled}
      className={`seat ${isAvailable ? "available" : "unavailable"} ${
        isSelected ? "selected" : ""
      } ${selectedByOther ? "selected-by-other" : ""}`}
      onClick={() => handleSeatSelect(passengerIndex, seat)}
    >
      {seat.seatNumber}
    </button>
  );
}

export default PassengerDetails;
