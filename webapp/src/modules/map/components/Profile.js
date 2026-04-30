import React from "react";

export default function Profile({ data, setData, onCheck }) {
    const isPollutionSelected = data.allergyType === "pollution" || data.allergyType === "pollution et pollen";
    const isPollenSelected = data.allergyType === "pollen" || data.allergyType === "pollution et pollen";

    const updateField = (field, value) => {
        setData({ ...data, [field]: value });
    };

    return (
        <div className="container mt-4" style={{ maxWidth: "600px" }}>
            <div className="card shadow p-4">
                <h2 className="mb-4 text-center">Configuration du profil</h2>

                <div className="form-check form-switch mb-3">
                    <input 
                        className="form-check-input" 
                        type="checkbox" 
                        id="asthmaCheck"
                        checked={data.isAsthmatic}
                        onChange={e => updateField("isAsthmatic", e.target.checked)} 
                    />
                    <label className="form-check-label" htmlFor="asthmaCheck">
                        Êtes-vous asthmatique ?
                    </label>
                </div>

                <div className="mb-3">
                    <label className="form-label">Type d'allergie :</label>
                    <select 
                        className="form-select" 
                        value={data.allergyType}
                        onChange={e => updateField("allergyType", e.target.value)}
                    >
                        <option value="pollen">Pollen</option>
                        <option value="pollution">Pollution</option>
                        <option value="pollution et pollen">Pollution et Pollen</option>
                    </select>
                </div>

                {isPollutionSelected && (
                    <div className="mb-3 p-2 border rounded bg-light">
                        <label className="form-label">Sensibilité pollution :</label>
                        <select 
                            className="form-select" 
                            value={data.pollutionLevel}
                            onChange={e => updateField("pollutionLevel", e.target.value)}
                        >
                            <option value="leger">Légère</option>
                            <option value="moyenne">Moyenne</option>
                            <option value="severe">Sévère</option>
                        </select>
                    </div>
                )}

                {isPollenSelected && (
                    <div className="mb-3 p-2 border rounded bg-light">
                        <label className="form-label">Sensibilité pollen :</label>
                        <select 
                            className="form-select" 
                            value={data.pollenLevel}
                            onChange={e => updateField("pollenLevel", e.target.value)}
                        >
                            <option value="faible">Faible</option>
                            <option value="modéré">Modérée</option>
                            <option value="forte">Forte</option>
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