CREATE TABLE capteur_pollution (
                                   id_capteur VARCHAR(20) PRIMARY KEY,
                                   ville VARCHAR(100) NOT NULL,
                                   profil VARCHAR(100) NOT NULL,
                                   longitude FLOAT NOT NULL,
                                   latitude FLOAT NOT NULL
);


CREATE TABLE mesure_pollution (
                                  id_mesure INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  id_capteur VARCHAR(20) NOT NULL,
                                  no2 FLOAT,
                                  pm10 FLOAT,
                                  pm25 FLOAT,
                                  FOREIGN KEY (id_capteur) REFERENCES capteur_pollution(id_capteur)
);


CREATE TABLE capteur_meteo (
                               id_capteur VARCHAR(20) PRIMARY KEY,
                               lieu VARCHAR(100) NOT NULL,
                               ville VARCHAR(100) NOT NULL,
                               longitude FLOAT NOT NULL,
                               latitude FLOAT NOT NULL
);


CREATE TABLE mesure_meteo (
                              id_mesure INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              id_capteur VARCHAR(20) NOT NULL,
                              vitesse_vent FLOAT,
                              temperature FLOAT,
                              humidite FLOAT,
                              FOREIGN KEY (id_capteur) REFERENCES capteur_meteo(id_capteur)
);



INSERT INTO capteur_pollution (id_capteur, profil, ville, latitude, longitude)
VALUES
    ('HONFP-1', 'touristique', 'Honfleur', 49.420259, 0.233919),
    ('HONFP-2', 'gros Trafic', 'Honfleur', 49.389884, 0.250779),
    ('HONFP-3', 'Industriel', 'Honfleur', 49.413699, 0.25534),
    ('HONFP-4', 'residentiel', 'Honfleur', 49.427189, 0.22313),
    ('HONFP-5', 'nature', 'Honfleur', 49.414793, 0.235832);


INSERT INTO capteur_meteo (id_capteur, lieu, ville, latitude, longitude)
VALUES
    ('HONFM-1', 'Vieux Bassin', 'Honfleur', 49.420259, 0.233919),
    ('HONFM-2', 'Longchamps', 'Honfleur', 49.412486, 0.243399),
    ('HONFM-3', 'Route de Trouville', 'Honfleur', 49.4256, 0.217915);


CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE ville (
                       nom VARCHAR(100) PRIMARY KEY,
                       geom GEOMETRY(MULTIPOLYGON, 4326) NOT NULL
);

--INSERT INTO ville (nom, geom)
--VALUES (
  --         'Honfleur',
  --         ST_GeomFromGeoJSON('{ "type": "Polygon", ... }'));


ALTER TABLE capteur_pollution
    ADD COLUMN geom GEOMETRY(POINT, 4326);

ALTER TABLE capteur_meteo
    ADD COLUMN geom GEOMETRY(POINT, 4326);


UPDATE capteur_pollution
SET geom = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
WHERE geom IS NULL;

UPDATE capteur_meteo
SET geom = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326)
WHERE geom IS NULL;


CREATE TABLE zone_ville (
                            id SERIAL PRIMARY KEY,
                            id_capteur VARCHAR(20) NOT NULL,
                            ville VARCHAR(100) NOT NULL,
                            geom GEOMETRY(MULTIPOLYGON, 4326) NOT NULL,

                            CONSTRAINT fk_zone_capteur
                                FOREIGN KEY (id_capteur) REFERENCES capteur_pollution(id_capteur)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_zone_ville
                                FOREIGN KEY (ville) REFERENCES ville(nom)
                                    ON DELETE CASCADE,

                            CONSTRAINT unique_zone_capteur_ville
                                UNIQUE (id_capteur, ville)
);


WITH
    capteurs AS (
        SELECT id_capteur, geom
        FROM capteur_pollution
        WHERE ville = 'Honfleur'
    ),
    voronoi AS (
        SELECT
            (ST_Dump(
                    ST_VoronoiPolygons(
                            ST_Collect(geom)
                    )
             )).geom AS geom
        FROM capteurs
    ),
    ville_geom AS (
        SELECT nom, geom
        FROM ville
        WHERE nom = 'Honfleur'
    )
INSERT INTO zone_ville (id_capteur, ville, geom)
SELECT
    c.id_capteur,
    v.nom,
    ST_Intersection(vor.geom, v.geom)
FROM voronoi vor
         JOIN capteurs c
              ON ST_Contains(vor.geom, c.geom)
         JOIN ville_geom v
              ON ST_Intersects(vor.geom, v.geom);


