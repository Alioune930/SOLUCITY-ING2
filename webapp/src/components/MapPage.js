import React, { useState } from "react";
import ClimateMap from "./ClimateMap";
import RoadMap from "./RoadMap";

export default function MapPage() {
  const [mode, setMode] = useState("ClimateMap");

  return (
    <div style={{ height: "100vh", width: "100%" }}>
      {/* Switch entre les modes */}
      <div style={{ padding: "10px", background: "#f0f0f0" }}>
        <select value={mode} onChange={e => setMode(e.target.value)}>
          <option value="ClimateMap">ClimateMap</option>
          <option value="RoadMap">RoadMap</option>
        </select>
      </div>

      {/* Affichage du mode sélectionné */}
      {mode === "ClimateMap" && <ClimateMap />}
      {mode === "RoadMap" && <RoadMap />}
    </div>
  );
}
