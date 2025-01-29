# Conventions de nommage

## **1. Commits**

### **Structure des Commits :**

```
prefix: description #issue-id
```

### **Préfixes :**
- **`feature:`** Nouvelles fonctionnalités
- **`fix:`** Corrections de bugs
- **`refactor:`** Restructuration du code
- **`test:`** Tests
- **`docs:`** Mise à jour de la documentation

Exemple : `fix: resolve issue with route scoring calculation (#2)`

---

## **2. Branches**

### **Structure des branches :**

Une branche par US et une ou plusieurs branches (par feature) qui découlent de cette US.

### **Nommage des branches :**

Branche dite "principale"

```
USx-description
```

Exemple : `US1-tirer-cartes-wagons`

Branche "feature"

```
taskx-description
```

Exemple : `T1.1-implementer-cartes-wagons`

## **3. Gestion des branches**

### **Ancienne stratégie :**
Une branche par US et plusieurs branches par tâche

1. **Branche par US :**  
   Exemple : `US1-tirer-cartes-wagons`

2. **Branches par tâche (découlant de l'US) :**  
   Exemple : `T1.1-implementer-cartes-wagons`


   **Avantage :** Chaque tâche est bien isolée.  
   **Inconvénient :** La gestion devient complexe à mesure que le nombre de branches augmente.

---

### **Stratégie mise à jour :**
Une branche par feature avec possibilité de sous-branches si nécessaire

1. **Branche principale par feature (généralement équivalente à une US) :**  
   Exemple : `feature1/medium-bot`

2. **Sous-branches par tâche (si nécessaire) :**  
   Exemple : `feature1/medium-bot-improve-choice`

   **Avantage :** Réduction du nombre de branches principales à gérer.  

---




