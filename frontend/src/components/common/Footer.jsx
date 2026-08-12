import React from "react";

import "../../styles/footer.css";

function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-top">
          {/* BRAND */}

          <div className="footer-brand">
            <h2>✈ FlightBooking</h2>

            <p>
              Book flights worldwide with secure payments, best prices and a
              seamless travel experience.
            </p>
          </div>

          {/* LINKS */}

          <div className="footer-column">
            <h4>Explore</h4>

            <a>Flights</a>

            <a>Destinations</a>

            <a>Offers</a>

            <a>My Bookings</a>
          </div>

          <div className="footer-column">
            <h4>Support</h4>

            <a>Help Center</a>

            <a>Contact Us</a>

            <a>Cancellation</a>

            <a>Privacy Policy</a>
          </div>

          {/* CONTACT */}

          <div className="footer-column">
            <h4>Contact</h4>

            <p>📞 +91 98765 43210</p>

            <p>✉ support@flightbooking.com</p>

            <p>🔒 Secure Payment</p>
          </div>
        </div>

        <div className="footer-line"></div>

        <div className="footer-bottom">
          <span>© 2026 FlightBooking. All rights reserved.</span>

          <div>🌐 📘 📸</div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
