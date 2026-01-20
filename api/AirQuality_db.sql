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
