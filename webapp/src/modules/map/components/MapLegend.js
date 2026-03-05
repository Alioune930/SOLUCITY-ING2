import { useEffect } from "react";
import { useMap } from "react-leaflet";
import L from "leaflet";

const LEGEND_ITEMS = [
    { color: "#006400", label: "0–20 Très bon" },
    { color: "#008000", label: "21–40 Bon" },
    { color: "#FFFF00", label: "41–60 Moyen" },
    { color: "#FFA500", label: "61–75 Dégradé" },
    { color: "#FF0000", label: "76–90 Mauvais" },
    { color: "#800080", label: "91–100 Très mauvais" },
    { color: "#654321", label: ">100 Extrême" },
];

const POLLEN_ITEMS = [
    { color: "#66BB6A", label: "Faible (≤20)" },
    { color: "#FFFF00", label: "Modéré (≤50)" },
    { color: "#FFA500", label: "Élevé (≤75)" },
    { color: "#FF0000", label: "Très élevé (>75)" },
];

export default function MapLegend() {
    const map = useMap();

    useEffect(function() {
        const legend = L.control({ position: "topright" });

        legend.onAdd = function() {
            const div = L.DomUtil.create("div");
            div.style.background = "white";
            div.style.padding = "8px";
            div.style.borderRadius = "6px";
            div.style.boxShadow = "0 0 6px rgba(0,0,0,0.3)";
            div.style.fontSize = "12px";
            div.style.lineHeight = "20px";

            const pollutionRows = LEGEND_ITEMS.map(function(item) {
                return '<div><i style="background:' + item.color +
                    ';width:12px;height:12px;display:inline-block;margin-right:5px"></i>' +
                    item.label + '</div>';
            }).join("");

            const pollenRows = POLLEN_ITEMS.map(function(item) {
                return '<div><i style="background:' + item.color +
                    ';width:12px;height:12px;display:inline-block;margin-right:5px"></i>' +
                    item.label + '</div>';
            }).join("");

            div.innerHTML = "<strong>Qualité de l'air</strong><br/>" + pollutionRows +
                "<br/><strong>Pollen</strong><br/>" + pollenRows;
            return div;
        };

        legend.addTo(map);
        return function() { legend.remove(); };
    }, [map]);

    return null;
}