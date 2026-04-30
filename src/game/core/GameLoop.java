package game.core;

import javax.swing.Timer;

/**
 * GameLoop drives the entire game using a javax.swing.Timer.
 *
 * Runs at ~60 FPS (every 16 ms). On each tick:
 *   1. Decrements the global 10-minute countdown.
 *   2. Calls GameStateManager.update() (entity logic).
 *   3. Calls repaint() on the render target.
 *
 * IMPORTANT:
 * - The global timer ALWAYS ticks — it does NOT pause during RIDDLE_STASIS.
 *   This is the core "Temporal Stasis" tension.
 * - GameLoop NEVER handles pause directly.
 * - Pause is handled inside GameStateManager / GamePanel.
 * - Rendering ALWAYS continues (so overlays like PAUSED show).
 */
public class GameLoop {

    private static final int TICK_MS = 16; // ~60 FPS
    private static final int GAME_DURATION_MS = 10 * 60 * 1000; // 10 minutes

    private final GameStateManager gsm;
    private final Runnable repaintCallback;

    private Timer timer;
    private int remainingMs = GAME_DURATION_MS;
    private long lastTickTime;

    // -------------------------------------------------------
    public GameLoop(GameStateManager gsm, Runnable repaintCallback) {
        this.gsm = gsm;
        this.repaintCallback = repaintCallback;
    }

    // ---- Start / Stop / Reset -----------------------------
    public void start() {
        lastTickTime = System.currentTimeMillis();
        remainingMs = GAME_DURATION_MS;

        timer = new Timer(TICK_MS, e -> tick());
        timer.setCoalesce(true);
        timer.start();
    }

    public void stop() {
        if (timer != null) timer.stop();
    }

    public void reset() {
        stop();
        remainingMs = GAME_DURATION_MS;
    }

    // ---- One tick -----------------------------------------
    private void tick() {
        long now = System.currentTimeMillis();
        int deltaMs = (int) (now - lastTickTime);
        lastTickTime = now;

        // Global timer always counts down (even during riddle stasis)
        GameState state = gsm.getCurrentState();

        // Timer continues in all active gameplay states
        if (state == GameState.EXPLORATION ||
            state == GameState.RIDDLE_STASIS ||
            state == GameState.PANIC_BUFFER) {

            remainingMs -= deltaMs;

            if (remainingMs <= 0) {
                remainingMs = 0;
                stop();
                gsm.triggerLose();
            }
        }

        // GameStateManager decides whether to update (pause handled there)
        gsm.update(deltaMs);

        // ALWAYS repaint (important for pause overlay)
        repaintCallback.run();
    }

    // ---- Getters ------------------------------------------
    public int getRemainingMs() {
        return remainingMs;
    }

    public int getMinutes() {
        return remainingMs / 60000;
    }

    public int getSeconds() {
        return (remainingMs % 60000) / 1000;
    }
}