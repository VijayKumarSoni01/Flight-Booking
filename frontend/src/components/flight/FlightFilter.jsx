import React from "react";

function FlightFilter({
  filters,

  setFilters,

  priceCeiling,

  airlines = [],

  clearFilters,
}) {
  const updateFilter = (key, value) => {
    setFilters((prev) => ({
      ...prev,

      [key]: value,
    }));
  };

  // Price slider maximum value

  const maxPrice = priceCeiling > 0 ? Math.max(priceCeiling, 50000) : 50000;

  // Current selected price

  const currentPrice = filters.maxPrice > 0 ? filters.maxPrice : maxPrice;

  return (
    <aside className="filter-card">
      {/* FILTER HEADER */}

      <div className="filter-header">
        <h4>Filters</h4>

        <button type="button" onClick={clearFilters}>
          Clear
        </button>
      </div>

      <hr />

      {/* CABIN CLASS FILTER */}

      <div className="filter-section">
        <h6>Cabin Class</h6>

        <select
          className="filter-select"
          value={filters.cabin}
          onChange={(e) => updateFilter("cabin", e.target.value)}
        >
          <option value="">All Classes</option>

          <option value="ECONOMY">Economy</option>

          <option value="PREMIUM_ECONOMY">Premium Economy</option>

          <option value="BUSINESS">Business</option>

          <option value="FIRST">First Class</option>
        </select>
      </div>

      {/* AIRLINE FILTER */}

      <div className="filter-section">
        <h6>Airlines</h6>

        <select
          className="filter-select"
          value={filters.airline}
          onChange={(e) => updateFilter("airline", e.target.value)}
        >
          <option value="">All Domestic Flights</option>

          {airlines.length > 0 &&
            airlines.map((airline) => (
              <option key={airline} value={airline}>
                {airline}
              </option>
            ))}
        </select>
      </div>

      {/* PRICE FILTER */}

      <div className="filter-section">
        <h6>Price Range</h6>

        <div className="price-box">
          ₹{Number(currentPrice).toLocaleString("en-IN")}
        </div>

        <input
          type="range"
          className="price-slider"
          min="1000"
          max={maxPrice}
          step="500"
          value={currentPrice}
          onChange={(e) =>
            updateFilter(
              "maxPrice",

              Number(e.target.value),
            )
          }
        />

        <div className="price-range-text">
          <span>₹1,000</span>

          <span>₹{Number(maxPrice).toLocaleString("en-IN")}</span>
        </div>
      </div>
    </aside>
  );
}

export default FlightFilter;
