import React, { useState, useEffect } from "react";
import { MapContainer, TileLayer, GeoJSON, CircleMarker } from "react-leaflet";
import honfleurContours from "../../../data/Honfleur-contours.json";
import { fetchSensitiveZones } from "../api/mapAPI";
import { pointInPolygon } from "../utils/mapUtils"; 
import Profile from "./Profile"; 
import MapLegend from "./MapLegend"; 
import "leaflet/dist/leaflet.css";

export default function SensitiveMap() {
    const [profile, setProfile] = useState(() => {
        const saved = localStorage.getItem("userProfile");
        return saved ? JSON.parse(saved) : null;
    });
    const [zones, setZones] = useState(null);
    const [pollenPoints, setPollenPoints] = useState([]);
    const [formData, setFormData] = useState({
        isAsthmatic: false,
        allergyType: 'pollen', 
        pollutionLevel: 'moyenne', 
        pollenLevel: 'modéré'
    });

    const handleSetProfile = (data) => {
        localStorage.setItem("userProfile", JSON.stringify(data));
        setProfile(data);
    };

    const handleReset = () => {
        localStorage.removeItem("userProfile");
        setProfile(null);
        setZones(null);
        setPollenPoints([]);
    };

    useEffect(() => {
        let interval;
        if (profile) {
            const loadData = () => {
                fetchSensitiveZones(profile)
                    .then(res => setZones(res))
                    .catch(err => console.error(err));
            };
            loadData();
            interval = setInterval(loadData, 3000);
        }
        return () => clearInterval(interval);
    }, [profile]);

    useEffect(() => {
        if (!zones || !zones.features) return;
        
        const generatedPoints = [];
        zones.features.forEach((feature) => {
            const props = feature.properties;
            const density = Math.floor((props.score_pollen_ajuste || 0) / 5);

            if (feature.geometry && feature.geometry.coordinates) {
                feature.geometry.coordinates.forEach((polygonGroup) => {
                    polygonGroup.forEach((polygon) => {
                        const longs = polygon.map(c => c[0]);
                        const lats = polygon.map(c => c[1]);
                        const minX = Math.min(...longs);
                        const maxX = Math.max(...longs);
                        const minY = Math.min(...lats);
                        const maxY = Math.max(...lats);

                        let added = 0;
                        while (added < density && added < 50) { 
                            const lng = minX + Math.random() * (maxX - minX);
                            const lat = minY + Math.random() * (maxY - minY);
                            if (pointInPolygon([lng, lat], polygon)) {
                                generatedPoints.push({
                                    position: [lat, lng],
                                    color: props.couleur_pollen || "#2ECC71"
                                });
                                added++;
                            }
                        }
                    });
                });
            }
        });
        setPollenPoints(generatedPoints);
    }, [zones]);

    if (!profile) {
        return <Profile data={formData} setData={setFormData} onCheck={() => handleSetProfile(formData)} />;
    }

    return (
        <div style={{ height: "100vh", width: "100%", display: "flex", flexDirection: "column" }}>
            <header className="bg-dark text-white p-3 d-flex justify-content-between align-items-center">
                <h1 className="h5 m-0">Analyse de vulnérabilité - Honfleur</h1>
                <button className="btn btn-outline-light btn-sm" onClick={handleReset}>
                    Modifier le profil
                </button>
            </header>

            <main style={{ flex: 1, position: "relative" }}>
                <MapContainer center={[49.4194, 0.2329]} zoom={13} style={{ height: "100%", width: "100%" }}>
                    <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                    <MapLegend />

                    {honfleurContours && (
                        <GeoJSON 
                            data={honfleurContours} 
                            style={{ color: "#2C3E50", weight: 3, fillOpacity: 0, interactive: false }} 
                        />
                    )}

                    {zones && (
                        <GeoJSON 
                            key={zones.features.length} 
                            data={zones} 
                            style={(f) => ({
                                fillColor: f.properties.couleur_pollution,
                                fillOpacity: 0.5,
                                color: "#222",
                                weight: 1,
                            })}
                            onEachFeature={(feature, layer) => {
                                const p = feature.properties;
                                const content = `
                                    <div style="padding: 2px;">
                                        <strong>Zone : ${p.idZone}</strong><br/>
                                        <hr style="margin: 4px 0;"/>
                                        <b>Risque Global :</b> ${p.score_risque_global.toFixed(1)}%<br/>
                                        <b>Pollution :</b> ${p.score_pollution_ajuste.toFixed(1)}%<br/>
                                        <b>Pollen :</b> ${p.score_pollen_ajuste.toFixed(1)}%
                                    </div>
                                `;
                                layer.bindTooltip(content, { sticky: true });
                            }}
                        />
                    )}

                    {pollenPoints.map((pt, index) => (
                        <CircleMarker
                            key={`p-${index}-${Math.random()}`}
                            center={pt.position}
                            radius={3}
                            pathOptions={{
                                fillColor: pt.color,
                                fillOpacity: 0.9,
                                color: "#000",
                                weight: 0.5
                            }}
                        />
                    ))}
                </MapContainer>
            </main>
        </div>
    );
}