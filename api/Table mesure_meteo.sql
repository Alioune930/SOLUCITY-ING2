CREATE TABLE IF NOT EXISTS mesure_meteo (
                                            id_mesure SERIAL PRIMARY KEY,
                                            id_capteur INT REFERENCES capteur(id_capteur),
    vitesse_vent FLOAT,
    direction_vent INT,
    temperature FLOAT,
    humidite FLOAT,
    date_mesure TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


INSERT INTO mesure_meteo (id_capteur, vitesse_vent, direction_vent, temperature, humidite, date_mesure)
SELECT
    c.id_capteur,
    -- VITESSE VENT
    (5 + RANDOM() * 10) * CASE
                              WHEN z.type_zone = 'PORT' THEN 2.5        -- Peut dépasser 30 km/h (Vent fort)
                              WHEN z.type_zone = 'PERIPHERIQUE' THEN 1.5 -- Zones dégagées
                              ELSE 1.0 END AS vitesse_vent,

    -- DIRECTION VENT
    FLOOR(RANDOM() * 361) AS direction_vent,

    -- TEMPERATURE
    (15 + RANDOM() * 7) + CASE
                              WHEN z.type_zone = 'CENTRE_VILLE' THEN 5  -- Souvent > 25°C l'été
                              WHEN z.type_zone = 'AXE_ROUTIER' THEN 3
                              WHEN z.type_zone = 'PORT' THEN -2         -- Air marin plus frais
                              ELSE 0 END AS temperature,

    -- HUMIDITE
    (40 + RANDOM() * 20) + CASE
                               WHEN z.type_zone = 'PORT' THEN 30         -- Souvent > 80% (Humidité élevée)
                               WHEN z.type_zone = 'PERIPHERIQUE' THEN 10
                               ELSE 0 END AS humidite,

    NOW()
FROM capteur c
         JOIN zone z ON c.id_zone = z.id_zone
WHERE c.type_capteur = 'Météo' AND c.actif = TRUE;