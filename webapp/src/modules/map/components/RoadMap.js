import React from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";
import honfleurContours from "../../../data/Honfleur-contours.json";
import honfleurRoads from "../../../data/honfleur-roads.json";

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
    iconUrl: require("leaflet/dist/images/marker-icon.png"),
    shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});

const ROAD_TYPES = ["primary", "secondary", "tertiary", "trunk"];
const CONGESTION_LEVELS = ["fluide", "dense", "saturée"];
const ROAD_STATUSES = ["ouverte", "fermée", "travaux"];

function randomFrom(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function getRoadStyle() {
    const congestion = randomFrom(CONGESTION_LEVELS);
    const status = randomFrom(ROAD_STATUSES);

    const colorMap = { fluide: "green", dense: "orange", saturée: "red" };

    return {
        color: colorMap[congestion],
        weight: status === "fermée" ? 6 : 4,
        dashArray: status === "travaux" ? "6 4" : null,
    };
}

export default function RoadMap() {
    const center = [49.4194, 0.2329];

    const roadsFiltered = {
        type: "FeatureCollection",
        features: honfleurRoads.features.filter(
            (f) => ROAD_TYPES.includes(f.properties?.highway)
        ),
    };

    const boundary = honfleurContours.features[0].geometry.coordinates[0].map(
        ([lng, lat]) => [lat, lng]
    );

    const onEachRoad = (feature, layer) => {
        const congestion = randomFrom(CONGESTION_LEVELS);
        const status = randomFrom(ROAD_STATUSES);
        const name = feature.properties?.name || "Route inconnue";

        layer.bindTooltip(
            `<strong>${name}</strong><br/>État : ${status}<br/>Congestion : ${congestion}`
        );
    };

    return (
        <MapContainer center={center} zoom={15} style={{ height: "90vh" }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="© OpenStreetMap"
            />
            <Polygon
                positions={boundary}
                pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
            />
            <GeoJSON
                data={roadsFiltered}
                style={getRoadStyle}
                onEachFeature={onEachRoad}
            />
        </MapContainer>
    );
}