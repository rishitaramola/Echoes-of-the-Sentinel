# 🎮 Echoes of the Sentinel

A 2D puzzle-survival game built using Java Swing, combining real-time gameplay, AI behavior, riddles, sound effects, and a structured game loop.

---

## 🚀 Overview

**Echoes of the Sentinel** is a grid-based survival adventure where the player must explore a hostile environment, solve riddles to collect critical equipment, and escape before time runs out — all while being hunted by an intelligent entity known as the **Sentinel**.

The game blends **logic-based challenges** with **real-time danger**, requiring both quick thinking and strategic movement.

---

## 🎯 Core Gameplay

* Navigate through a tile-based map
* Locate and collect **3 essential bio-equipment items**
* Solve riddles to unlock items
* Avoid the Sentinel, which actively hunts the player
* Reach the exit before time runs out

---

## 🔁 Game Flow

```text
MENU → EXPLORATION → RIDDLE STASIS → PANIC MODE → WIN / LOSE
```

* **Exploration**: Free movement and item search
* **Riddle Stasis**: Game pauses for puzzle solving
* **Panic Mode**: Triggered on wrong answer — Sentinel aggressively targets player
* **Win/Lose**: Based on escape or capture/time-out

---

## 🧠 Key Features

### 🎮 Gameplay Mechanics

* Smooth grid-based player movement (WASD / Arrow Keys)
* Interactive item system with proximity detection
* Exit unlocking after item collection
* Time-bound survival (10-minute countdown)

---

### 👾 Enemy AI (Sentinel)

* Tracks and moves toward the player
* Detects collisions and crossing paths
* Becomes aggressive during panic mode
* Simulates basic real-time AI behavior

---

### 🧩 Riddle System

* Dynamically generated riddles
* Multiple difficulty levels
* Instant feedback system:

  * ✅ Correct → Item collected + Score increase
  * ❌ Wrong → Panic Mode triggered

---

### ⏸️ Game Control System

* Pause / Resume functionality
* Restart option
* Input blocking during pause
* Overlay rendering for paused state

---

### 🏆 Score System

* Rewards correct answers
* Displays score in:

  * HUD (during gameplay)
  * Final result screen

---

### 🔊 Sound System

* Integrated using Java audio APIs
* Event-based sound triggers:

  * Item collection
  * Wrong answer
  * Panic mode
  * Win / Lose
* Uses lightweight `.wav` files

---

### 🎨 User Interface

* Built using Java Swing
* Real-time rendering via custom GamePanel
* HUD displaying:

  * Timer
  * Items collected
  * Score
  * Game state
* Popup dialogs for results and riddles

---

## 🏗️ Architecture

The project follows a **modular and scalable design**:

```text
src/
 └── game/
      ├── core/        → Game loop, state manager
      ├── entity/      → Player, Sentinel, Items
      ├── riddle/      → Riddle generation and logic
      ├── ui/          → Rendering and UI panels
      └── audio/       → Sound system
resources/
 └── sounds/           → Audio files
```

### 🔧 Key Components

* **GameStateManager** → Central controller (game logic & states)
* **GameLoop** → Runs at ~60 FPS (heartbeat of game)
* **GamePanel** → Rendering engine
* **GameWindow** → UI navigation (Menu ↔ Game)
* **RiddleEngine** → Puzzle generation

---

## 🛠️ Technologies Used

* Java (Core + OOP concepts)
* Java Swing (GUI development)
* javax.sound.sampled (Audio system)
* Event-driven programming
* Game loop architecture

---

## ▶️ How to Run

### 🔹 Using VS Code

1. Open the project folder
2. Open `Main.java`
3. Click ▶️ Run

---

### 🔹 Using Terminal

```bash
cd src
javac game/ui/Main.java
java game.ui.Main
```

---

## 🎮 Controls

| Key       | Action                   |
| --------- | ------------------------ |
| W / ↑     | Move Up                  |
| S / ↓     | Move Down                |
| A / ←     | Move Left                |
| D / →     | Move Right               |
| E / Space | Interact                 |
| Buttons   | Pause / Resume / Restart |

---

## 💡 Learning Outcomes

This project demonstrates:

* Game loop implementation (~60 FPS)
* State-based game architecture
* Basic AI movement logic
* Event-driven UI handling
* Modular Java project design
* Integration of sound in desktop applications

---

## 🚀 Future Enhancements

* 🎵 Background music (looping system)
* 🏆 Persistent high score (file/database)
* 🎮 Smooth animations
* 🧠 Advanced AI (pathfinding algorithms)
* 💾 Save/Load game feature
* 📱 Cross-platform version (Android/Web)

---

## 👩‍💻 Team Members

* **Rishita Ramola** — Team Lead
* **Ayushi Sharma**
* **Tanisha**
* **Siddhi**

---

## 📌 Project Highlights

* Fully functional playable game
* Clean modular architecture
* Combines logic, UI, and real-time systems
* Suitable for academic submission and portfolio

---

## ✨ Conclusion

**Echoes of the Sentinel** is not just a simple Java project — it is a complete mini-game that demonstrates core concepts of game development, including real-time systems, AI behavior, user interaction, and structured programming.

---
