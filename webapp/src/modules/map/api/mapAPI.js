import {GET_DonneesPollen, GET_ZONES} from "../../../constants/back";

export async function fetchZones() {
    const response = await fetch(GET_ZONES);
    if (!response.ok) {
        throw new Error('Erreur lors du chargement des zones');
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

export async function fetchTronconsCarte() {
    const response = await fetch("/api/trafic/troncons/carte");

    if (!response.ok) {
        throw new Error("Erreur lors du chargement des tronçons");
    }

    return response.json();
}

export async function fetchCongestionsCarte() {
    const response = await fetch("/api/trafic/congestions/carte");

    if (!response.ok) {
        throw new Error("Erreur lors du chargement des congestions");
    }

    return response.json();
}
