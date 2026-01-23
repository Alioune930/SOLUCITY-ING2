import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import honfleurContours from "../data/Honfleur-contours.json";

const Legend = () => {
  const map = useMap();

  useEffect(() => {
    const legend = L.control({ position: "bottomright" });

    legend.onAdd = () => {
      const div = L.DomUtil.create("div");

      div.style.background = "white";
      div.style.padding = "8px";
      div.style.borderRadius = "6px";
      div.style.boxShadow = "0 0 6px rgba(0,0,0,0.3)";
      div.style.fontSize = "12px";
      div.style.lineHeight = "18px";

      div.innerHTML = `
        <strong>Qualité de l’air</strong><br/>
        <i style="background:#006400;width:12px;height:12px;display:inline-block"></i> 0–20 Très bon<br/>
        <i style="background:#008000;width:12px;height:12px;display:inline-block"></i> 21–40 Bon<br/>
        <i style="background:#FFFF00;width:12px;height:12px;display:inline-block"></i> 41–60 Moyen<br/>
        <i style="background:#FFA500;width:12px;height:12px;display:inline-block"></i> 61–75 Dégradé<br/>
        <i style="background:#FF0000;width:12px;height:12px;display:inline-block"></i> 76–90 Mauvais<br/>
        <i style="background:#800080;width:12px;height:12px;display:inline-block"></i> 91–100 Très mauvais<br/>
        <i style="background:#654321;width:12px;height:12px;display:inline-block"></i> >100 Extrême
      `;

      return div;
    };

    legend.addTo(map);
    return () => legend.remove();
  }, [map]);

  return null;
};

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

      <Polygon
        positions={honfleurBoundary}
        pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
      />

      {zonesGeoJSON && (
        <GeoJSON
          data={zonesGeoJSON}
          style={zoneStyle}
          onEachFeature={onEachZone}
        />
      )}

      {/* Légende */}
      <Legend />
    </MapContainer>
  );
};

export default ClimateMap;
