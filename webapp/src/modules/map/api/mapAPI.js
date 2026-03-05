import {GET_ZONES} from "../../../constants/back";

export async function fetchZones() {
    const response = await fetch(GET_ZONES);
    if (!response.ok) {
        throw new Error('Erreur lors du chargement des zones');
    }
    return response.json();
}

