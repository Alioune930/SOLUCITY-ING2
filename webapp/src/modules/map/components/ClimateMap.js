import React, { useEffect, useState } from "react";
import {MapContainer, TileLayer, GeoJSON, Polygon, CircleMarker} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import honfleurContours from "../../../data/Honfleur-contours.json";
import {fetchPollen, fetchZones} from "../api/mapAPI";
import MapLegend from "./MapLegend";
import {getPollenForZone, pointInPolygon} from "../utils/mapUtils";

export default function ClimateMap() {
    const [zones, setZones] = useState(null);
    const [pollens, setPollens] = useState(null);
    const [pollenPoints, setPollenPoints] = useState([]);
    const center = [49.4194, 0.2329];

    useEffect(function() {
        console.log("Début du fetch Zones");

        fetchZones()
            .then(function(data) {
                console.log("Zones reçues :", data);
                setZones(data);
            })
            .catch(function(err) {
                console.error("Erreur chargement zones :", err);
            });

        fetchPollen()
            .then(function(data) {
                setPollens(data);
            })
            .catch(function(err) {
                console.error("Erreur chargement pollen :", err);
            });

    }, []);


    useEffect(function() {
        if (!pollens) {
            return;
        }
        const points = [];
        pollens.features.forEach(function(feature) {
            //const polygon = feature.geometry.coordinates[0][0];
            const concentration = feature.properties.concentration;
            const color = feature.properties.codeCouleur;
            const libelle = feature.properties.libelle;

            const numberOfPoints = Math.floor(concentration / 5);

            feature.geometry.coordinates.forEach(function (polygonGroup) {
                polygonGroup.forEach(function (polygon) {

                    const longs = polygon.map(function (p) {
                        return p[0];
                    });
                    const lats = polygon.map(function (p) {
                        return p[1];
                    });
                    const minLong = Math.min.apply(null, longs);
                    const maxLong = Math.max.apply(null, longs);
                    const minLat = Math.min.apply(null, lats);
                    const maxLat = Math.max.apply(null, lats);

                    let generated = 0;
                    while (generated < numberOfPoints) {
                        const long = minLong + Math.random() * (maxLong - minLong);
                        const lat = minLat + Math.random() * (maxLat - minLat);

                        if (pointInPolygon([long, lat], polygon)) {
                            points.push({
                                position: [lat, long],
                                color: color,
                                libelle: libelle,
                                concentration: concentration
                            });
                            generated++;
                        }
                    }
                });
            });
        });
        setPollenPoints(points);
    }, [pollens]);


    const zoneStyle = function(feature) {
        return {
            color: feature.properties.couleur,
            weight: 2,
            fillColor: feature.properties.couleur,
            fillOpacity: 0.3
        };
    };

    const onEachZone = function(feature, layer) {
        const {libelle_pollution, score_pollution} = feature.properties;

        const pollen = getPollenForZone(feature.id, pollens);

        layer.bindTooltip(`Zone ${feature.id} <br/> Pollution : ${libelle_pollution} <br/> Pollen : ${pollen.libelle}`, {
            sticky: true,
        });

        layer.on("click", function() {
            layer.bindPopup(
                '<div style="font-size:14px">' +
                "<strong>Zone " + feature.id + "</strong><br/>" +
                "<b>Pollution :</b> " + libelle_pollution + "<br/>" +
                "<b>Score Pollution :</b> " + score_pollution + "<br/>" +
                "<b>Pollen :</b> " + pollen.libelle + "<br/>" +
                "<b>Concentration Pollen :</b> " + pollen.concentration +
                "</div>"
            ).openPopup();
        });
    };

    const boundary = honfleurContours.features[0].geometry.coordinates[0].map(function([lng, lat]) {
        return [lat, lng];
    });

    return (
        <MapContainer center={center} zoom={14} style={{ height: "90vh" }}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution="© OpenStreetMap"
            />
            <Polygon
                positions={boundary}
                pathOptions={{ color: "black", weight: 3, fillOpacity: 0 }}
            />
            {zones && (
                <GeoJSON
                    data={zones}
                    style={zoneStyle}
                    onEachFeature={onEachZone} />
            )}

            {pollenPoints.map(function(point, index) {
                return (
                    <CircleMarker
                        key={index}
                        center={point.position}
                        radius={3}
                        pathOptions={{
                            color: "#000000",
                            weight: 1,
                            //color: point.color,
                            fillColor: point.color,
                            fillOpacity: 1
                        }}
                    />
                );
            })}

            <MapLegend />
        </MapContainer>
    );
}