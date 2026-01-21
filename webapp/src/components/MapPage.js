import React, { useState } from 'react';
import {
  MapContainer,
  TileLayer,
  Rectangle,
  Polygon,
  GeoJSON,
  Tooltip
} from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import roads from '../data/honfleur-roads.json';
import honfleurGeoJSON from '../data/Honfleur-contours.json';
import L from 'leaflet';


delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

export default function MapPage() {
  const [mode, setMode] = useState('ClimateMap');
  const center = [49.4194, 0.2329];

  //le contour
  const honfleurBoundary = honfleurGeoJSON.features[0].geometry.coordinates[0].map(
    coord => [coord[1], coord[0]]
  );

  //si un point est dans un polygone
  const isPointInPolygon = (point, polygon) => {
    const [x, y] = point;
    let inside = false;
    for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
      const xi = polygon[i][0], yi = polygon[i][1];
      const xj = polygon[j][0], yj = polygon[j][1];
      const intersect =
        yi > y !== yj > y &&
        x < ((xj - xi) * (y - yi)) / (yj - yi) + xi;
      if (intersect) inside = !inside;
    }
    return inside;
  };

  // QUADRILLAGE
  const topLeft = [49.425, 0.229];
  const bottomRight = [49.415, 0.239];
  const rows = 30; 
  const cols = 30;
  const latStep = (topLeft[0] - bottomRight[0]) / rows;
  const lngStep = (bottomRight[1] - topLeft[1]) / cols;

  const grid = [];
  for (let i = 0; i < rows; i++) {
    for (let j = 0; j < cols; j++) {
      const sw = [topLeft[0] - latStep * (i + 1), topLeft[1] + lngStep * j];
      const ne = [topLeft[0] - latStep * i, topLeft[1] + lngStep * (j + 1)];
      const centerCell = [(sw[0] + ne[0]) / 2, (sw[1] + ne[1]) / 2];
      if (isPointInPolygon(centerCell, honfleurBoundary)) {
        grid.push({ bounds: [sw, ne], pollution: Math.floor(Math.random() * 100) });
      }
    }
  }

  const getColor = (value) => {
    if (value < 30) return 'green';
    if (value < 60) return 'orange';
    return 'red';
  };

  // Filtrage des routes 
  const filterMajorRoadsInCity = (roads, boundary) => {
    const majorTypes = ['primary', 'secondary', 'tertiary', 'trunk'];
    return {
      ...roads,
      features: roads.features.filter(f =>
        majorTypes.includes(f.properties.highway) &&
        f.geometry.coordinates.every(coord =>
          isPointInPolygon([coord[1], coord[0]], boundary)
        )
      )
    };
  };

  const roadsInCity = filterMajorRoadsInCity(roads, honfleurBoundary);

  // état des routes 
  const getRoadStyle = (feature) => {
    const congestion = feature.properties.congestion ||
                       ['fluide', 'dense', 'jammed'][Math.floor(Math.random() * 3)];
    const status = feature.properties.status ||
                   ['ouvert', 'fermé', 'Travaux'][Math.floor(Math.random() * 3)];

    const baseColor = congestion === 'fluide' ? 'green' :
                      congestion === 'dense' ? 'orange' : 'red';
    const weight = status === 'fermé' ? 6 : 4;
    const dashArray = status === 'Travaux' ? '8 6' : null;

    return { color: baseColor, weight, dashArray };
  };

  return (
    <div style={{ height: '100vh', width: '100%' }}>
      {/* Sélecteur de mode */}
      <div style={{ padding: '10px', background: '#f0f0f0' }}>
        <label>Mode : </label>
        <select value={mode} onChange={e => setMode(e.target.value)}>
          <option value="ClimateMap">ClimateMap</option>
          <option value="RoadMap">RoadMap</option>
        </select>
      </div>

      <MapContainer center={center} zoom={15} style={{ height: '90vh' }}>
        {/* Fond de carte */}
        <TileLayer
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          attribution="© OpenStreetMap contributors"
        />

        {/* Contour de la ville */}
        <Polygon
          positions={honfleurBoundary}
          pathOptions={{ color: 'black', weight: 3, fillOpacity: 0 }}
        />

        {/* ClimateMap */}
        {mode === 'ClimateMap' &&
          grid.map((cell, index) => (
            <Rectangle
              key={index}
              bounds={cell.bounds}
              pathOptions={{ color: getColor(cell.pollution), weight: 1, fillOpacity: 0.4 }}
            >
              <Tooltip>
                Pollution : {cell.pollution}<br/>
                Qualité air : OK<br/>
                Confort climatique : Normal
              </Tooltip>
            </Rectangle>
          ))
        }

        {/* RoadMap */}
        {mode === 'RoadMap' &&
          <GeoJSON
            data={roadsInCity}
            style={getRoadStyle}
            onEachFeature={(feature, layer) => {
              const congestion = feature.properties.congestion ||
                                 ['fluide', 'dense', 'interrompu'][Math.floor(Math.random() * 3)];
              const status = feature.properties.status ||
                             ['ouvert', 'fermé', 'Travaux'][Math.floor(Math.random() * 3)];
              const name = feature.properties.name || 'Route inconnue';
              layer.bindTooltip(
                `<strong>${name}</strong><br/>État : ${status}<br/>Congestion : ${congestion}`
              );
            }}
          />
        }
      </MapContainer>
    </div>
  );
}
