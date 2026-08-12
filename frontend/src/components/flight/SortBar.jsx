import React from "react";

function SortBar({ count, sort, setSort }) {
  return (
    <div className="sort-bar">
      <h5>{count} flight{count === 1 ? "" : "s"} found</h5>

      <select value={sort} onChange={(e) => setSort(e.target.value)}>
        <option value="recommended">Recommended</option>
        <option value="price">Lowest price</option>
        <option value="duration">Shortest duration</option>
        <option value="departure">Earliest departure</option>
      </select>
    </div>
  );
}

export default SortBar;