package game.core;

/**
 * Observer interface. Implement this in any class that needs
 * to react when the game state changes (UI panels, audio, etc.)
 */
public interface GameStateListener {
    void onStateChanged(GameState newState);
}
