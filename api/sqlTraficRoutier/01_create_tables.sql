CREATE TABLE route (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE troncon (
    id BIGSERIAL PRIMARY KEY,
    longueur DOUBLE PRECISION NOT NULL,
    congestion_niveau VARCHAR(20) NOT NULL,
    route_id BIGINT,

    CONSTRAINT fk_troncon_route
        FOREIGN KEY (route_id)
        REFERENCES route(id)
);

CREATE TABLE coordonnee (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    ordre_coordonnee INTEGER NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_coordonnee_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE TABLE voie (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    statut VARCHAR(100) NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_voie_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE TABLE capteur (
    id BIGSERIAL PRIMARY KEY,
    reference VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_capteur_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE TABLE mesure_trafic (
    id BIGSERIAL PRIMARY KEY,
    vitesse_moyenne DOUBLE PRECISION,
    nombre_vehicules INTEGER,
    taux_occupation DOUBLE PRECISION,
    date_mesure TIMESTAMP NOT NULL,
    capteur_id BIGINT NOT NULL,

    CONSTRAINT fk_mesure_capteur
        FOREIGN KEY (capteur_id)
        REFERENCES capteur(id)
        ON DELETE CASCADE
);

CREATE TABLE congestion (
    id BIGSERIAL PRIMARY KEY,
    niveau VARCHAR(20) NOT NULL,
    vitesse_moyenne DOUBLE PRECISION,
    nombre_vehicules INTEGER,
    taux_occupation DOUBLE PRECISION,
    date_calcul TIMESTAMP NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_congestion_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE TABLE evenement (
    id VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_evenement_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE TABLE action_regulation (
    id BIGSERIAL PRIMARY KEY,
    type_action VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date_action TIMESTAMP NOT NULL,
    statut VARCHAR(100) NOT NULL,
    troncon_id BIGINT NOT NULL,

    CONSTRAINT fk_action_troncon
        FOREIGN KEY (troncon_id)
        REFERENCES troncon(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_troncon_route
ON troncon(route_id);

CREATE INDEX idx_coordonnee_troncon
ON coordonnee(troncon_id);

CREATE INDEX idx_voie_troncon
ON voie(troncon_id);

CREATE INDEX idx_capteur_troncon
ON capteur(troncon_id);

CREATE INDEX idx_mesure_capteur
ON mesure_trafic(capteur_id);

CREATE INDEX idx_congestion_troncon
ON congestion(troncon_id);

CREATE INDEX idx_evenement_troncon
ON evenement(troncon_id);

CREATE INDEX idx_action_troncon
ON action_regulation(troncon_id);