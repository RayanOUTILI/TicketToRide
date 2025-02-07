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
***À noter que cela gère l'affichage si une sortie console est défini sur le mode d'execution.*** Il est cependant possible de forcer l'affichage console avec l'argument : `--force-log`
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

#### 2.1.6 Exécuteur de parties
L'exécuteur de partie `GameExecutor` est une classe qui va envelopper le moteur du jeu.
Elle permet d'automatiser la gestion du moteur dans ses différentes phases à savoir :
- Initialisation de la partie. `initGame()`
- Initialisation des joueurs. `initPlayers()`
- Exécution du jeu. `startGame()`
- Réinitialisation de la partie. `reset()`

Le tout pour un nombre de parties défini qui lui est passé en argument.

#### 2.1.7 Factories
Les factories sont utilisées pour créer les objets du jeu.

##### 2.1.7.1 Modelisation des données (`DataModelisationFactory`)
Cette factory se charge de créer les objets du jeu à partir d'une implémentation spécifique.
Via le passage d'une liste de type de joueurs `PlayerType` souhaité,
elle crée un `GameModel` prêt à l'emploi et des `PlayerModel` pour chaque joueur.

Elle peut être `extends` permettant donc l'implémentation de cartes différentes ou encore de plateaux différents.

*e.g. `EuropeDatasFactory` qui implémente les cartes et les routes de la version Europe du jeu.*

##### 2.1.7.2 Modelisation des actions (`GameActionFactory`)
Cette factory se charge de créer les controllers du jeu à partir d'une implémentation spécifique.
Elle retourne différentes listes de controllers en fonction des actions de jeu souhaitées :
- `getGameActions()` : Action que les joeurs peuvent effectuer pendant leur tour
- `getEndPlayerTurnActions()` : Actions effectuées à la fin du tour d'un joueur
- `getEndGameActions()` : Actions effectuées à la fin de la partie

Elle peut également être `extends` pour implémenter des actions spécifiques.

##### 2.1.7.3 Modelisation des joueurs (`PlayerFactory`)
Cette factory se charge de créer les controllers des joueurs en fonction du `PlayerType` demandé.

Elle retourne une liste de controllers de joueurs prêts à l'emploi.

##### 2.1.7.4 Modelisation des vues (`ViewFactory`)
Cette factory se charge de créer les vues des joueurs en fonction du `PlayerType` et des options de vues `ViewOptions` demandé.

Elle permet de créer la vue pour chaque joueur et de les retourner dans une liste `IPlayerEngineViewable` et par la suite de créer la vue pour le moteur du jeu `IGameViewable`.

##### 2.1.7.1 GameExecutorFactory
Cette factory se charge d'assembler la totalité des objets du jeu générés par les autres factories.
Elle est utilisée pour créer un `GameExecutor` prêt à l'emploi.

Elle ne prend en paramètre qu'une liste de joueurs `PlayerType` et une liste d'options visuelles `ViewOptions` pour créer un `GameExecutor`.

##### 2.1.8 Arguments (`JCommander`)
La librairie `JCommander` est utilisée pour gérer les arguments de la ligne de commande.
Elle est implémentée dans la classe `CommandLineArgs` et permet de définir les arguments suivants :
- `--verbose` : Niveau de verbosité des logs. *c.f. Logs*
- `--force-log` : Forcer l'affichage des logs en console.
- `--stats` : Exporter les statistiques en CSV.
- `--database` : Exporter les statistiques dans une base de données. *(fichier .env)*
- `--nbOfGames` : Mode d'exécution sélectif, il exécutera un nombre de parties demo **x** fois.
- `--demo` : Mode d'exécution démo, lance une partie avec chacun des bots disponibles avec un affichage console.
- `--2thousands` : Mode d'exécution 2*1000 parties, lance 1000 parties avec chacun des bots disponibles et 1000 parties avec le bot le plus fort `MEDIUM_BOT`. Il affichera les statistiques des parties dans la console.

***À noter que les arguments `--nbOfGames`, `--demo` et `--2thousands` ne peuvent pas être utilisés ensemble.***

### 2.2 Qualité
#### 2.2.1 Tests
- Principalement concentrés sur les controllers du jeu et les joueurs.
- Utilise des mocks pour simuler les actions et cas spécifiques.
#### 2.2.2 SonarQube
- Utilisation de SonarQube pour analyser la qualité du code.
- Correction des erreurs et avertissements remontés.
#### 2.2.3 Principes
- Respect au maximum des principes SOLID.
- Utilisation de design patterns pour structurer le code.

### 2.3 Refactor et amélioration
- Même si le coverage est supérieur à 80%, plus de tests d'intégration sont nécessaires.
- Le game engine est encore perfectible surtout dans sa complexité.
- Une séparation d'un GameModel en plusieurs sous classe pourrait être envisagée.
- Cela est notamment représenté sur SonarQube avec le plugin Softvis3D.
- La gestion des actions avec les contrôleurs est plutôt bien réussi et permet une mobilité importante.

## 3. Répartition des points
- `Louis`: `140` points
- `Rayan`: `100` points
- `Matvei`: `80` points
- `Jim`: `80` points
