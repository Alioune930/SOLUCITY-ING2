import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, Polygon } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import L from "leaflet";

import honfleurContours from "../../../data/Honfleur-contours.json";
import {
    fetchTronconsCarte,
    fetchCongestionsCarte,
    fetchVoiesByTroncon,
    simulerRegulation,
    appliquerRegulation
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
    const [voies, setVoies] = useState([]);
    const [loadingVoies, setLoadingVoies] = useState(false);
    const [errorVoies, setErrorVoies] = useState(null);
    const [voieSelectionnee, setVoieSelectionnee] = useState(null);
    const [simulation, setSimulation] = useState(null);
    const [loadingSimulation, setLoadingSimulation] = useState(false);
    const [errorSimulation, setErrorSimulation] = useState(null);
    const [applicationEnCours, setApplicationEnCours] = useState(false);
    const [messageApplication, setMessageApplication] = useState(null);
    const [erreurApplication, setErreurApplication] = useState(null);
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
    useEffect(() => {
        setVoieSelectionnee(null);
        setSimulation(null);
        setErrorSimulation(null);

        if (!tronconSelectionne) {
            setVoies([]);
            return;
        }

        async function chargerVoies() {
            try {
                setLoadingVoies(true);
                setErrorVoies(null);

                const voiesData = await fetchVoiesByTroncon(
                    tronconSelectionne.id
                );

                setVoies(voiesData);
            } catch (err) {
                console.error(
                    "Erreur lors du chargement des voies :",
                    err
                );

                setErrorVoies(
                    "Impossible de charger les voies de ce tronçon."
                );
                setVoies([]);
            } finally {
                setLoadingVoies(false);
            }
        }

        chargerVoies();
    }, [tronconSelectionne]);

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

    const lancerSimulation = async (typeAction) => {
        if (!tronconSelectionne || !voieSelectionnee) {
            return;
        }

        try {
            setLoadingSimulation(true);
            setErrorSimulation(null);
            setSimulation(null);

            const resultat = await simulerRegulation(
                tronconSelectionne.id,
                voieSelectionnee.id,
                typeAction
            );
            setSimulation({
                ...resultat,
                typeAction,
            });

        } catch (err) {
            console.error(
                "Erreur lors de la simulation :",
                err
            );

            setErrorSimulation(
                "Impossible de lancer la simulation."
            );
        } finally {
            setLoadingSimulation(false);
        }
    };
    const appliquerLaRegulation = async () => {
        if (!tronconSelectionne || !voieSelectionnee || !simulation) {
            return;
        }

        setApplicationEnCours(true);
        setMessageApplication(null);
        setErreurApplication(null);

        try {
            await appliquerRegulation(
                tronconSelectionne.id,
                voieSelectionnee.id,
                simulation.typeAction
            );

            // Actualiser les voies du tronçon
            const voiesActualisees = await fetchVoiesByTroncon(
                tronconSelectionne.id
            );

            setVoies(voiesActualisees);

            const voieActualisee = voiesActualisees.find(
                (voie) => voie.id === voieSelectionnee.id
            );

            setVoieSelectionnee(voieActualisee || null);

            // Actualiser les données de la carte
            const [tronconsActualises, congestionsActualisees] =
                await Promise.all([
                    fetchTronconsCarte(),
                    fetchCongestionsCarte(),
                ]);

            setTroncons(tronconsActualises);
            setCongestions(congestionsActualisees);

            const tronconActualise = tronconsActualises.find(
                (troncon) => troncon.id === tronconSelectionne.id
            );

            if (tronconActualise) {
                const congestionActualisee =
                    congestionsActualisees[String(tronconActualise.id)];

                setTronconSelectionne({
                    ...tronconActualise,
                    congestion: congestionActualisee?.niveau || "INCONNUE",
                    tauxOccupation: congestionActualisee?.tauxOccupation ?? null,
                    vitesseMoyenne: congestionActualisee?.vitesseMoyenne ?? null,
                });
            }
            setSimulation(null);

            setMessageApplication("Régulation appliquée avec succès.");

        } catch (error) {
            setErreurApplication(error.message);
        } finally {
            setApplicationEnCours(false);
        }
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

                    <hr />

                    <strong>Voies du tronçon</strong>

                    {loadingVoies && <p>Chargement des voies...</p>}

                    {errorVoies && <p>{errorVoies}</p>}

                    {!loadingVoies && !errorVoies && voies.length === 0 && (
                        <p>Aucune voie trouvée.</p>
                    )}

                    {!loadingVoies && !errorVoies && voies.length > 0 && (
                        <ul>
                            {voies.map((voie) => (
                                <li key={voie.id}>
                                    {voie.nom} — {voie.statut}
                                </li>
                            ))}
                        </ul>




                    )}
                    {voies.length > 0 && (
                        <div>
                            <h4>Choisir une voie</h4>

                            <select
                                value={voieSelectionnee?.id || ""}
                                onChange={(e) => {
                                    const voie = voies.find(
                                        (v) => v.id === Number(e.target.value)
                                    );

                                    setVoieSelectionnee(voie);
                                    setSimulation(null);
                                }}
                            >
                                <option value="">Sélectionner une voie</option>

                                {voies.map((voie) => (
                                    <option key={voie.id} value={voie.id}>
                                        {voie.nom} — {voie.statut}
                                    </option>
                                ))}
                            </select>
                        </div>
                    )}
                    {voieSelectionnee && (
                        <div>
                            <h4>Simuler une régulation</h4>

                            <button
                                onClick={() => lancerSimulation("OUVRIR_VOIE")}
                                disabled={
                                    loadingSimulation ||
                                    voieSelectionnee.statut === "OUVERTE"
                                }
                            >
                                Ouvrir la voie
                            </button>

                            <button
                                onClick={() => lancerSimulation("FERMER_VOIE")}
                                disabled={
                                    loadingSimulation ||
                                    voieSelectionnee.statut === "FERMEE" ||
                                    voies.filter((voie) => voie.statut === "OUVERTE").length <= 1
                                }
                            >
                                Fermer la voie
                            </button>
                        </div>
                    )}
                    {loadingSimulation && (
                        <p>Simulation en cours...</p>
                    )}

                    {errorSimulation && (
                        <p style={{ color: "red" }}>
                            {errorSimulation}
                        </p>
                    )}

                    {simulation && (
                        <div>
                            <h4>Résultat de la simulation</h4>

                            <p>
                                <strong>Occupation avant :</strong>{" "}
                                {simulation.occupationAvant} %
                            </p>

                            <p>
                                <strong>Occupation estimée après :</strong>{" "}
                                {simulation.occupationApres} %
                            </p>

                            <p>
                                <strong>Niveau estimé après :</strong>{" "}
                                {simulation.niveauEstimeApres}
                            </p>
                        </div>
                    )}

                    {loadingSimulation && (
                        <p>Simulation en cours...</p>
                    )}

                    {errorSimulation && (
                        <p style={{ color: "red" }}>
                            {errorSimulation}
                        </p>
                    )}

                    {simulation && (
                        <div
                            style={{
                                marginTop: "12px",
                                padding: "10px",
                                background: "#f3f3f3",
                                borderRadius: "4px",
                            }}
                        >
                            <strong>Résultat de la simulation</strong>

                            <p>
                                Occupation avant :{" "}
                                {simulation.occupationAvant} %
                            </p>

                            <p>
                                Occupation après :{" "}
                                {simulation.occupationApres.toFixed(1)} %
                            </p>

                            <p>
                                Niveau estimé après :{" "}
                                {simulation.niveauEstimeApres}
                            </p>
                            <button
                                onClick={appliquerLaRegulation}
                                disabled={applicationEnCours}
                            >
                                {applicationEnCours
                                    ? "Application en cours..."
                                    : "Appliquer cette régulation"}
                            </button>
                        </div>
                    )}
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


