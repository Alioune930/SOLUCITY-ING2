Author :
ADAMALY Clara
DIENG Moussa
TIENDREBEOGO Moumouni

------------------------------------------------------------------------------------------------------------

ADAMALY Clara

**R1**
Mock pollution/météo : Générer les données mock pour tous les capteurs (pollution + météo)
-> Terminé : creation du mock + api rest pour notification mise à jour BDD
US 1 : Créer les polygones Voronoï autour de points définis
-> Terminé : création des polygones avec postgis et REST API pour les zones
US 2 : Visualisation des polygones
-> Terminé : ajout du path de l'api zones dans back.js pour que le front puisse récupérer et afficher les polygones
US 3 : Afficher le score global par quartier
-> Terminé : calcule des scores de pollution
US 4 : Afficher libellés et correspondance couleur pour chaque score
-> Terminé : ajout methode pour determiné libellé et couleur en fonction du score
US 5 : Visualisation des polygones avec score global de pollution, couleur et libellé
-> Terminé : ajout des scores, libellé et couleur dans l'API pour les zones

**R2**
Restructuration du Frontend
-> Terminé : creation du dossier modules/home modules/map adaptation des fichiers js créer par Moumouni
US 6 : Afficher les scores global de pollen sur la carte
-> Terminé : Affichage de la concentration de pollen sur la carte, superposition données pollen et pollution,
possibilité de filtrer par vue (pollen/pollution), rafraichissement automatique des données sur la carte
US 7 : Améliorer mock données capteur stream continue (3 min une journée)
-> s'adapte au mock du pollen car les données méteo change


**R3**
US 9 : Afficher le score de risque personnalisé, libellés de score de risque
et correspondance couleurs pour chaque par quartier
-> Terminé : calcule des scores de pollution et pollen adaptée aux données utilisateur,
determine libellé et couleur en fonction du score et donne un score de risque global
 + api qui reçoit le profil santé de l'utilisateur et renvoie les données calculées
US 10 : Visualisation des polygones avec risque personnalisé
-> Terminé : ajout du chemin de l'api dans le front



-------------------------------------------------------------------------------------------------------------

TIENDREBEOGO Moumouni Alioune

**** UC1 – Visualiser la carte interactive multi-indices

US1.1 – Afficher la carte avec le fond cartographique --- (Done)

US1.2 – Afficher la carte de ma ville et sa répartition en zones (position + zones analysées) --(Done)

US1.3 – Ajouter la représentation du trafic routier à la répartition en zones (Done but with static Json data)

US1.4 – Afficher le niveau de congestion et le statut des routes (Done but with random static data)

US1.5 – Afficher les zones de pollution, la qualité de l’air et le confort climatique -- (Done)

US1.6 – Afficher les légendes et tooltips dynamiques --  (Done)

US1.7 – Actualisation en temps réel - (Done)


**** Calcul des scores environnementaux à partir d’une adresse complète

US2.1 – (API) Backend qui retourne le score Global de pollution en fonction d'une adresse postale (abandon)

US2.4Suite – Calcul des Scores par Zone (Done)

US2.4 – (API) Backend pour renvoyer les données Pollen au Frontend (Done)

US2.5 – Extension de l’API (US2.1) : renvoi des données pollen selon l’adresse postale complète (abandon)


**** UC3 – Personnaliser l’affichage pour utilisateurs sensibles

US3.1 – Tests Unitaire et d'intégration (Done)

US3.2 – Ajouter la page pour l'utilisateur sensible (Done)

US3.3 – Visualiser les zones à risque selon le profil (Done)

US3.4 – Afficher des conseils contextuels (Done)

----------------------------------------------------------------------------------------------------

DIENG Moussa

**** R1

Modélisation abstraite du réseau routier (coordonnées, tronçons)
-> Terminé : définition du modèle abstrait du réseau routier avec les coordonnées et les tronçons, présenté dans un fichier Excel

Conception des objets du réseau routier et peuplement des tables de la base
-> Terminé : conception des objets du réseau routier, instanciation des données et peuplement des tables de la base de données

**** R2

US1.1 – Renvoyer tous les éléments du réseau routier (+ affichage test)
-> Terminé : mise en place de l'API permettant de récupérer les éléments du réseau routier et affichage complet du réseau sur la carte

US1.2 – Calculer le niveau de congestion par tronçon
-> Terminé : calcul du niveau de congestion à partir des données de trafic simulées, avec évolution des valeurs de congestion

US1.3 – Renvoyer le niveau de congestion par tronçon (affichage carte et coloration des tronçons)
-> Terminé : ajout de l'API permettant de récupérer les niveaux de congestion et affichage des tronçons avec une couleur correspondant à leur niveau de congestion

US1.4 – Conception aléatoire d'événements (accidents, etc.)
-> Ignoré

US1.5 – Actualisation automatique des données de trafic + statut voies
-> Terminé : actualisation des données de trafic et mise à jour automatique du statut des voies, avec affichage des changements sur la carte

**** R3

US2.1 – Identifier et sélectionner les tronçons problématiques
-> Terminé : identification et sélection des tronçons présentant un problème de congestion et affichage des informations du tronçon sélectionné

US2.2 – Simuler l'impact des actions de régulation
-> Terminé : simulation des actions de régulation sur le trafic et comparaison des valeurs de congestion avant et après l'action

US2.3 – Appliquer les actions de régulation sur le réseau et persister l'historique de l'action
-> Terminé : application des actions de régulation sur les voies, mise à jour de leur statut et enregistrement de l'historique des actions en base de données

US2.4 – Actualiser automatiquement la carte après action
-> Terminé : actualisation dynamique des données et de la carte après l'application d'une action de régulation

**Remarque concernant l'organisation des branches :**
Pour chaque UC, les US à partir de la US3 sont regroupées dans la branche correspondant à l'US2. 
Ainsi, les développements des US3 et suivantes sont inclus dans la branche US2 correspondante.
