import React, { useState } from "react";

function AirportSearch({ value, onChange, placeholder }) {
  const [show, setShow] = useState(false);

  const airports = [
    {
      city: "Delhi",
      code: "DEL",
    },

    {
      city: "Mumbai",
      code: "BOM",
    },

    {
      city: "Bangalore",
      code: "BLR",
    },

    {
      city: "Kolkata",
      code: "CCU",
    },
  ];

  const filtered = airports.filter((airport) =>
    airport.city.toLowerCase().includes(value.toLowerCase()),
  );

  return (
    <div className="airport-search">
      <input
        value={value}
        placeholder={placeholder}
        onFocus={() => setShow(true)}
        onChange={(e) => onChange(e.target.value)}
      />

      {show && value && (
        <div className="airport-dropdown">
          {filtered.map((airport) => (
            <div
              className="airport-item"
              key={airport.code}
              onClick={() => {
                onChange(airport.code);

                setShow(false);
              }}
            >
              <b>{airport.city}</b>

              <span>({airport.code})</span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AirportSearch;
