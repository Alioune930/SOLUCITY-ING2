import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import honfleurContours from "../../../data/Honfleur-contours.json";
import { fetchZones } from "../api/mapAPI";
import MapLegend from "./MapLegend";

export default function ClimateMap() {
    const [zones, setZones] = useState(null);
    const center = [49.4194, 0.2329];

    useEffect(function() {
        console.log("Début du fetch Zones");

        fetchZones()
            .then(function(data) {
                console.log("Zones reçues :", data);
                setZones(data);
            })
            .catch(function(err) {
                console.error("Erreur chargement zones :", err);
            });
    }, []);

    const zoneStyle = function(feature) {
        return {
            color: feature.properties.couleur,
            weight: 2,
            fillColor: feature.properties.couleur,
            fillOpacity: 0.5
        };
    };

    const onEachZone = function(feature, layer) {
        const { libelle_pollution, score_pollution } = feature.properties;

        layer.bindTooltip(`Zone ${feature.id} – Pollution : ${libelle_pollution}`, {
            sticky: true,
        });

        layer.on("click", function() {
            layer.bindPopup(
                '<div style="font-size:14px">' +
                "<strong>Zone " + feature.id + "</strong><br/>" +
                "<b>Pollution :</b> " + libelle_pollution + "<br/>" +
                "<b>Score :</b> " + score_pollution +
                "</div>"
            ).openPopup();
        });
    };

    const boundary = honfleurContours.features[0].geometry.coordinates[0].map(function([lng, lat]) {
        return [lat, lng];
    });

    return (
        <MapContainer center={center} zoom={14} style={{ height: "90vh" }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="© OpenStreetMap"
            />
            <Polygon
                positions={boundary}
                pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
            />
            {zones && (
                <GeoJSON data={zones} style={zoneStyle} onEachFeature={onEachZone} />
            )}
            <MapLegend />
        </MapContainer>
    );
}