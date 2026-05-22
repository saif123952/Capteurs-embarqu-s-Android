# Capteurs-embarqués-Android

Cette application Android démontre l'utilisation de divers capteurs embarqués (Accéléromètre, Gyroscope, Température, Proximité, etc.) avec une architecture modulaire basée sur des Fragments.

## Fonctionnalités
- **Liste des capteurs** : Affiche tous les capteurs disponibles sur l'appareil avec leurs détails techniques (vendeur, puissance, résolution).
- **Moniteur de capteurs** : Visualisation en temps réel via des graphiques personnalisés pour la température, l'humidité et le champ magnétique.
- **Mouvements & Dynamique** : Analyse en temps réel des données de l'accéléromètre, du gyroscope et de la gravité.
- **Compteur de pas** : Suivi précis des pas effectués durant la session avec gestion des permissions.
- **Boussole Numérique** : Détermination de l'orientation cardinale (Nord, Sud, Est, Ouest) en utilisant la matrice de rotation.
- **Reconnaissance d'activité** : Algorithme personnalisé pour détecter si l'utilisateur est stable, marche ou saute.

## Aperçu
<img width="327" height="672" alt="21" src="https://github.com/user-attachments/assets/0f202351-33fc-4c2b-a3b7-79670f7ab922" />
<img width="332" height="507" alt="213" src="https://github.com/user-attachments/assets/c99f8184-3433-467f-88e3-ba37071aa80a" />
<img width="333" height="573" alt="212" src="https://github.com/user-attachments/assets/03e6ca78-1836-4e64-abd5-6a3b4fb0424d" />



## Architecture du Projet
- **Package fragments** : Contient la logique isolée pour chaque type de capteur.
- **Package views** : Contient `CustomGraphView`, une vue personnalisée pour le dessin de courbes sans librairie externe.
- **Package utils** : Classes utilitaires pour le formatage des données.

## Installation et Utilisation
1. Clonez ce dépôt sur votre machine locale.
2. Ouvrez le projet avec **Android Studio**.
3. Synchronisez le projet avec Gradle.
4. Déployez sur un appareil physique (recommandé) ou un émulateur supportant les capteurs.
