# Ticket to Ride - Europe (Les Aventuriers du Rail)

![IMG\_8927-1-900x600](https://github.com/user-attachments/assets/b58b3a78-5853-48b0-ac97-3d696cb71a52)

This project is a digital adaptation of the board game *Ticket to Ride - Europe* by Alan R. Moon. It is fully implemented in Java, executable via Maven, and designed to simulate games between AI players without requiring human input or a graphical interface.

---

## Project Overview

The goal of this project is to create a complete simulation of *Ticket to Ride - Europe*, including:

* **Game Representation**: Board setup, routes between cities, train pieces, stations, destination tickets, train cards, and victory points.
* **Game Engine**: Setup, deck management, player actions, point calculation, bonus/penalty handling, and end-of-game logic.
* **AI Players**: From simple baseline bots to advanced strategies capable of competing against each other.
* **Simulation**: Run multiple AI games, calculate scores, track wins, and produce rankings.
* **Text-based Visualization**: Display the game state in the console, initially at the end of the game and eventually throughout the entire game progression.

---

## Compiling the Project

```bash
mvn clean install -DskipTests
```

---

## Running the Project

* **Run a single game demo with full logs:**

```bash
mvn exec:java -Dexec.args="--demo"
```

* **Run 2,000 games with two player configurations:**

```bash
mvn exec:java -Dexec.args="--2thousands"
```

* **Run 1,000 games and save results to a CSV file (`/stats/gamestats.csv`):**

```bash
mvn exec:java -Dexec.args="--csv"
```

* **Run X games and save results to CSV:**

```bash
mvn exec:java -Dexec.args="--csv --nbOfGames x"
```

---

## Logging Levels

The project supports three verbosity levels, controlled via `--verbose=X`:

1. **ERROR/WARN** – Only errors and warnings:

```bash
mvn exec:java -Dexec.args="--verbose 1 --demo"
```

2. **INFO + ERROR/WARN** – Includes informational messages (default level):

```bash
mvn exec:java -Dexec.args="--verbose 2 --demo"
```

3. **DEBUG + INFO + ERROR/WARN** – Full debug details:

```bash
mvn exec:java -Dexec.args="--verbose 3 --demo"
```

---

## Rules Overview

In *Ticket to Ride - Europe*, players:

1. **Collect train cards** of various colors to claim routes.
2. **Claim routes** connecting cities on the European map.
3. **Complete destination tickets** for bonus points.
4. **Place stations** strategically to use opponents’ routes.
5. **Score points** based on routes, completed tickets, and remaining trains/stations.
6. **End the game** when a player’s train supply is low, then tally final scores.

Full rules: [Ticket to Ride - Europe Rules PDF](https://cdn.svc.asmodee.net/production-daysofwonder/uploads/2023/09/7222-T2RE-Rules-FR.pdf)

---

## Contributors

* @LouisR0 – Louis Rainero
* @RayanOUTILI – Rayan Outili
* @MatveiMKS – Maksimenka Matvei
* @allien-j – Jim Lainel

---

## License

This project is for educational purposes only and does not include any commercial license of the original *Ticket to Ride* game.
