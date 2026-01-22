import React from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import honfleurContours from "../data/Honfleur-contours.json";
import honfleurRoads from "../data/honfleur-roads.json";


delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
  iconUrl: require("leaflet/dist/images/marker-icon.png"),
  shadowUrl: require("leaflet/dist/images/marker-shadow.png")
});

const RoadMap = () => {
  const center = [49.4194, 0.2329];

  const roadTypesAllowed = ["primary", "secondary", "tertiary", "trunk"];
  const roadsFiltered = {
    type: "FeatureCollection",
    features: honfleurRoads.features.filter(
      f => roadTypesAllowed.includes(f.properties?.highway)
    )
  };

  const getRoadStyle = () => {
    const congestion = ["fluide", "dense", "saturée"][Math.floor(Math.random() * 3)];
    const status = ["ouverte", "fermée", "travaux"][Math.floor(Math.random() * 3)];

    let color = "green";
    if (congestion === "dense") color = "orange";
    if (congestion === "saturée") color = "red";

    return {
      color,
      weight: status === "fermée" ? 6 : 4,
      dashArray: status === "travaux" ? "6 4" : null
    };
  };

  const honfleurBoundary = honfleurContours.features[0].geometry.coordinates[0].map(
    c => [c[1], c[0]]
  );

  return (
    <MapContainer center={center} zoom={15} style={{ height: "90vh" }}>
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution="© OpenStreetMap"
      />

      <Polygon
        positions={honfleurBoundary}
        pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
      />

      <GeoJSON
        data={roadsFiltered}
        style={getRoadStyle}
        onEachFeature={(feature, layer) => {
          const congestion = ["fluide", "dense", "saturée"][Math.floor(Math.random() * 3)];
          const status = ["ouverte", "fermée", "travaux"][Math.floor(Math.random() * 3)];
          const name = feature.properties?.name || "Route inconnue";

          layer.bindTooltip(
            `<strong>${name}</strong><br/>État : ${status}<br/>Congestion : ${congestion}`
          );
        }}
      />
    </MapContainer>
  );
};

export default RoadMap;
