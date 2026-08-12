import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function SearchBox() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    source: "",
    destination: "",
    departureDate: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,

      [e.target.name]: e.target.value,
    });
  };

  const handleSearch = () => {
    navigate("/flights", {
      state: formData,
    });
  };

  return (
    <div className="container mt-4">
      <div className="card shadow p-4">
        <h3 className="mb-4 text-center">Search Flights</h3>

        <div className="row">
          <div className="col-md-4 mb-3">
            <input
              className="form-control"
              name="source"
              placeholder="From (DEL)"
              value={formData.source}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-4 mb-3">
            <input
              className="form-control"
              name="destination"
              placeholder="To (BOM)"
              value={formData.destination}
              onChange={handleChange}
            />
          </div>

          <div className="col-md-4 mb-3">
            <input
              type="date"
              className="form-control"
              name="departureDate"
              value={formData.departureDate}
              onChange={handleChange}
            />
          </div>
        </div>

        <button className="btn btn-primary" onClick={handleSearch}>
          Search Flights
        </button>
      </div>
    </div>
  );
}

export default SearchBox;
