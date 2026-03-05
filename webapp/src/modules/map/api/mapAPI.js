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

