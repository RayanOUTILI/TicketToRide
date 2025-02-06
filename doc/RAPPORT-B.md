# Rapport - Knowledge Exchange

## 1. Point d’avancement

### Fonctionnalités réalisées
- Jeu complet avec couverture des règles : ferry, gares, tunnels, etc.
- Parties jouables avec 3 types de bots.
- Liste des fonctionnalités non implémentées : *à compléter*

### Logs
- Utilisation de SLF4J avec mode verbose via JCommander.
- Trois niveaux de verbosité disponibles :
    1. **(ERROR/WARN)** : Affiche uniquement les erreurs et avertissements.
       ```bash
       mvn exec:java -Dexec.args="--verbose=1"
       ```
    2. **(INFO + ERROR/WARN)** : Niveau par défaut, affiche informations, erreurs et avertissements.
       ```bash
       mvn exec:java -Dexec.args="--verbose=2"
       ```
    3. **(DEBUG + INFO + ERROR/WARN)** : Affiche tous les détails.
       ```bash
       mvn exec:java -Dexec.args="--verbose=3"
       ```
- Configuration des logs définie dans `/resources/logback.xml` :
    - Utilisation de **Logback** pour la gestion des logs.
    - Permet d’afficher les logs en console et de les enregistrer dans un fichier.
    - Trois niveaux de logs définis : `INFO`, `DEBUG` et `ERROR/WARN`.
    - Le fichier `application_debug.log` contient les logs détaillés pour le niveau `DEBUG`.
    - Cette configuration permet le suivi des événements et le débogage du projet.

### Statistiques en CSV
- Utilisation de la bibliothèque **OpenCSV**.
- Génération et exportation des données statistiques au format CSV.
- POWER BI

### Bot spécifique
- Implémentation d’un bot dédié avec comparaison à notre meilleur bot.
- Analyse des performances et identification des points forts du bot gagnant.

---