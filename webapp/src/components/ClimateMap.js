import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import honfleurContours from "../data/Honfleur-contours.json";

const ClimateMap = () => {
  const [zonesGeoJSON, setZonesGeoJSON] = useState(null);

  const center = [49.4194, 0.2329];

  useEffect(() => {
    fetch("http://172.31.249.83:8082/zones")
      .then(res => res.json())
      .then(data => setZonesGeoJSON(data))
      .catch(err => console.error("Erreur chargement GeoJSON :", err));
  }, []);

  const zoneStyle = {
    color: "#3388ff",
    weight: 2,
    fillOpacity: 0.1
  };

  const honfleurBoundary = honfleurContours.features[0].geometry.coordinates[0].map(
    c => [c[1], c[0]]
  );

  // Fonction pour gérer le clic sur une zone et afficher un popup
  const onEachZone = (feature, layer) => {
    layer.on("click", (e) => {
      const zoneId = feature.id !== undefined ? feature.id : "Zone inconnue";
      layer.bindPopup(`Vous êtes dans la zone : ${zoneId}`).openPopup(e.latlng);
    });
  };

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

      {zonesGeoJSON && (
        <GeoJSON
          data={zonesGeoJSON}
          style={zoneStyle}
          onEachFeature={onEachZone} // <-- détection du clic et popup
        />
      )}
    </MapContainer>
  );
};

export default ClimateMap;
