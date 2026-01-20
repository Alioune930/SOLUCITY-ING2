#!/bin/bash

# --- CONFIG ---
DB_HOST="172.31.249.83"
DB_USER="solucitybdd_int"
DB_NAME="solucity-bdd_int"
export PGPASSWORD="solucity"
export LC_NUMERIC="C"

echo "Début MOCK"

# ---------------------------------------------------------
#                   COEFF HEURE DE POINTE
# ---------------------------------------------------------
HEURE=$(date +%H)

if [[ ($HEURE -ge 7 && $HEURE -lt 9) || ($HEURE -ge 17 && $HEURE -lt 19) ]]; then
    COEFF_H=1.5
else
    COEFF_H=1.0
fi

echo "Heure = $HEURE | Coeff heure pointe = $COEFF_H"

# ---------------------------------------------------------
#                     MOCK POLLUTION
# ---------------------------------------------------------

BASE_NO2=(15 22 25 27 28)
BASE_PM10=(10 14 18)
BASE_PM25=(4 6 8)

CAPTEURS_POLLUTION=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -A \
    -c "SELECT id_capteur, profil FROM capteur_pollution;")

echo "Données mock pollution"

while IFS="|" read -r ID PROFIL; do
    [[ -z "$ID" ]] && continue

    case $PROFIL in
        "touristique") COEFF_PROFIL=1.2 ;;
        "gros Trafic") COEFF_PROFIL=2.0 ;;
        "Industriel") COEFF_PROFIL=1.5 ;;
        "residentiel") COEFF_PROFIL=0.7 ;;
        "nature") COEFF_PROFIL=0.5 ;;
        *)
            echo "Profil inconnu : $PROFIL -> capteur ignoré"
            continue
            ;;
    esac

    B_NO2=${BASE_NO2[$RANDOM % ${#BASE_NO2[@]}]}
    B_PM10=${BASE_PM10[$RANDOM % ${#BASE_PM10[@]}]}
    B_PM25=${BASE_PM25[$RANDOM % ${#BASE_PM25[@]}]}

    A=$(awk "BEGIN {print 0.8 + rand()*0.4}")

    NO2=$(awk "BEGIN {printf \"%.2f\", $B_NO2 * $COEFF_PROFIL * $COEFF_H * $A}")
    PM10=$(awk "BEGIN {printf \"%.2f\", $B_PM10 * $COEFF_PROFIL * $COEFF_H * $A}")
    PM25=$(awk "BEGIN {printf \"%.2f\", $B_PM25 * $COEFF_PROFIL * $COEFF_H * $A}")

    echo "Pollution -> $ID | NO2=$NO2 | PM10=$PM10 | PM25=$PM25"

    # Vérifie si une mesure existe déjà pour l'ID capteur
    EXIST=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -A \
        -c "SELECT id_mesure FROM mesure_pollution WHERE id_capteur='$ID' ORDER BY id_mesure DESC LIMIT 1;")

    echo "Update des données pollution dans la BDD"
    if [[ -z "$EXIST" ]]; then
        # Aucune mesure trouvée on INSERT
        psql -h $DB_HOST -U $DB_USER -d $DB_NAME \
            -c "INSERT INTO mesure_pollution (id_capteur, no2, pm10, pm25)
                VALUES ('$ID', $NO2, $PM10, $PM25);" > /dev/null
    else
        # Une mesure existe déjà on UPDATE
        psql -h $DB_HOST -U $DB_USER -d $DB_NAME \
            -c "UPDATE mesure_pollution
                SET no2 = $NO2, pm10 = $PM10, pm25 = $PM25
                WHERE id_mesure = $EXIST;" > /dev/null
    fi


done <<< "$CAPTEURS_POLLUTION"

# ---------------------------------------------------------
#                       MOCK METEO
# ---------------------------------------------------------
echo "Données mock météo"

TEMP_BASES=(10 13 15 20 23 25 28 30)
T_BASE=${TEMP_BASES[$RANDOM % ${#TEMP_BASES[@]}]}
T_RAND=$(awk "BEGIN {print $T_BASE + (rand()*4 - 2)}")

echo "Température simulée = $T_RAND °C"

CAPTEURS_METEO=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -A \
    -c "SELECT id_capteur, lieu FROM capteur_meteo;")


while IFS="|" read -r ID LIEU; do
    [[ -z "$ID" ]] && continue

    case "$LIEU" in
        "Vieux Bassin")
            VENTS=(8 10 12)
            AJUST_HUM=5
            ;;
        "Longchamps")
            VENTS=(14 16 18 20)
            AJUST_HUM=0
            ;;
        "Route de Trouville")
            VENTS=(22 27 30)
            AJUST_HUM=10
            ;;
        *)
            echo "Lieu inconnu : $LIEU → ignoré"
            continue
            ;;
    esac

    V_BASE=${VENTS[$RANDOM % ${#VENTS[@]}]}
    V_FINAL=$(awk "BEGIN {printf \"%.2f\", $V_BASE + (rand()*5)}")

    H_FINAL=$(awk "BEGIN {printf \"%.2f\", (100 - ($T_RAND * 1.2)) + $AJUST_HUM}")

    echo "Météo -> $ID | Vent=$V_FINAL | Hum=$H_FINAL"

    # Vérifie si une mesure existe déjà pour l'ID capteur
    EXIST=$(psql -h $DB_HOST -U $DB_USER -d $DB_NAME -t -A \
        -c "SELECT id_mesure FROM mesure_meteo WHERE id_capteur='$ID' ORDER BY id_mesure DESC LIMIT 1;")

    echo "Update des données meteo dans la BDD"
    if [[ -z "$EXIST" ]]; then
        # Aucune mesure trouvée on INSERT
        psql -h $DB_HOST -U $DB_USER -d $DB_NAME \
            -c "INSERT INTO mesure_meteo (id_capteur, vitesse_vent, temperature, humidite)
                VALUES ('$ID', $V_FINAL, $T_RAND, $H_FINAL);" > /dev/null
    else
        # Une mesure existe déjà on UPDATE
        psql -h $DB_HOST -U $DB_USER -d $DB_NAME \
            -c "UPDATE mesure_meteo
                SET vitesse_vent = $V_FINAL, temperature = $T_RAND, humidite = $H_FINAL
                WHERE id_mesure = $EXIST;" > /dev/null
    fi



done <<< "$CAPTEURS_METEO"

echo "Notification à l'API"

curl -X POST http://172.31.249.83:8082/air-quality/update-data


echo "Terminé"