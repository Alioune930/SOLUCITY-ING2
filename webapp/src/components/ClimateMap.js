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

  const zoneStyle = (feature) => ({
    color: feature.properties.couleur,
    weight: 2,
    fillColor: feature.properties.couleur,
    fillOpacity: 0.5
  });

  const onEachZone = (feature, layer) => {
    const { libelle_pollution, score_pollution } = feature.properties;


    layer.bindTooltip(
      `Zone ${feature.id} – Pollution : ${libelle_pollution}`,
      { sticky: true }
    );

    layer.on("click", () => {
      layer.bindPopup(`
        <div style="font-size:14px">
          <strong>Zone ${feature.id}</strong><br/>
          <b>Pollution :</b> ${libelle_pollution}<br/>
          <b>Score :</b> ${score_pollution}
        </div>
      `).openPopup();
    });
  };

  const honfleurBoundary =
    honfleurContours.features[0].geometry.coordinates[0].map(
      c => [c[1], c[0]]
    );

  return (
    <MapContainer center={center} zoom={14} style={{ height: "90vh" }}>
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution="© OpenStreetMap"
      />

      {/* Contour ville */}
      <Polygon
        positions={honfleurBoundary}
        pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
      />

      {/* Zones pollution */}
      {zonesGeoJSON && (
        <GeoJSON
          data={zonesGeoJSON}
          style={zoneStyle}
          onEachFeature={onEachZone}
        />
      )}
    </MapContainer>
  );
};

export default ClimateMap;
