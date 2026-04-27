# Echoes of the Sentinel - Recent Updates
*Changes implemented after April 11, 2026*

## 1. Project Initialization & Core Framework (April 12)
The project structure was formally established. This major release included:
- **Dual-Mode Gameplay:** Introduced the "Kids Mode" and "Fun Play" configurations, allowing different subjects and complexities.
- **Difficulty Settings:** Added adjustable difficulties (Easy, Medium, Hard) and dynamically scaling Sentinel speed.
- **Improved Sentinel Tracking:** Implemented a robust AI tracking system for the Sentinel, heavily utilizing TileMap BFS pathfinding to hunt players more intelligently.

## 2. Riddle Mechanics & Penalties (April 14)
- **Skip Riddle Consequence:** The exploit where players could spam the "Skip" button with no consequences has been patched. Skipping a riddle now actively penalizes the player by triggering the 3-second **Panic Buffer**, immediately alerting the Sentinel.
- **English Kids Puzzle Fix:** Corrected a bug in the English section's "missing letter" puzzles where the game incorrectly expected the player to type the *index number* of the letter instead of the missing letter character itself.

## 3. Gameplay Tuning & UI Fixes (April 14)
- **Panic Buffer Movement Lock Removed:** Fixed an issue where the game's UI would freeze the player's inputs during the 3-second Panic Buffer penalty. Players can now seamlessly attempt to run away the moment the penalty starts.
- **Dynamic Sentinel Hunt Speed:** Balanced the Sentinel's hunting behavior. Previously, the Sentinel would always unconditionally double its speed when hunting. This scaling behavior is now intelligently capped. The Sentinel will only increase its speed if your base gameplay speed is set to 1.0x or 2.0x. This prevents impossible scenarios where the Sentinel moved fast beyond human reaction times at higher base multiplier difficulties.
