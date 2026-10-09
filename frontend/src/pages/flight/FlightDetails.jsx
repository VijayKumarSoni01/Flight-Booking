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
    weekday: "short",
    day: "numeric",
    month: "short",
    year: "numeric",
  });
}

function formatDuration(minutes) {
  if (minutes === null || minutes === undefined || minutes < 0) {
    return "--";
  }

  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;

  if (hours === 0) {
    return `${mins}m`;
  }

  if (mins === 0) {
    return `${hours}h`;
  }

  return `${hours}h ${mins}m`;
}

function formatCabinClass(cabinClass) {
  if (!cabinClass) return "";

  return cabinClass
    .toLowerCase()
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(" ");
}

// Current fare-feature fallback.
// Ideally these values should eventually come from the backend.
function getFareFeatures(cabinClass, refundable) {
  return [
    {
      label: "Cabin baggage",
      included: true,
    },
    {
      label: "Free seat selection",
      included: cabinClass !== "ECONOMY",
    },
    {
      label: "Complimentary meal",
      included: cabinClass === "BUSINESS" || cabinClass === "FIRST",
    },
    {
      label: "Refundable",
      included: Boolean(refundable),
    },
  ];
}

function getStopsText(stops) {
  if (!stops || stops.length === 0) {
    return "Nonstop";
  }

  return `${stops.length} stop${stops.length > 1 ? "s" : ""}`;
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

      setFlight(response.data.data || response.data);
    } catch (error) {
      console.error("Flight loading error:", error);
    } finally {
      setLoading(false);
    }
  };

  const continueBooking = () => {
    if (!selectedFare || !flight) {
      return;
    }

    const bookingData = {
      flightId: flight.id,
      flight: flight,
      fare: selectedFare,
    };

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

  /*
   * Loading state
   */
  if (loading) {
    return (
      <div className="flight-details-page">
        <div className="details-card skeleton-card">
          <div className="skeleton-line skeleton-line--wide" />
          <div className="skeleton-line skeleton-line--medium" />

          <div className="skeleton-row">
            <div className="skeleton-block" />
            <div className="skeleton-block" />
            <div className="skeleton-block" />
          </div>
        </div>
      </div>
    );
  }

  /*
   * Flight not found
   */
  if (!flight) {
    return (
      <div className="flight-details-page">
        <div className="empty-box">
          <h3>This flight isn't available</h3>

          <p>
            It may have been removed or the link is out of date. Try searching
            again.
          </p>

          <button type="button" onClick={() => navigate("/flights")}>
            Back to search
          </button>
        </div>
      </div>
    );
  }

  /*
   * Fare list
   */
  const fares = [
    {
      id: 1,
      name: "Economy",
      cabinClass: "ECONOMY",
      price: flight.economyPrice,
    },
    {
      id: 2,
      name: "Premium Economy",
      cabinClass: "PREMIUM_ECONOMY",
      price: flight.premiumEconomyPrice,
    },
    {
      id: 3,
      name: "Business",
      cabinClass: "BUSINESS",
      price: flight.businessPrice,
    },
    {
      id: 4,
      name: "First Class",
      cabinClass: "FIRST",
      price: flight.firstPrice,
    },
  ].filter(
    (fare) =>
      fare.price !== null &&
      fare.price !== undefined &&
      Number(fare.price) >= 0,
  );

  const recommendedId = fares.length > 1 ? fares[1].id : fares[0]?.id;

  return (
    <div className="flight-details-page">
      {/* =========================================================
          FLIGHT HEADER / BOARDING PASS
      ========================================================== */}

      <div className="ticket-card">
        {/* Airline Header */}
        <div className="ticket-top">
          <div className="airline-brand">
            <div className="airline-mark">
              {flight.airlineName?.charAt(0)?.toUpperCase() || "A"}
            </div>

            <div>
              <h2 className="airline-name">
                {flight.airlineName || "Airline"}
              </h2>

              <p className="flight-number">
                Flight {flight.flightNumber || "—"}
              </p>
            </div>
          </div>

          <div className="ticket-badges">
            {flight.refundable && (
              <span className="refund-pill">Refundable</span>
            )}

            <span className="status-pill">Flight available</span>
          </div>
        </div>

        {/* =====================================================
            ROUTE
        ====================================================== */}

        <div className="ticket-route">
          {/* Departure */}
          <div className="route-point">
            <span className="route-code">
              {flight.originAirportCode || "---"}
            </span>

            <span className="route-time">
              {formatTime(flight.departureTime)}
            </span>

            <span className="route-date">
              {formatDate(flight.departureTime)}
            </span>

            <span className="route-label">Departure</span>

            <span className="route-airport">
              {flight.originAirportName || "Departure airport"}
            </span>

            {flight.departureTerminal && (
              <span className="route-terminal">
                Terminal {flight.departureTerminal}
              </span>
            )}
          </div>

          {/* Route path */}
          <div className="route-path">
            <span className="route-duration">
              {formatDuration(flight.durationMinutes)}
            </span>

            <div className="route-line">
              <span className="route-dot" />

              <span className="route-plane">✈</span>

              <span className="route-dot" />
            </div>

            <span className="route-stops">{getStopsText(flight.stops)}</span>
          </div>

          {/* Arrival */}
          <div className="route-point route-point--end">
            <span className="route-code">
              {flight.destinationAirportCode || "---"}
            </span>

            <span className="route-time">{formatTime(flight.arrivalTime)}</span>

            <span className="route-date">{formatDate(flight.arrivalTime)}</span>

            <span className="route-label">Arrival</span>

            <span className="route-airport">
              {flight.destinationAirportName || "Arrival airport"}
            </span>

            {flight.arrivalTerminal && (
              <span className="route-terminal">
                Terminal {flight.arrivalTerminal}
              </span>
            )}
          </div>
        </div>

        <div className="ticket-perforation" aria-hidden="true" />

        {/* =====================================================
            FLIGHT METADATA
        ====================================================== */}

        <div className="ticket-meta">
          <div className="meta-item">
            <span className="meta-label">Flight</span>

            <span className="meta-value">{flight.flightNumber || "—"}</span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Date</span>

            <span className="meta-value">
              {formatDate(flight.departureTime)}
            </span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Terminal</span>

            <span className="meta-value">
              {flight.departureTerminal || "TBD"}
            </span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Aircraft</span>

            <span className="meta-value">{flight.aircraftType || "—"}</span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Duration</span>

            <span className="meta-value">
              {formatDuration(flight.durationMinutes)}
            </span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Stops</span>

            <span className="meta-value">{getStopsText(flight.stops)}</span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Refund</span>

            <span className="meta-value">
              {flight.refundable ? "Refundable" : "Non-refundable"}
            </span>
          </div>
        </div>
      </div>

      {/* =========================================================
          FARES
      ========================================================== */}

      <div className="details-card">
        <h2 className="section-title">Choose your fare</h2>

        <p className="section-subtitle">
          Select the cabin and fare that best suits your journey.
        </p>

        {fares.length > 0 ? (
          <div className="fare-grid">
            {fares.map((fare) => {
              const isSelected = selectedFare?.id === fare.id;

              const features = getFareFeatures(
                fare.cabinClass,
                flight.refundable,
              );

              return (
                <button
                  type="button"
                  key={fare.id}
                  className={`fare-card ${
                    isSelected ? "fare-card--selected" : ""
                  }`}
                  onClick={() => setSelectedFare(fare)}
                >
                  {fare.id === recommendedId && !isSelected && (
                    <span className="fare-tag">Recommended</span>
                  )}

                  {isSelected && <span className="fare-check">✓</span>}

                  <span className="fare-name">{fare.name}</span>

                  <span className="fare-price">
                    ₹{Number(fare.price).toLocaleString("en-IN")}
                  </span>

                  <span className="fare-hint">per passenger</span>

                  <ul className="fare-features">
                    {features.map((feature) => (
                      <li
                        key={feature.label}
                        className={
                          feature.included ? "feature-yes" : "feature-no"
                        }
                      >
                        <span className="feature-mark">
                          {feature.included ? "✓" : "✕"}
                        </span>

                        {feature.label}
                      </li>
                    ))}
                  </ul>
                </button>
              );
            })}
          </div>
        ) : (
          <p className="muted-text">
            No fare options are currently available for this flight.
          </p>
        )}
      </div>

      {/* =========================================================
          SELECTED FARE SUMMARY
      ========================================================== */}

      {selectedFare && (
        <div className="details-card selected-fare-card">
          <div className="selected-fare-header">
            <div>
              <span className="selected-fare-label">Selected fare</span>

              <h3>{selectedFare.name}</h3>

              <p>{formatCabinClass(selectedFare.cabinClass)}</p>
            </div>

            <div className="selected-fare-price">
              <span>Per passenger</span>

              <strong>
                ₹{Number(selectedFare.price).toLocaleString("en-IN")}
              </strong>
            </div>
          </div>

          <div className="selected-fare-details">
            <div>
              <span>Cabin baggage</span>

              <strong>Included</strong>
            </div>

            <div>
              <span>Seat selection</span>

              <strong>
                {selectedFare.cabinClass === "ECONOMY"
                  ? "As per fare"
                  : "Included"}
              </strong>
            </div>

            <div>
              <span>Meal</span>

              <strong>
                {selectedFare.cabinClass === "BUSINESS" ||
                selectedFare.cabinClass === "FIRST"
                  ? "Included"
                  : "As per fare"}
              </strong>
            </div>

            <div>
              <span>Refundability</span>

              <strong>
                {flight.refundable ? "Refundable" : "Non-refundable"}
              </strong>
            </div>
          </div>
        </div>
      )}

      <div className="details-card">
        <h2 className="section-title">Onboard amenities</h2>

        <p className="section-subtitle">Amenities available on this flight.</p>

        {flight.amenities ? (
          <div className="amenity-grid">
            {flight.amenities.mealIncluded && (
              <div className="amenity-item">
                <span className="amenity-dot" />
                <div>
                  <p className="amenity-name">Complimentary Meal</p>
                  <p className="amenity-desc">
                    Meal service is available on this flight.
                  </p>
                </div>
              </div>
            )}

            {flight.amenities.wifiAvailable && (
              <div className="amenity-item">
                <span className="amenity-dot" />
                <div>
                  <p className="amenity-name">Wi-Fi</p>
                  <p className="amenity-desc">Wi-Fi is available onboard.</p>
                </div>
              </div>
            )}

            {flight.amenities.usbCharging && (
              <div className="amenity-item">
                <span className="amenity-dot" />
                <div>
                  <p className="amenity-name">USB Charging</p>
                  <p className="amenity-desc">
                    USB charging is available onboard.
                  </p>
                </div>
              </div>
            )}

            {flight.amenities.entertainmentSystem && (
              <div className="amenity-item">
                <span className="amenity-dot" />
                <div>
                  <p className="amenity-name">Entertainment System</p>
                  <p className="amenity-desc">
                    In-flight entertainment is available.
                  </p>
                </div>
              </div>
            )}
          </div>
        ) : (
          <p className="muted-text">
            Amenity details aren't listed for this flight yet.
          </p>
        )}
      </div>

      {/* =========================================================
          BAGGAGE
      ========================================================== */}

      <div className="details-card">
        <h2 className="section-title">Baggage allowance</h2>

        <p className="section-subtitle">
          Baggage allowance depends on the selected cabin class and fare.
        </p>

        {flight.baggagePolicies?.length > 0 ? (
          <div className="baggage-grid">
            {flight.baggagePolicies.map((bag) => (
              <div className="baggage-card" key={bag.id}>
                <span className="baggage-class">
                  {formatCabinClass(bag.cabinClass)}
                </span>

                <div className="baggage-row">
                  <span>Cabin baggage</span>

                  <strong>{bag.cabinBaggageKg ?? 0} kg</strong>
                </div>

                <div className="baggage-row">
                  <span>Check-in baggage</span>

                  <strong>{bag.checkinBaggageKg ?? 0} kg</strong>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="muted-text">
            Baggage details aren't available for this flight yet.
          </p>
        )}
      </div>

      {/* =========================================================
          CANCELLATION / REFUND
      ========================================================== */}

      <div className="details-card">
        <h2 className="section-title">Cancellation & refund</h2>

        <div className="policy-box">
          {flight.refundable ? (
            <>
              <div className="policy-status policy-status--success">
                <span>✓</span>
                <strong>This flight is refundable</strong>
              </div>

              <p>
                Cancellation and refund charges may depend on the selected fare
                and the time remaining before departure.
              </p>
            </>
          ) : (
            <>
              <div className="policy-status policy-status--warning">
                <span>!</span>
                <strong>This flight is non-refundable</strong>
              </div>

              <p>
                Cancellation may not qualify for a refund. Check the selected
                fare rules before completing your booking.
              </p>
            </>
          )}
        </div>
      </div>

      {/* =========================================================
          FARE RULES
      ========================================================== */}

      <div className="details-card fare-rules-card">
        <h2 className="section-title">Before you book</h2>

        <ul className="rules-list">
          <li>
            Fare changes and cancellation charges depend on the fare type
            selected above.
          </li>

          <li>Web check-in opens 48 hours before departure.</li>

          <li>
            Ensure passenger names match government ID exactly. Corrections
            after booking may incur a fee.
          </li>

          <li>
            Please verify the departure terminal, baggage allowance and fare
            conditions before continuing.
          </li>

          <li>Seat availability may change before booking confirmation.</li>
        </ul>
      </div>

      {/* =========================================================
          TRUST STRIP
      ========================================================== */}

      <div className="trust-strip">
        <div className="trust-item">
          <span className="trust-icon">🔒</span>

          <span>Secure checkout</span>
        </div>

        <div className="trust-item">
          <span className="trust-icon">⚡</span>

          <span>Instant confirmation</span>
        </div>

        <div className="trust-item">
          <span className="trust-icon">🎧</span>

          <span>24/7 support</span>
        </div>
      </div>

      {/* =========================================================
          STICKY BOOKING BAR
      ========================================================== */}

      <div className="booking-footer">
        <div className="booking-footer-price">
          {selectedFare ? (
            <>
              <span className="price-label">
                {selectedFare.name} · Per passenger
              </span>

              <span className="price-value">
                ₹{Number(selectedFare.price).toLocaleString("en-IN")}
              </span>
            </>
          ) : (
            <span className="price-placeholder">Select a fare to continue</span>
          )}
        </div>

        <button
          type="button"
          className="continue-btn"
          disabled={!selectedFare}
          onClick={continueBooking}
        >
          Continue booking
        </button>
      </div>
    </div>
  );
}

export default FlightDetails;
