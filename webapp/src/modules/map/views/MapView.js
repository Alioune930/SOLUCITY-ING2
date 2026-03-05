import React, { useState } from "react";
import ClimateMap from "../components/ClimateMap";
import RoadMap from "../components/RoadMap";

const MAP_MODES = [
    { value: "ClimateMap", label: "Qualité de l'air" },
    { value: "RoadMap", label: "Réseau routier" },
];

export default function MapView() {
    const [mode, setMode] = useState("ClimateMap");

    function handleChange(e) {
        setMode(e.target.value);
    }

    function renderOption(item) {
        return (
            <option key={item.value} value={item.value}>
                {item.label}
            </option>
        );
    }

    return (
        <div style={{ height: "100vh", width: "100%", display: "flex", flexDirection: "column" }}>
            <div style={{ padding: "10px", background: "#f0f0f0" }}>
                <select value={mode} onChange={handleChange}>
                    {MAP_MODES.map(renderOption)}
                </select>
            </div>

            <h1 style={{ textAlign: "center", margin: "10px 0", fontSize: "20px", fontWeight: "bold" }}>
                Honfleur – Real-Time Environmental Indicators
            </h1>

            <div style={{ flexGrow: 1 }}>
                {mode === "ClimateMap" && <ClimateMap />}
                {mode === "RoadMap" && <RoadMap />}
            </div>
        </div>
    );
}