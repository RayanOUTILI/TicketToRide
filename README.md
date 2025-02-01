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
mvn exec:java@gameSimulator
```






