DROP TABLE IF EXISTS zone;
CREATE TABLE zone (
                      id_zone SERIAL PRIMARY KEY,
                      nom_zone VARCHAR(50) NOT NULL,
                      centre_lat DOUBLE PRECISION NOT NULL,
                      centre_lon DOUBLE PRECISION NOT NULL,
                      lat_min DOUBLE PRECISION NOT NULL,
                      lat_max DOUBLE PRECISION NOT NULL,
                      lon_min DOUBLE PRECISION NOT NULL,
                      lon_max DOUBLE PRECISION NOT NULL,
                      surface_km2 DOUBLE PRECISION DEFAULT 0.04,
                      ville VARCHAR(100),
                      type_zone VARCHAR(50),
                      Poluglobal DOUBLE PRECISION DEFAULT NULL,
                      Poluglobal_libellé VARCHAR(30) DEFAULT NULL
);

-- Génération du quadrillage
WITH params AS (
    SELECT
        49.4190 AS centre_lat_ref, -- Centre de Honfleur
        0.2310  AS centre_lon_ref,
        49.40098 AS lat_min_city,
        49.43702 AS lat_max_city,
        0.20375  AS lon_min_city,
        0.25825  AS lon_max_city,
        200.0 AS cell_size_m
),
     calc AS (
         SELECT
             *,
             (cell_size_m / 111111.0) AS delta_lat,
             (cell_size_m / (111111.0 * COS(centre_lat_ref * PI() / 180.0))) AS delta_lon
         FROM params
     ),
     grid AS (
         SELECT
             i, j,
             (lat_min_city + (i + 0.5) * delta_lat) AS c_lat,
             (lon_min_city + (j + 0.5) * delta_lon) AS c_lon,
             (lat_min_city + i * delta_lat) AS l_min,
             (lat_min_city + (i + 1) * delta_lat) AS l_max,
             (lon_min_city + j * delta_lon) AS o_min,
             (lon_min_city + (j + 1) * delta_lon) AS o_max
         FROM calc,
              generate_series(0, FLOOR((lat_max_city - lat_min_city) / (cell_size_m / 111111.0))::INT) AS i,
              generate_series(0, FLOOR((lon_max_city - lon_min_city) / (cell_size_m / (111111.0 * COS(49.4190 * PI() / 180.0))))::INT) AS j
     )
INSERT INTO zone (nom_zone, centre_lat, centre_lon, lat_min, lat_max, lon_min, lon_max, surface_km2, ville)
SELECT
    'HONF_' || i || '_' || j,
    c_lat, c_lon, l_min, l_max, o_min, o_max, 0.04, 'Honfleur'
FROM grid;

-- Remplissage des types de zones
WITH stats AS (
    SELECT
        AVG(centre_lat) as avg_lat,
        AVG(centre_lon) as avg_lon,
        percentile_cont(0.85) WITHIN GROUP (ORDER BY centre_lat) as limit_port
    FROM zone
)
UPDATE zone
SET type_zone = CASE
    -- Centre-ville (Distance < 600m du centre théorique)
                    WHEN (SQRT(POWER((centre_lat - 49.4190) * 111, 2) + POWER((centre_lon - 0.2310) * 72, 2))) < 0.6
                        THEN 'CENTRE_VILLE'
    -- Port (Zones très au Nord)
                    WHEN centre_lat > (SELECT limit_port FROM stats)
                        THEN 'PORT'
    -- Axes (Proches des moyennes lat/lon)
                    WHEN ABS(centre_lat - (SELECT avg_lat FROM stats)) < 0.0005 OR ABS(centre_lon - (SELECT avg_lon FROM stats)) < 0.0005
                        THEN 'AXE_ROUTIER'
    -- Résidentiel (Proche du centre)
                    WHEN (SQRT(POWER((centre_lat - 49.4190) * 111, 2) + POWER((centre_lon - 0.2310) * 72, 2))) < 1.5
                        THEN 'RESIDENTIEL'
                    ELSE 'PERIPHERIQUE'
END
    WHERE id_zone IS NOT NULL;