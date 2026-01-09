
CREATE TABLE capteur (
                         id_capteur SERIAL PRIMARY KEY,
                         id_zone INT REFERENCES zone(id_zone),
                         type_capteur VARCHAR(50),
                         latitude DOUBLE PRECISION,
                         longitude DOUBLE PRECISION,
                         actif BOOLEAN DEFAULT TRUE
);

-- Insertion des capteurs de POLLUTION (90)
INSERT INTO capteur (id_zone, type_capteur, latitude, longitude, actif)
WITH zones_classees AS (
    SELECT id_zone, type_zone, centre_lat, centre_lon,
           ROW_NUMBER() OVER(PARTITION BY type_zone ORDER BY RANDOM()) as rang
    FROM zone
)
SELECT id_zone, 'Pollution', centre_lat, centre_lon, TRUE
FROM zones_classees
WHERE
    (type_zone = 'CENTRE_VILLE' AND rang <= 25) OR
    (type_zone = 'PORT' AND rang <= 15) OR
    (type_zone = 'AXE_ROUTIER' AND rang <= 20) OR
    (type_zone = 'RESIDENTIEL' AND rang <= 20) OR
    (type_zone = 'PERIPHERIQUE' AND rang <= 10);

-- Insertion des capteurs METEO (35)
INSERT INTO capteur (id_zone, type_capteur, latitude, longitude, actif)
WITH zones_classees AS (
    SELECT id_zone, type_zone, centre_lat, centre_lon,
           ROW_NUMBER() OVER(PARTITION BY type_zone ORDER BY RANDOM()) as rang
    FROM zone
)
SELECT id_zone, 'Météo', centre_lat, centre_lon, TRUE
FROM zones_classees
WHERE
    (type_zone = 'CENTRE_VILLE' AND rang <= 8) OR
    (type_zone = 'PORT' AND rang <= 6) OR
    (type_zone = 'AXE_ROUTIER' AND rang <= 8) OR
    (type_zone = 'RESIDENTIEL' AND rang <= 8) OR
    (type_zone = 'PERIPHERIQUE' AND rang <= 5);