import React from "react";

export default function Profile({ data, setData, onCheck }) {
    return (
        <div className="container mt-4" style={{ maxWidth: "600px" }}>
            <div className="card shadow p-4">
                <h2 className="mb-4 text-center">Configuration du profil</h2>

                <div className="form-check form-switch mb-3">
                    <input 
                        className="form-check-input" 
                        type="checkbox" 
                        onChange={e => setData({...data, isAsthmatic: e.target.checked})} 
                    />
                    <label className="form-check-label">Êtes-vous asthmatique ?</label>
                </div>

                <div className="mb-3">
                    <label className="form-label">Type d'allergie :</label>
                    <select 
                        className="form-select" 
                        value={data.allergyType}
                        onChange={e => setData({...data, allergyType: e.target.value})}
                    >
                        <option value="pollen">Pollen</option>
                        <option value="pollution">Pollution</option>
                        <option value="both">Pollution et Pollen</option>
                    </select>
                </div>

                {/* Pollution */}
                {(data.allergyType === "pollution" || data.allergyType === "both") && (
                    <div className="mb-3 p-2 border rounded bg-light">
                        <label className="form-label">Sensibilité pollution :</label>
                        <select className="form-select" onChange={e => setData({...data, pollutionLevel: e.target.value})}>
                            <option value="LEGER">Légère</option>
                            <option value="MODERE">Modérée</option>
                            <option value="SEVERE">Sévère</option>
                        </select>
                    </div>
                )}

                {/* Pollen */}
                {(data.allergyType === "pollen" || data.allergyType === "both") && (
                    <div className="mb-3 p-2 border rounded bg-light">
                        <label className="form-label">Sensibilité pollen :</label>
                        <select className="form-select" onChange={e => setData({...data, pollenLevel: e.target.value})}>
                            <option value="FAIBLE">Faible</option>
                            <option value="MOYENNE">Moyenne</option>
                            <option value="FORTE">Forte</option>
                        </select>
                    </div>
                )}

                <button className="btn btn-primary w-100 mt-3" onClick={onCheck}>
                    Visualiser la carte
                </button>
            </div>
        </div>
    );
}