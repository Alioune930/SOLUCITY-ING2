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
                        congestions[String(troncon.id)]?.niveau || "INCONNUE",

                    tauxOccupation:
                        congestions[String(troncon.id)]?.tauxOccupation ?? null,

                    vitesseMoyenne:
                        congestions[String(troncon.id)]?.vitesseMoyenne ?? null,
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
    const [tronconSelectionne, setTronconSelectionne] = useState(null);
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

        const tauxOccupation =
            feature.properties?.tauxOccupation;

        layer.bindTooltip(
            `<strong>${name}</strong><br/>` +
            `Congestion : ${congestion}<br/>` +
            `Longueur : ${longueur ?? "N/A"} m`
        );

        layer.on("click", () => {

            if (
                congestion === "MOYEN" ||
                congestion === "SATURE"
            ) {
                setTronconSelectionne(feature.properties);
            } else {
                setTronconSelectionne(null);
            }

        });
    };

    return (
        <div style={{ height: "90vh", position: "relative" }}>

            {tronconSelectionne && (
                <div
                    style={{
                        position: "absolute",
                        zIndex: 1000,
                        top: "10px",
                        right: "10px",
                        background: "white",
                        padding: "15px",
                        borderRadius: "5px",
                        boxShadow: "0 2px 8px rgba(0,0,0,0.3)",
                        minWidth: "250px",
                    }}
                >
                    <strong>Tronçon sélectionné</strong>

                    <p>
                        <strong>Route :</strong>{" "}
                        {tronconSelectionne.nom}
                    </p>

                    <p>
                        <strong>Congestion :</strong>{" "}
                        {tronconSelectionne.congestion}
                    </p>

                    <p>
                        <strong>Taux d'occupation :</strong>{" "}
                        {tronconSelectionne.tauxOccupation ?? "N/A"} %
                    </p>

                    <p>
                        <strong>Longueur :</strong>{" "}
                        {tronconSelectionne.longueur ?? "N/A"} m
                    </p>

                    <button
                        onClick={() => setTronconSelectionne(null)}
                    >
                        Fermer
                    </button>
                </div>
            )}

            <MapContainer
                center={center}
                zoom={15}
                style={{ height: "100%", width: "100%" }}
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

        </div>
    );
}