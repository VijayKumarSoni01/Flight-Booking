import React, { useEffect, useMemo, useState } from "react";

import { useLocation, useNavigate } from "react-router-dom";

import { searchFlights } from "../../api/flightApi";

import FlightCard from "../../components/flight/FlightCard";
import FlightFilter from "../../components/flight/FlightFilter";
import SortBar from "../../components/flight/SortBar";
import FlightSkeleton from "../../components/flight/FlightSkeleton";
import SearchBox from "../../components/flight/SearchBox";

import "../../styles/flightSearch.css";

function getPrice(flight, cabin = "ECONOMY") {
  switch (cabin) {
    case "BUSINESS":
      return Number(flight.businessPrice || 0);

    case "FIRST":
      return Number(flight.firstPrice || 0);

    case "PREMIUM_ECONOMY":
      return Number(flight.premiumEconomyPrice || 0);

    default:
      return Number(flight.economyPrice || 0);
  }
}

function parseDuration(minutes) {
  if (!minutes) return 0;

  return Number(minutes);
}

function FlightSearch() {
  const location = useLocation();

  const navigate = useNavigate();

  const [flights, setFlights] = useState([]);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState("");

  const [searchData, setSearchData] = useState(null);

  const [showSearch, setShowSearch] = useState(false);

  const [sort, setSort] = useState("recommended");

  const [filters, setFilters] = useState({
    maxPrice: 0,

    cabin: "",

    airline: "",
  });

  useEffect(() => {
    if (location.state) {
      setSearchData(location.state);

      fetchFlights(location.state);
    }
  }, [location.state]);

  const fetchFlights = async (data) => {
    try {
      setLoading(true);

      setError("");

      const response = await searchFlights(data);

      console.log("Flight API Response:", response.data);

      const result = response.data || [];

      console.log("First Flight:", result[0]);

      setFlights(result);

      const maxPrice = result.length
        ? Math.max(...result.map((flight) => getPrice(flight, "ECONOMY")))
        : 0;

      setFilters({
        maxPrice,

        cabin: "",

        airline: "",
      });
    } catch (error) {
      console.error("Flight Search Error:", error);

      setError("Unable to load flights");

      setFlights([]);
    } finally {
      setLoading(false);
    }
  };

  // Maximum price for slider

  const priceCeiling = useMemo(() => {
    if (!flights.length) return 0;

    return Math.max(
      ...flights.map((flight) => getPrice(flight, filters.cabin)),
    );
  }, [flights, filters.cabin]);

  // Airline list

  const airlines = useMemo(() => {
    const list = flights

      .map((flight) => flight.airlineName)

      .filter(Boolean);

    return [...new Set(list)];
  }, [flights]);

  // APPLY FILTERS

  const filteredFlights = useMemo(() => {
    let data = [...flights];

    // PRICE

    if (filters.maxPrice) {
      data = data.filter(
        (flight) =>
          getPrice(
            flight,

            filters.cabin,
          ) <= filters.maxPrice,
      );
    }

    // CABIN

    if (filters.cabin) {
      data = data.filter((flight) => {
        switch (filters.cabin) {
          case "ECONOMY":
            return flight.economyPrice;

          case "PREMIUM_ECONOMY":
            return flight.premiumEconomyPrice;

          case "BUSINESS":
            return flight.businessPrice;

          case "FIRST":
            return flight.firstPrice;

          default:
            return true;
        }
      });
    }

    // AIRLINE

    if (filters.airline) {
      data = data.filter((flight) => flight.airlineName === filters.airline);
    }

    // SORT

    switch (sort) {
      case "price":
        return [...data].sort(
          (a, b) => getPrice(a, filters.cabin) - getPrice(b, filters.cabin),
        );

      case "duration":
        return [...data].sort(
          (a, b) =>
            parseDuration(a.durationMinutes) - parseDuration(b.durationMinutes),
        );

      case "departure":
        return [...data].sort(
          (a, b) => new Date(a.departureTime) - new Date(b.departureTime),
        );

      default:
        return data;
    }
  }, [flights, filters, sort]);

  const handleNewSearch = (data) => {
    setSearchData(data);

    setShowSearch(false);

    fetchFlights(data);
  };

  const clearFilters = () => {
    setFilters({
      maxPrice: priceCeiling,

      cabin: "",

      airline: "",
    });
  };

  if (!searchData && !loading) {
    return (
      <div className="empty-box">
        <h3>Search flights first</h3>

        <button className="btn-primary" onClick={() => navigate("/")}>
          Go Home
        </button>
      </div>
    );
  }

  return (
    <div className="flight-search-page">
      {showSearch && (
        <div className="inline-search-bar">
          <SearchBox initialValues={searchData} onSearch={handleNewSearch} />
        </div>
      )}

      <div className="search-summary">
        <div>
          <h2>Available Flights</h2>

          <p>
            {searchData?.source}
            &nbsp; → &nbsp;
            {searchData?.destination}
            &nbsp; | &nbsp;
            {searchData?.departureDate}
          </p>
        </div>

        <button
          className="btn-secondary"
          onClick={() => setShowSearch((prev) => !prev)}
        >
          {showSearch ? "Close" : "Modify Search"}
        </button>
      </div>

      <div className="flight-main">
        <FlightFilter
          filters={filters}
          setFilters={setFilters}
          priceCeiling={priceCeiling}
          airlines={airlines}
          clearFilters={clearFilters}
        />

        <div className="flight-list">
          <SortBar
            count={filteredFlights.length}
            sort={sort}
            setSort={setSort}
          />

          {loading && <FlightSkeleton />}

          {error && <div className="empty-box">{error}</div>}

          {!loading && !error && filteredFlights.length === 0 && (
            <div className="empty-box">No flights found</div>
          )}

          {!loading &&
            !error &&
            filteredFlights.map((flight) => (
              <FlightCard
                key={flight.id}
                flight={flight}
                onSelect={() => navigate(`/flights/${flight.id}`)}
              />
            ))}
        </div>
      </div>
    </div>
  );
}

export default FlightSearch;
