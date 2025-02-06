# Projet les aventuriers du rail

## **1. Contribuer**

### **Voir CONTRIBUTING.md**

## **2. Compiler le projet**

```
mvn clean install -DskipTests
```

## **3. Lancer le projet**

Lancer le jeu
```
mvn exec:java
```

Lancer l'évaluation des bots
```
mvn exec:java@gameStatistics
```

Lancer x parties
```
mvn exec:java@gameExecutor
```


## **Logs**

Le projet supporte trois niveaux de verbosité, contrôlables via l'argument `--verbose=X` :

**1. (ERROR/WARN)** : Affiche uniquement les erreurs et avertissements.
```
  mvn exec:java -Dexec.args="--verbose=1" 
```
**2. (INFO + ERROR/WARN)** : Affiche les informations, erreurs et avertissements. Niveau par défaut.
```
 mvn exec:java -Dexec.args="--verbose=2" 
```
**3. (DEBUG + INFO + ERROR/WARN)** : Affiche tout, y compris les détails de débogage.
```
  mvn exec:java -Dexec.args="--verbose=3"
```



