import React from "react";
import { useNavigate } from "react-router-dom";

import SearchBox from "../../components/flight/SearchBox";

import heroImage from "../../assets/hero.jpeg";

import "../../styles/home.css";

function Home() {
  const navigate = useNavigate();

  return (
    <div>
      {/* HERO SECTION */}

      <section className="hero-section">
        <div className="hero-overlay">
          <div className="container">
            <div className="row align-items-center">
              {/* LEFT CONTENT */}

              <div className="col-md-6 hero-content">
                <h1 className="hero-title">
                  Book Your Next
                  <br />
                  Journey With Us
                </h1>

                <p className="hero-text">
                  Search and compare flights from hundreds of airlines and
                  travel your dream destination.
                </p>
              </div>

              {/* RIGHT IMAGE */}
            </div>
          </div>
        </div>
      </section>

      {/* SEARCH BOX */}

      <section className="search-container">
        <SearchBox
          onSearch={(data) => {
            navigate("/flights", {
              state: data,
            });
          }}
        />
      </section>

      {/* POPULAR DESTINATIONS */}

      <section className="popular-section container">
        <h2 className="section-title">Popular Destinations</h2>

        <div className="destination-wrapper">
          {[
            {
              city: "Delhi",
              country: "India",
              image: "/images/delhi.jpg",
            },

            {
              city: "Mumbai",
              country: "India",
              image: "/images/mumbai.jpg",
            },

            {
              city: "Kashmir",
              country: "India",
              image: "/images/kashmir.jpg",
            },

            {
              city: "Dubai",
              country: "UAE",
              image: "/images/dubai.jpg",
            },

            {
              city: "Singapore",
              country: "Singapore",
              image: "/images/singapore.jpg",
            },

            {
              city: "Japan",
              country: "Japan",
              image: "/images/japan.jpg",
            },
          ].map((place) => (
            <div className="destination-card" key={place.city}>
              <img
                src={place.image}
                alt={place.city}
                className="destination-image"
              />

              <div className="destination-content">
                <h5>{place.city}</h5>

                <small>{place.country}</small>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* FEATURES */}

      {/* FEATURES */}

      <section className="features-section container mt-5 mb-5">
        <h2 className="section-title">Why Choose FlightBooking?</h2>

        <p className="section-subtitle">
          Experience a smooth, secure and reliable way to book your flights.
        </p>

        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">🔒</div>

            <h5>Secure Payments</h5>

            <p>
              Your payments and personal data are protected with secure
              technology.
            </p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">⚡</div>

            <h5>Fast Booking</h5>

            <p>
              Search flights and complete your booking in just a few clicks.
            </p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">💰</div>

            <h5>Best Flight Deals</h5>

            <p>Compare prices and find affordable flights for your journey.</p>
          </div>

          <div className="feature-card">
            <div className="feature-icon">🌍</div>

            <h5>Global Destinations</h5>

            <p>Travel to domestic and international destinations worldwide.</p>
          </div>
        </div>
      </section>
    </div>
  );
}

export default Home;
