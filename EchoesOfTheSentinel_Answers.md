# Project Answers for Echoes of the Sentinel

## 1. Project Abstract
The primary goal of "Echoes of the Sentinel" is to deliver an engaging 2D puzzle survival game that combines dynamic AI tracking with educational and logical challenges. Players must navigate a map and solve procedurally generated riddles (in Kids Mode) or complex logic puzzles (in Fun Play Mode) while evading a relentless "Sentinel" entity. The game balances pressure and cognitive skills, rewarding quick thinking and spatial awareness.

## 2. Updated Project Approach and Architecture
The system is built using a custom object-oriented Java architecture, encompassing core game logic, entity management, UI rendering, and a procedural riddle engine. The primary mechanics utilize a 2D TileMap implementation for environment structure and a Breadth-First Search (BFS) pathfinding algorithm that enables the Sentinel to dynamically and efficiently track the player. Game states are modular, allowing seamless transitions between gameplay, penalty phases, and menus.

## 3. Tasks Completed
- Initialized core project framework and Java structure.
- Developed dual-mode gameplay systems (Kids Mode for basic puzzles, Fun Play for advanced riddles).
- Implemented adjustable difficulty settings scaling Sentinel speed and behavior.
- Designed robust BFS pathfinding for Sentinel tracking.
- Patched the "skip riddle" exploit, replacing it with a 3-second Panic Buffer penalty.
- Fixed UI input locks during the Panic Buffer and capped the Sentinel hunt speed at higher difficulties.
- Corrected logic in the English missing letter puzzles.
**Task Completed** | **Team Member**
--- | ---
All tasks listed above | Rishita Ramola

## 4. Challenges/Roadblocks
A primary challenge was implementing an intelligent enemy AI without significant performance lag. This was solved by utilizing an optimized BFS algorithm over the 2D TileMap. Another roadblock involved the "Panic Buffer" penalty: the UI inadvertently locked the player's movement, freezing them in place when they should have been able to flee. By decoupling the movement input listeners from the puzzle UI state, this was successfully patched. Finally, the Sentinel's speed scaled uncontrollably on high difficulties, creating impossible survival scenarios; this was resolved by writing a dynamic capping system based on the base difficulty.

## 5. Tasks Pending
- Expand the existing riddle and puzzle pool for both Kids and Fun Play modes.
- Implement additional varied TileMaps to create distinct, progressively difficult levels.
- Add audio integration to increase tension during the Sentinel's pursuit.
- Final UI polish and menu transitions.
**Task Pending** | **Team Member (to complete the task)**
--- | ---
All remaining features | Rishita Ramola

## 6. Project Outcome/Deliverables
The key deliverables include a fully functional, desktop-based 2D Java game featuring dynamic AI tracking and dual-mode puzzle mechanics. Tangible outcomes are the core game engine, the procedural riddle generation modules, an adjustable difficulty and settings interface, and the completed TileMap level implementation.

## 7. Progress Overview
The project is currently about 85% complete and is ahead of schedule regarding core engine mechanics and AI implementation. Pathfinding, dual-mode logic, and gameplay loops are fully operational. Time is currently being dedicated to bug fixing, balance adjustments, and level design rather than foundational system building. No major components are falling behind schedule.

## 8. Codebase Information
**Repository link**: `D:\VS CODE\PROJECTS\EchoesOfTheSentinel` (Local Repository)
**Branch**: `main`
**Important commits**:
- `68ab782`: "Fix UI input blocking in panic buffer and fix hunt speed scaling"
- `ea6a92c`: "Fix #2: Penalize skipRiddle with panic buffer instead of immediate exploration"
- `6eff9e3`: "Initialize project and add dual-mode gameplay, improved Sentinel tracking, and difficulty settings"

## 9. Testing and Validation Status
**Test Type** | **Status (Pass/Fail)** | **Notes**
--- | --- | ---
Sentinel BFS Pathfinding | Pass | Tracks player smoothly without wall clipping.
Panic Buffer Movement Lock | Pass | Player successfully regains movement instantly during the penalty countdown.
Hunt Speed Balancing | Pass | Sentinel avoids breaking human reaction scales on 3x or 4x base movement speed.
English Riddle Logic | Pass | Correctly accepts the missing character instead of its index.

## 10. Deliverables Progress
- Core Game Engine Sandbox: 100%
- AI Pathfinding & Entity Management: 100%
- UI/Menus & Game States: 85%
- Level Design & Maps: 70%
- Puzzle Variations: 80%
