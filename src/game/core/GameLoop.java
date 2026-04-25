package game.core;

import javax.swing.Timer;

public class GameLoop {

    private static final int TICK_MS = 16; // ~60 FPS
    private static final int GAME_DURATION_MS = 10 * 60 * 1000; // 10 minutes

    private final GameStateManager gsm;
    private final Runnable repaintCallback;

    private Timer timer;
    private int remainingMs = GAME_DURATION_MS;
    private long lastTickTime;

    public GameLoop(GameStateManager gsm, Runnable repaintCallback) {
        this.gsm = gsm;
        this.repaintCallback = repaintCallback;
    }

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

        GameState state = gsm.getCurrentState();

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

        // ✅ IMPORTANT: send remaining time to GameStateManager
        gsm.setRemainingTime(remainingMs);

        // Update game logic
        gsm.update(deltaMs);

        // Always repaint
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