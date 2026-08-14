import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

import honfleurContours from "../../../data/Honfleur-contours.json";
import {
    fetchTronconsCarte,
    fetchCongestionsCarte,
} from "../api/mapAPI";

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
    iconUrl: require("leaflet/dist/images/marker-icon.png"),
    shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
});

const COLOR_MAP = {
    FLUIDE: "green",
    MOYEN: "orange",
    SATURE: "red",
};

function getRoadStyle(feature) {
    const congestion = feature.properties?.congestion;

    return {
        color: COLOR_MAP[congestion] || "gray",
        weight: 4,
    };
}

function convertirEnGeoJSON(troncons, congestions) {
    return {
        type: "FeatureCollection",

        features: troncons
            .filter(
                (troncon) =>
                    troncon.coordonnees &&
                    troncon.coordonnees.length >= 2
            )
            .map((troncon) => ({
                type: "Feature",

                properties: {
                    id: troncon.id,
                    nom: troncon.nom || "Route inconnue",
                    longueur: troncon.longueur,

                    congestion:
                        congestions[String(troncon.id)] || "INCONNUE",
                },

                geometry: {
                    type: "LineString",

                    coordinates: troncon.coordonnees.map(
                        (coordonnee) => [
                            Number(coordonnee.longitude),
                            Number(coordonnee.latitude),
                        ]
                    ),
                },
            })),
    };
}

export default function RoadMap() {

    const center = [49.4194, 0.2329];

    const [troncons, setTroncons] = useState([]);
    const [congestions, setCongestions] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {

        async function chargerDonnees() {

            try {
                setLoading(true);

                const [tronconsData, congestionsData] =
                    await Promise.all([
                        fetchTronconsCarte(),
                        fetchCongestionsCarte(),
                    ]);

                setTroncons(tronconsData);
                setCongestions(congestionsData);

                setError(null);

            } catch (err) {

                console.error(
                    "Erreur lors du chargement de la carte :",
                    err
                );

                setError(
                    "Impossible de charger les données du réseau routier."
                );

            } finally {
                setLoading(false);
            }
        }

        chargerDonnees();

    }, []);

    const roadsGeoJSON =
        convertirEnGeoJSON(troncons, congestions);

    const boundary =
        honfleurContours.features[0].geometry.coordinates[0].map(
            ([lng, lat]) => [lat, lng]
        );

    const onEachRoad = (feature, layer) => {

        const name =
            feature.properties?.nom || "Route inconnue";

        const longueur =
            feature.properties?.longueur;

        const congestion =
            feature.properties?.congestion || "INCONNUE";

        layer.bindTooltip(
            `<strong>${name}</strong><br/>` +
            `Congestion : ${congestion}<br/>` +
            `Longueur : ${longueur ?? "N/A"} m`
        );
    };

    return (
        <MapContainer
            center={center}
            zoom={15}
            style={{ height: "90vh" }}
        >

            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="© OpenStreetMap"
            />

            <Polygon
                positions={boundary}
                pathOptions={{
                    color: "black",
                    weight: 3,
                    fillOpacity: 0,
                }}
            />

            {loading && (
                <div
                    style={{
                        position: "absolute",
                        zIndex: 1000,
                        top: "10px",
                        left: "10px",
                        background: "white",
                        padding: "10px",
                    }}
                >
                    Chargement du réseau routier...
                </div>
            )}

            {error && (
                <div
                    style={{
                        position: "absolute",
                        zIndex: 1000,
                        top: "10px",
                        left: "10px",
                        background: "white",
                        padding: "10px",
                    }}
                >
                    {error}
                </div>
            )}

            {!loading && !error && (
                <GeoJSON
                    key={JSON.stringify(congestions)}
                    data={roadsGeoJSON}
                    style={getRoadStyle}
                    onEachFeature={onEachRoad}
                />
            )}

        </MapContainer>
    );
}

// import React from "react";
// import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
// import "leaflet/dist/leaflet.css";
// import L from "leaflet";
// import honfleurContours from "../../../data/Honfleur-contours.json";
// import honfleurRoads from "../../../data/honfleur-roads.json";

// delete L.Icon.Default.prototype._getIconUrl;
// L.Icon.Default.mergeOptions({
//     iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
//     iconUrl: require("leaflet/dist/images/marker-icon.png"),
//     shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
// });

// const ROAD_TYPES = ["primary", "secondary", "tertiary", "trunk"];
// const CONGESTION_LEVELS = ["fluide", "dense", "saturée"];
// const ROAD_STATUSES = ["ouverte", "fermée", "travaux"];

// function randomFrom(arr) {
//     return arr[Math.floor(Math.random() * arr.length)];
// }

// function getRoadStyle() {
//     const congestion = randomFrom(CONGESTION_LEVELS);
//     const status = randomFrom(ROAD_STATUSES);

//     const colorMap = { fluide: "green", dense: "orange", saturée: "red" };

//     return {
//         color: colorMap[congestion],
//         weight: status === "fermée" ? 6 : 4,
//         dashArray: status === "travaux" ? "6 4" : null,
//     };
// }

// export default function RoadMap() {
//     const center = [49.4194, 0.2329];

//     const roadsFiltered = {
//         type: "FeatureCollection",
//         features: honfleurRoads.features.filter(
//             (f) => ROAD_TYPES.includes(f.properties?.highway)
//         ),
//     };

//     const boundary = honfleurContours.features[0].geometry.coordinates[0].map(
//         ([lng, lat]) => [lat, lng]
//     );

//     const onEachRoad = (feature, layer) => {
//         const congestion = randomFrom(CONGESTION_LEVELS);
//         const status = randomFrom(ROAD_STATUSES);
//         const name = feature.properties?.name || "Route inconnue";

//         layer.bindTooltip(
//             `<strong>${name}</strong><br/>État : ${status}<br/>Congestion : ${congestion}`
//         );
//     };

//     return (
//         <MapContainer center={center} zoom={15} style={{ height: "90vh" }}>
//             <TileLayer
//                 url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
//                 attribution="© OpenStreetMap"
//             />
//             <Polygon
//                 positions={boundary}
//                 pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
//             />
//             <GeoJSON
//                 data={roadsFiltered}
//                 style={getRoadStyle}
//                 onEachFeature={onEachRoad}
//             />
//         </MapContainer>
//     );
// }

