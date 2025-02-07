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

### Bot spécifique ObjectiveBot
- Implémentation d’un bot dédié avec comparaison à notre meilleur bot.
- Analyse des performances et identification des points forts du bot gagnant.
- Fonctionnement du bot:
    - Stocke ses destinations et y asssocie les routes nécessaires
    - À chaque début de partie il vérifie si les routes qui l'interessent on été claim
    - Il va prioriser le fait d'essayer de claim ses routes
    - S'il peut pas il pioche des cartes pour ses routes
    - Si il a remplit ses objectifs il en pioche des nouveaux
    - Il determine les routes nécessaire en appliquant l'algorithme de Djiskstra sur un graph de villes
 
  - Comparaison avec le meuilleur bot (MediumBot):
    
        Le MediumBot accumule au moins 10 cartes, puis essaye de capturer les routes les plus longues,
        Cette stratégie arrive à battre le bot obectif car il va bloquer l'objectif long du bot et surtout il va épuiser ses Wagons avant que l'objectif complète ses destinations.


## 2. Architecture et qualité
### 2.1. Architecture
#### 2.1.1 Architecture globale
L'architecture du projet s'inspire du modèle MVC.
Les `controllers` implémentes les règles du jeu.
Les `models` stockent les données du jeu sans appliquer aucune règle.
Les `views` affichent les données du jeu.
#### 2.1.2 Architecture du moteur du jeu
##### 2.1.2.1 Controllers
Chaque controller implémente une action du jeu.
Chaque controller étant de la classe `Controller` et possède une interface vers le modèle du Jeu.
Un controller possède plusieurs méthodes :
- `initGame()` : Initialise une partie du modèle du Jeu
- `initPlayer(Player)`: Initialise un joueur (Exemple : donner des cartes)
- `doAction(Player)`: Effectue l'action à laquelle le controller est associé tout en gérant les cas d'erreurs
- `resetPlayer(Player)`: Réinitialise un joueur (Exemple : remettre les cartes en main)
- `resetGame()`: Remet le modèle du jeu à 0
##### 2.1.2.2 Fonctionnement du moteur de jeu
Le moteur du jeu manipule uniquement des objets de la classe `Player` et des `Controller`.
Il n'a connaissance d'aucune règle du jeu et traite uniquement les différents cas d'actions refusées par les controllers.
Les controllers sont découpés en trois types :
- `gameControllers`: les controllers d'action du jeu (Exemple: pioches des cartes destinations, prendre une route)
- `endPlayerTurnControllers`: Controllers des actions effectuées juste après le tour d'un joueur (Exemple: mettre à jour son score)
- `endGameControllers`: Controllers des actions effectuées à la fin de la partie (Exemple : calcul final des scores).

Le moteur traite également le cas où tous les joueurs ont décidé d'arrêter la partie en effectuant l'action `STOP`.

#### 2.1.3 Models
Les models stockent les données du jeu sans appliquer aucune règle.
Ils sont partagés entre les joueurs et les controllers du jeu.
Les models sont toutes les objets du jeu (cartes, routes, villes, etc.).
Tous ces modèles sont stockés dans une seule classe `GameModel`.
Comme indiqué précédemment, chaque controller du jeu possède une interface avec lui même pour limiter l'accès aux données.
Chaque joueur à également le droit à une interface envers ce même model.

#### 2.1.4 Joueurs
Les joueurs possèdent un model qui leur est propre. Il stocke leurs cartes en main et leur score.
##### 2.1.4.1 Controller
Le controller du joueur est une implémentation de l'interface `IPlayerActionsControllable`.
C'est le programme qui va décider quels actions le joueur va effectuer suivant sa stratégie.
Il est appelé par le moteur du jeu pour effectuer une action.
Le controller du joueur n'a connaissance que deux interfaces :
- Une interface vers son modèle.
- Une interface vers le modèle du jeu.

##### 2.1.4.2 Model
Le model du joueur n'est pas implémenté dans la même classe que le controller du joueur pour éviter la modification des données en écriture par le joueur.
Il y a deux interfaces :
- `IPlayerActionsControllable` : Interface utilisé par controller du jeu pour modifier les informations du joueur.
- `IPlayerModel`: Interface que connaît le controller du joueur pour lire ses informations.
##### 2.1.4.3 Views
Il existe deux vues pour les joueurs :
- Une appartenant au modèle notifiant une sortie à la mise à jour d'informations
- Une appartenant au controller du joueur pour notifier une demande d'action.
##### 2.1.4.4 La classe Player
La classe `Player` est une simple combinaison des deux interfaces du joueur.
Elle est utilisé uniquement dans les controllers du jeu pour manipuler un seul objet au lieu de deux.

#### 2.1.5 Ressources
La carte du jeu est l'ensemble des cartes destinations sont stockés dans des fichier JSON.
Ce choix permet s'ils l'ont le souhaitent de modifier la carte du jeu sans modifier le code source.
Même si charger un fichier prend plus de temps que de charger une carte en dur. Le fichier étant chargé une fois au lancement cela n'impacte que très peu les performances.
