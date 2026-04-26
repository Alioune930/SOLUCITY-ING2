import {GET_DonneesPollen, GET_ZONES, GET_SENSITIVE_ZONES} from "../../../constants/back";

export async function fetchZones() {
    const response = await fetch(GET_ZONES);
    if (!response.ok) {
        throw new Error('Erreur lors du chargement des zones');
    }
    return response.json();
}

export async function fetchSensitiveZones(profile) {
    const paramètres = new URLSearchParams({
        asthme: profile.isAsthmatic.toString(), 
        type: profile.allergyType,
        polluLvl: profile.pollutionLevel || "", 
        pollenLvl: profile.pollenLevel || ""
    });
    
    const response = await fetch(`${GET_SENSITIVE_ZONES}?${paramètres.toString()}`);
    
    if (!response.ok) {
        throw new Error("Erreur lors de la récupération des zones sensibles");
    }
    return response.json();
}

export async function fetchPollen() {
    const response = await fetch(GET_DonneesPollen);
    if (!response.ok) {
        throw new Error('Erreur lors du chargement des zones');
    }
    return response.json();
}
