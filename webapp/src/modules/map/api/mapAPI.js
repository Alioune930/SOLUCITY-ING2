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

export async function simulerRegulation(
    tronconId,
    voieId,
    typeAction
) {
    const response = await fetch(
        `/api/trafic/regulations/simuler?tronconId=${tronconId}&voieId=${voieId}&typeAction=${typeAction}`,
        {
            method: "POST",
        }
    );

    if (!response.ok) {
        throw new Error("Erreur lors de la simulation");
    }

    return response.json();
}

export async function fetchVoiesByTroncon(tronconId) {
    const response = await fetch(
        `/api/trafic/voies/troncon/${tronconId}`
    );

    if (!response.ok) {
        throw new Error("Erreur lors du chargement des voies");
    }

    return response.json();
}

export async function appliquerRegulation(
    tronconId,
    voieId,
    typeAction
) {
    const response = await fetch(
        `/api/trafic/regulations/appliquer?tronconId=${tronconId}&voieId=${voieId}&typeAction=${typeAction}`,
        {
            method: "POST",
        }
    );

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || "Erreur lors de l'application de la régulation");
    }

    return response.text();
}