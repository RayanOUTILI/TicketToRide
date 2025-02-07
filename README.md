# Projet les aventuriers du rail

## **1. Contribuer**

### **Voir CONTRIBUTING.md**

## **2. Compiler le projet**

```
mvn clean install -DskipTests
```

## **3. Lancer le projet**

Lancer la démonstration d'une seule partie avec les logs complets
```
mvn exec:java -Dexec.args="--demo"
```

Lancer deux milles parties avec les deux configurations de joueurs
```
mvn exec:java -Dexec.args="--2thousands"
```

Lancer 1000 parties et enregistrer les résultats dans un fichier CSV (/stats/gamestats.csv)
```
mvn exec:java -Dexec.args="--csv"
```

Lancer x parties et enregistrer les résultats dans un fichier CSV (/stats/gamestats.csv)
```
mvn exec:java -Dexec.args="--csv --nbOfGames x"
```

## **Logs**

Le projet supporte trois niveaux de verbosité, contrôlables via l'argument `--verbose=X` :

**1. (ERROR/WARN)** : Affiche uniquement les erreurs et avertissements.
```
  mvn exec:java -Dexec.args="--verbose 1 --demo"
```
**2. (INFO + ERROR/WARN)** : Affiche les informations, erreurs et avertissements. Niveau par défaut.
```
 mvn exec:java -Dexec.args="--verbose 2 --demo"
```
**3. (DEBUG + INFO + ERROR/WARN)** : Affiche tout, y compris les détails de débogage.
```
  mvn exec:java -Dexec.args="--verbose 3 --demo"
```