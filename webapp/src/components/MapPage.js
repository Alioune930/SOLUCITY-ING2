import React, { useState } from "react";
import ClimateMap from "./ClimateMap";
import RoadMap from "./RoadMap";

export default function MapPage() {
  const [mode, setMode] = useState("ClimateMap");

  return (
    <div style={{ height: "100vh", width: "100%", display: "flex", flexDirection: "column" }}>
      {/* Switch entre les modes */}
      <div style={{ padding: "10px", background: "#f0f0f0" }}>
        <select value={mode} onChange={e => setMode(e.target.value)}>
          <option value="ClimateMap">ClimateMap</option>
          <option value="RoadMap">RoadMap</option>
        </select>
      </div>

      {/* Titre */}
      <h1 style={{ textAlign: "center", margin: "10px 0", fontSize: "20px", fontWeight: "bold" }}>
        Honfleur – Real-Time Environmental Indicators
      </h1>

      {/* Affichage du mode sélectionné */}
      <div style={{ flexGrow: 1 }}>
        {mode === "ClimateMap" && <ClimateMap />}
        {mode === "RoadMap" && <RoadMap />}
      </div>
    </div>
  );
}
