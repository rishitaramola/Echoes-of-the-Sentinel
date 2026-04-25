package game.core;

import game.entity.Player;
import game.entity.Sentinel;
import game.entity.BioEquipment;
import game.riddle.RiddleEngine;
import game.riddle.Riddle;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {

    private boolean isPaused = false;

    // --- Core State ---
    private GameState currentState = GameState.MENU;
    private int panicBufferMs = 0;
    private static final int PANIC_DURATION_MS = 3000;

    // --- Score System ---
    private int score = 0;
    private int timeRemainingMs = 0;

    // --- Game Entities ---
    private final Player player;
    private final Sentinel sentinel;
    private final List<BioEquipment> items;
    private final RiddleEngine riddleEngine;

    // --- Session Data ---
    private GameMode activeMode = GameMode.FUN_PLAY;
    private KidsSubject kidsSubject = KidsSubject.MATHS;
    private DifficultyLevel difficulty = DifficultyLevel.EASY;
    private int itemsCollected = 0;
    private static final int ITEMS_NEEDED = 3;

    // --- Current active riddle ---
    private Riddle activeRiddle = null;
    private BioEquipment riddleTargetItem = null;

    // --- Listeners ---
    private final List<GameStateListener> listeners = new ArrayList<>();

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public GameStateManager() {
        player = new Player(5, 5);
        sentinel = new Sentinel(1, 1);
        items = createItems();
        riddleEngine = new RiddleEngine();
    }

    private List<BioEquipment> createItems() {
        List<BioEquipment> list = new ArrayList<>();
        list.add(new BioEquipment("Oxygen Mask", 9, 2, 0));
        list.add(new BioEquipment("Neural Implant", 2, 9, 1));
        list.add(new BioEquipment("Bio-Scanner", 9, 9, 2));
        list.add(new BioEquipment("Stasis Pod", 5, 12, 3));
        return list;
    }

    // ---- Update loop ----
    public void update(int deltaMs) {
        if (isPaused) return;

        if (currentState == GameState.EXPLORATION) {
            int prevSentinelX = sentinel.getTileX();
            int prevSentinelY = sentinel.getTileY();
            sentinel.update(deltaMs, player);
            checkSentinelCatch(prevSentinelX, prevSentinelY);
            checkItemProximity();

        } else if (currentState == GameState.PANIC_BUFFER) {
            panicBufferMs -= deltaMs;

            int prevSentinelX = sentinel.getTileX();
            int prevSentinelY = sentinel.getTileY();
            sentinel.update(deltaMs, player);
            checkSentinelCatch(prevSentinelX, prevSentinelY);
            checkItemProximity();

            if (panicBufferMs <= 0) {
                endPanicBuffer();
            }
        }
    }

    // ---- Movement ----
    public void movePlayer(int dx, int dy) {
        if (currentState != GameState.EXPLORATION && currentState != GameState.PANIC_BUFFER) return;

        player.move(dx, dy);

        checkSentinelCatch(sentinel.getTileX(), sentinel.getTileY());
        if (currentState == GameState.LOSE) return;

        if (itemsCollected >= ITEMS_NEEDED && player.isOnExitTile()) {
            triggerWin();
        }
    }

    // ---- Item proximity ----
    private void checkItemProximity() {
        for (BioEquipment item : items) {
            if (!item.isCollected() && player.isAdjacentTo(item.getTileX(), item.getTileY())) {
                item.setNearby(true);
            } else {
                item.setNearby(false);
            }
        }
    }

    // ---- Interaction ----
    public void interact() {
        if (currentState != GameState.EXPLORATION) return;

        for (BioEquipment item : items) {
            if (!item.isCollected() && player.isAdjacentTo(item.getTileX(), item.getTileY())) {
                startRiddleStasis(item);
                return;
            }
        }
    }

    private void startRiddleStasis(BioEquipment item) {
        currentState = GameState.RIDDLE_STASIS;
        activeRiddle = riddleEngine.generateRiddle(activeMode, kidsSubject, difficulty);
        riddleTargetItem = item;
        notifyListeners();
    }

    // ---- Answer submission ----
    public void submitRiddleAnswer(String answer) {
        if (currentState != GameState.RIDDLE_STASIS || activeRiddle == null) return;

        if (activeRiddle.checkAnswer(answer)) {
            // ✅ SUCCESS
            riddleTargetItem.collect();
            itemsCollected++;

            score += 100; // 🔥 SCORE UPDATE

            activeRiddle = null;
            riddleTargetItem = null;
            currentState = GameState.EXPLORATION;
            notifyListeners();

        } else {
            // ❌ FAIL
            activeRiddle = null;
            riddleTargetItem = null;
            startPanicBuffer();
        }
    }

    public void skipRiddle() {
        if (currentState != GameState.RIDDLE_STASIS) return;
        activeRiddle = null;
        riddleTargetItem = null;
        startPanicBuffer();
    }

    private void startPanicBuffer() {
        currentState = GameState.PANIC_BUFFER;
        panicBufferMs = PANIC_DURATION_MS;
        sentinel.setHuntTarget(player.getTileX(), player.getTileY());
        notifyListeners();
    }

    private void endPanicBuffer() {
        currentState = GameState.EXPLORATION;
        notifyListeners();
    }

    // ---- Collision ----
    private void checkSentinelCatch(int prevSentX, int prevSentY) {
        if (sentinel.overlaps(player)) {
            currentState = GameState.LOSE;
            notifyListeners();
            return;
        }

        if (sentinel.getTileX() == player.getTileX() &&
            sentinel.getTileY() == player.getTileY()) {
            currentState = GameState.LOSE;
            notifyListeners();
        }
    }

    // ---- Lose ----
    public void triggerLose() {
        currentState = GameState.LOSE;
        notifyListeners();
    }

    // ---- Win (FINAL SCORE LOGIC) ----
    private void triggerWin() {

        int secondsLeft = timeRemainingMs / 1000;
        int timeBonus = secondsLeft * 2;

        float multiplier = switch (difficulty) {
            case EASY -> 1.0f;
            case MEDIUM -> 1.5f;
            case HARD -> 2.0f;
        };

        score += timeBonus;
        score = (int)(score * multiplier);

        currentState = GameState.WIN;
        notifyListeners();
    }

    // ---- Start Game ----
    public void startGame(GameMode mode, KidsSubject subject, DifficultyLevel difficulty, float speedMultiplier) {
        this.activeMode = mode;
        this.kidsSubject = subject;
        this.difficulty = difficulty;

        score = 0; // 🔥 RESET SCORE

        sentinel.setSpeedMultiplier(speedMultiplier);

        player.reset(5, 5);
        sentinel.reset(1, 1);
        items.forEach(BioEquipment::reset);

        itemsCollected = 0;
        panicBufferMs = 0;
        activeRiddle = null;
        riddleTargetItem = null;

        currentState = GameState.EXPLORATION;
        notifyListeners();
    }

    public void goToMenu() {
        currentState = GameState.MENU;
        notifyListeners();
    }

    // ---- Time setter (from GameLoop) ----
    public void setRemainingTime(int ms) {
        this.timeRemainingMs = ms;
    }

    // ---- Listeners ----
    public void addListener(GameStateListener l) { listeners.add(l); }
    public void removeListener(GameStateListener l) { listeners.remove(l); }
    private void notifyListeners() { listeners.forEach(l -> l.onStateChanged(currentState)); }

    // ---- Getters ----
    public GameState getCurrentState() { return currentState; }
    public Player getPlayer() { return player; }
    public Sentinel getSentinel() { return sentinel; }
    public List<BioEquipment> getItems() { return items; }
    public int getItemsCollected() { return itemsCollected; }
    public Riddle getActiveRiddle() { return activeRiddle; }
    public int getPanicBufferMs() { return panicBufferMs; }
    public boolean isExitUnlocked() { return itemsCollected >= ITEMS_NEEDED; }
    public int getScore() { return score; }
}