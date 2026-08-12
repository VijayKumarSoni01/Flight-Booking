import React from "react";

function FlightSkeleton() {
  return (
    <div className="skeleton-list">
      {[1, 2, 3].map((item) => (
        <div className="flight-card skeleton-flight-card" key={item}>
          {/* Airline Section */}

          <div className="skeleton-airline">
            <div className="skeleton-circle"></div>

            <div>
              <div className="skeleton-line medium"></div>

              <div className="skeleton-line small"></div>
            </div>
          </div>

          {/* Time Section */}

          <div className="skeleton-time">
            <div className="skeleton-line time"></div>

            <div className="skeleton-line location"></div>
          </div>

          <div className="skeleton-route">
            <div></div>
          </div>

          <div className="skeleton-time">
            <div className="skeleton-line time"></div>

            <div className="skeleton-line location"></div>
          </div>

          {/* Fare Section */}

          <div className="skeleton-fare">
            <div className="skeleton-line price"></div>

            <div className="skeleton-line small"></div>

            <div className="skeleton-button"></div>
          </div>
        </div>
      ))}
    </div>
  );
}

export default FlightSkeleton;
