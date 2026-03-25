package game.core;

import game.entity.Player;
import game.entity.Sentinel;
import game.entity.BioEquipment;
import game.riddle.RiddleEngine;
import game.riddle.Riddle;

import java.util.ArrayList;
import java.util.List;

/**
 * GameStateManager is the central controller for the game.
 * It holds references to all entities and manages state transitions.
 *
 * Architecture: Single-threaded. The GameLoop calls update() every tick.
 * State flags (isRiddleActive, isPanicBuffer) control which systems run.
 */
public class GameStateManager {

    // --- Core State ---
    private GameState currentState = GameState.MENU;
    private int       panicBufferMs = 0;        // countdown in milliseconds
    private static final int PANIC_DURATION_MS = 3000;

    // --- Game Entities ---
    private final Player         player;
    private final Sentinel       sentinel;
    private final List<BioEquipment> items;
    private final RiddleEngine   riddleEngine;

    // --- Session Data ---
    private int     itemsCollected  = 0;
    private static final int ITEMS_NEEDED = 3;

    // --- Current active riddle (null when not in stasis) ---
    private Riddle  activeRiddle    = null;
    private BioEquipment riddleTargetItem = null;

    // --- Listener so UI can react to state changes ---
    private final List<GameStateListener> listeners = new ArrayList<>();

    // -------------------------------------------------------
    public GameStateManager() {
        player       = new Player(5, 5);           // starting tile
        sentinel     = new Sentinel(1, 1);         // starting tile
        items        = createItems();
        riddleEngine = new RiddleEngine();
    }

    // ---- Factory: place 4 items on the map ----------------
    private List<BioEquipment> createItems() {
        List<BioEquipment> list = new ArrayList<>();
        list.add(new BioEquipment("Oxygen Mask",     9,  2, 0));
        list.add(new BioEquipment("Neural Implant",  2,  9, 1));
        list.add(new BioEquipment("Bio-Scanner",     9,  9, 2));
        list.add(new BioEquipment("Stasis Pod",      5, 12, 3));
        return list;
    }

    // ---- Called once per game tick by the GameLoop --------
    public void update(int deltaMs) {
        if (currentState == GameState.EXPLORATION) {
            sentinel.update(deltaMs, player);
            checkSentinelCatch();
            checkItemProximity();
        } else if (currentState == GameState.PANIC_BUFFER) {
            panicBufferMs -= deltaMs;
            sentinel.update(deltaMs, player);       // Sentinel moves during panic!
            if (panicBufferMs <= 0) {
                endPanicBuffer();
            }
        }
        // RIDDLE_STASIS: sentinel is frozen, nothing entity-wise runs.
    }

    // ---- Player movement (called from input handler) ------
    public void movePlayer(int dx, int dy) {
        if (currentState != GameState.EXPLORATION) return;
        player.move(dx, dy);
        // Check if player walked onto the exit tile while holding enough items
        if (itemsCollected >= ITEMS_NEEDED && player.isOnExitTile()) {
            triggerWin();
        }
    }

    // ---- Item proximity check -----------------------------
    private void checkItemProximity() {
        for (BioEquipment item : items) {
            if (!item.isCollected() && player.isAdjacentTo(item.getTileX(), item.getTileY())) {
                item.setNearby(true);
            } else {
                item.setNearby(false);
            }
        }
    }

    // ---- Player presses INTERACT key ----------------------
    public void interact() {
        if (currentState != GameState.EXPLORATION) return;
        for (BioEquipment item : items) {
            if (!item.isCollected() && player.isAdjacentTo(item.getTileX(), item.getTileY())) {
                startRiddleStasis(item);
                return;
            }
        }
    }

    // ---- BEGIN Temporal Stasis ----------------------------
    private void startRiddleStasis(BioEquipment item) {
        currentState     = GameState.RIDDLE_STASIS;
        activeRiddle     = riddleEngine.getRiddleFor(item.getRiddleIndex());
        riddleTargetItem = item;
        notifyListeners();
    }

    // ---- Player submits riddle answer ---------------------
    public void submitRiddleAnswer(String answer) {
        if (currentState != GameState.RIDDLE_STASIS || activeRiddle == null) return;

        if (activeRiddle.checkAnswer(answer)) {
            // --- SUCCESS ---
            riddleTargetItem.collect();
            itemsCollected++;
            activeRiddle     = null;
            riddleTargetItem = null;
            currentState     = GameState.EXPLORATION;
            if (itemsCollected >= ITEMS_NEEDED) {
                // Unlock exit — notify UI to show exit tile
                notifyListeners();
            }
            notifyListeners();
        } else {
            // --- FAILURE: trigger Panic Buffer ---
            activeRiddle     = null;
            riddleTargetItem = null;
            startPanicBuffer();
        }
    }

    // ---- Skip/abandon current riddle ----------------------
    public void skipRiddle() {
        if (currentState != GameState.RIDDLE_STASIS) return;
        activeRiddle     = null;
        riddleTargetItem = null;
        currentState     = GameState.EXPLORATION;
        notifyListeners();
    }

    // ---- BEGIN Panic Buffer (3-second freeze on player) ---
    private void startPanicBuffer() {
        currentState  = GameState.PANIC_BUFFER;
        panicBufferMs = PANIC_DURATION_MS;
        // Tell sentinel to rush toward the player's position
        sentinel.setHuntTarget(player.getTileX(), player.getTileY());
        notifyListeners();
    }

    // ---- END Panic Buffer ---------------------------------
    private void endPanicBuffer() {
        currentState = GameState.EXPLORATION;
        notifyListeners();
    }

    // ---- Sentinel catches player --------------------------
    private void checkSentinelCatch() {
        if (sentinel.overlaps(player)) {
            currentState = GameState.LOSE;
            notifyListeners();
        }
    }

    // ---- Timer runs out -----------------------------------
    public void triggerLose() {
        currentState = GameState.LOSE;
        notifyListeners();
    }

    // ---- Player escapes -----------------------------------
    private void triggerWin() {
        currentState = GameState.WIN;
        notifyListeners();
    }

    // ---- Start a new game run -----------------------------
    public void startGame() {
        player.reset(5, 5);
        sentinel.reset(1, 1);
        items.forEach(BioEquipment::reset);
        itemsCollected   = 0;
        panicBufferMs    = 0;
        activeRiddle     = null;
        riddleTargetItem = null;
        currentState     = GameState.EXPLORATION;
        notifyListeners();
    }

    // ---- Return to menu -----------------------------------
    public void goToMenu() {
        currentState = GameState.MENU;
        notifyListeners();
    }

    // ---- Listener management ------------------------------
    public void addListener(GameStateListener l)    { listeners.add(l); }
    public void removeListener(GameStateListener l) { listeners.remove(l); }
    private void notifyListeners() { listeners.forEach(l -> l.onStateChanged(currentState)); }

    // ---- Getters ------------------------------------------
    public GameState        getCurrentState()    { return currentState; }
    public Player           getPlayer()          { return player; }
    public Sentinel         getSentinel()        { return sentinel; }
    public List<BioEquipment> getItems()         { return items; }
    public int              getItemsCollected()  { return itemsCollected; }
    public Riddle           getActiveRiddle()    { return activeRiddle; }
    public int              getPanicBufferMs()   { return panicBufferMs; }
    public boolean          isExitUnlocked()     { return itemsCollected >= ITEMS_NEEDED; }
}
