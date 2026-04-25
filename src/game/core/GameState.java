package game.core;

/**
 * Represents all possible states the game can be in.
 * The GameStateManager transitions between these.
 */
public enum GameState {
    MENU,           // Title screen
    EXPLORATION,    // Player is freely moving around the map
    RIDDLE_STASIS,  // Temporal Stasis: world frozen, player solving a riddle
    PANIC_BUFFER,   // 3-second grace window after a failed riddle
    WIN,            // Player collected 3 items and reached the exit
    LOSE            // Timer ran out or Sentinel caught the player
}
