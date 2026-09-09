#!/bin/bash

DB_HOST="172.31.249.83"
DB_USER="solucitybdd_int"
DB_NAME="solucity-bdd_int"

export PGPASSWORD="solucity"
export LC_NUMERIC="C"

HEURE=$(date +%H)

if [[ ($HEURE -ge 7 && $HEURE -lt 9) || ($HEURE -ge 17 && $HEURE -lt 19) ]]; then
    COEFF_TRAFIC=1.5
elif [[ $HEURE -ge 22 || $HEURE -lt 6 ]]; then
    COEFF_TRAFIC=0.6
else
    COEFF_TRAFIC=1.0
fi

CAPTEURS=$(psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -t -A \
    -c "SELECT id FROM capteur WHERE type = 'TRAFIC' ORDER BY id;")

if [[ -z "$CAPTEURS" ]]; then
    echo "ERREUR : aucun capteur trafic récupéré."
    exit 1
fi

JSON="["
PREMIER=true

DATE_MESURE=$(date '+%Y-%m-%dT%H:%M:%S')

while IFS= read -r ID; do

    [[ -z "$ID" ]] && continue

    VARIATION=$((RANDOM % 11 - 5))

    VITESSE=$(awk \
        -v coeff="$COEFF_TRAFIC" \
        -v variation="$VARIATION" \
        -v id="$ID" \
        'BEGIN {
            vitesse = 50 - (15 * coeff) + variation + (id % 7);

            if (vitesse < 10)
                vitesse = 10;

            if (vitesse > 70)
                vitesse = 70;

            printf "%.2f", vitesse;
        }')

    VEHICULES=$(awk \
        -v coeff="$COEFF_TRAFIC" \
        -v id="$ID" \
        -v variation="$VARIATION" \
        'BEGIN {
            vehicules = 8 + (25 * coeff) + (id % 10) + variation;

            if (vehicules < 1)
                vehicules = 1;

            printf "%d", vehicules;
        }')

    OCCUPATION=$(awk \
        -v coeff="$COEFF_TRAFIC" \
        -v id="$ID" \
        -v variation="$VARIATION" \
        'BEGIN {
            occupation = 15 + (35 * coeff) + (id % 8) + variation;

            if (occupation < 0)
                occupation = 0;

            if (occupation > 100)
                occupation = 100;

            printf "%.2f", occupation;
        }')

    if [[ "$PREMIER" == false ]]; then
        JSON+=","
    fi

    JSON+="{\"vitesseMoyenne\":$VITESSE,\"nombreVehicules\":$VEHICULES,\"tauxOccupation\":$OCCUPATION,\"dateMesure\":\"$DATE_MESURE\",\"capteur\":{\"id\":$ID}}"

    PREMIER=false

done <<< "$CAPTEURS"

JSON+="]"

echo "$JSON" > /tmp/traffic_mesures_batch.json

echo "Envoi du batch trafic au backend..."

HTTP_STATUS=$(curl.exe -sS \
    -o /dev/null \
    -w "%{http_code}" \
    -X POST \
    "http://localhost:8080/api/trafic/mesures/batch" \
    -H "Content-Type: application/json" \
    --data-binary "@/tmp/traffic_mesures_batch.json")

if [[ "$HTTP_STATUS" != "204" ]]; then
    echo "ERREUR : backend HTTP $HTTP_STATUS"
    exit 1
fi

echo "Batch trafic envoyé avec succès : HTTP $HTTP_STATUS"