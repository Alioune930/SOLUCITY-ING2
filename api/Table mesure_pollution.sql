CREATE TABLE IF NOT EXISTS mesure_pollution (
                                                id_mesure SERIAL PRIMARY KEY,
                                                id_capteur INT REFERENCES capteur(id_capteur),
                                                PM10 FLOAT,
                                                PM2_5 FLOAT,
                                                NO2 FLOAT,
                                                O3 FLOAT,
                                                SO2 FLOAT,
                                                date_mesure TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO mesure_pollution (id_capteur, PM10, PM2_5, NO2, O3, SO2, date_mesure)
SELECT
    c.id_capteur,
    -- PM10
    (15 + RANDOM() * 30) * CASE
                               WHEN z.type_zone = 'PORT' THEN 2.5
                               WHEN z.type_zone = 'AXE_ROUTIER' THEN 2.8
                               WHEN z.type_zone = 'CENTRE_VILLE' THEN 1.5
                               ELSE 1.0 END AS PM10,

    -- PM2.5
    (8 + RANDOM() * 12) * CASE
                              WHEN z.type_zone = 'AXE_ROUTIER' THEN 3.0
                              WHEN z.type_zone = 'PORT' THEN 2.2
                              ELSE 1.0 END AS PM2_5,

    -- NO2
    (30 + RANDOM() * 40) * CASE
                               WHEN z.type_zone = 'AXE_ROUTIER' THEN 3.8
                               WHEN z.type_zone = 'CENTRE_VILLE' THEN 2.2
                               ELSE 1.0 END AS NO2,

    -- O3
    (40 + RANDOM() * 50) * CASE
                               WHEN z.type_zone = 'PERIPHERIQUE' THEN 2.8
                               WHEN z.type_zone = 'RESIDENTIEL' THEN 1.8
                               ELSE 1.0 END AS O3,

    -- SO2
    (50 + RANDOM() * 60) * CASE
                               WHEN z.type_zone = 'PORT' THEN 5.0
                               ELSE 1.0 END AS SO2,

    NOW()
FROM capteur c
         JOIN zone z ON c.id_zone = z.id_zone
WHERE c.type_capteur = 'Pollution' AND c.actif = TRUE;