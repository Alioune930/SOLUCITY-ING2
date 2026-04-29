import React, { useState, useEffect } from "react";
import { MapContainer, TileLayer, GeoJSON } from "react-leaflet";
import honfleurContours from "../../../data/Honfleur-contours.json";
import { fetchSensitiveZones } from "../api/mapAPI";
import Profile from "./Profile"; 
import "leaflet/dist/leaflet.css";

export default function SensitiveMap() {
    const [profile, setProfile] = useState(null);
    const [zones, setZones] = useState(null);
    const [formData, setFormData] = useState({
        isAsthmatic: false,
        allergyType: 'pollen', 
        pollutionLevel: 'MODERE',
        pollenLevel: 'MOYENNE'
    });

    useEffect(() => {
        if (profile) {
            fetchSensitiveZones(profile)
                .then(res => setZones(res))
                .catch(err => console.log("Erreur:", err));
        }
    }, [profile]);
    if (!profile) {
        return (
            <Profile 
                data={formData} 
                setData={setFormData} 
                onCheck={() => setProfile(formData)} 
            />
        );
    }
    return (
        <div style={{ height: "90vh", width: "100%" }}>
            <div className="bg-dark text-white p-2 d-flex justify-content-between px-4">
                <span>Carte Honfleur - Analyse personnalisée</span>
                <button className="btn btn-sm btn-light" onClick={() => setProfile(null)}>
                    Modifier mon profil
                </button>
            </div>

    <MapContainer center={[49.4194, 0.2329]} zoom={13} style={{ height: "100%" }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                
        {honfleurContours && (
            <GeoJSON data={honfleurContours} style={{ color: "#333", weight: 2, fillOpacity: 0.1 }} />
         )}

        {zones && (
            <GeoJSON 
            key={JSON.stringify(zones)} 
            data={zones} 
            style={(f) => ({
            fillColor: f.properties.couleur || "red",
            fillOpacity: 0.5,
            weight: 1
            })}
             />
                )}
            </MapContainer>
        </div>
    );
}