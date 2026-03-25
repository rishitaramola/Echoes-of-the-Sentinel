package game.entity;

import game.core.TileMap;
import java.util.*;

/**
 * The Sentinel — a sound-reactive alien AI.
 *
 * Behaviour states:
 *   PATROL  — Walks a random path through the house.
 *   HUNT    — Moves directly toward a known target tile.
 *             Triggered when the player fails a riddle (Panic Buffer).
 *
 * Movement is tile-based BFS pathfinding.
 * Move speed: one tile per MOVE_INTERVAL_MS milliseconds.
 */
public class Sentinel {

    public enum SentinelMode { PATROL, HUNT }

    private static final int MOVE_INTERVAL_MS = 600;  // ms between tile moves (slower = easier)
    private static final int PATROL_INTERVAL_MS = 2000; // recalculate patrol target every 2s

    private int          tileX;
    private int          tileY;
    private SentinelMode mode   = SentinelMode.PATROL;

    private int          moveTimer   = 0;
    private int          patrolTimer = 0;

    // Pathfinding
    private List<int[]>  path        = new ArrayList<>();
    private int          huntTargetX = -1;
    private int          huntTargetY = -1;

    private final Random rng = new Random();

    // -------------------------------------------------------
    public Sentinel(int startX, int startY) {
        this.tileX = startX;
        this.tileY = startY;
    }

    // ---- Called once per game tick (only in EXPLORATION and PANIC_BUFFER) ----
    public void update(int deltaMs, Player player) {
        moveTimer   += deltaMs;
        patrolTimer += deltaMs;

        if (mode == SentinelMode.PATROL) {
            updatePatrol();
        } else {
            updateHunt();
        }
    }

    // ---- PATROL logic: wander to random walkable tiles -----
    private void updatePatrol() {
        if (patrolTimer >= PATROL_INTERVAL_MS || path.isEmpty()) {
            patrolTimer = 0;
            // Pick a random walkable target
            int tx, ty;
            do {
                tx = rng.nextInt(TileMap.COLS);
                ty = rng.nextInt(TileMap.ROWS);
            } while (!TileMap.isWalkable(tx, ty));
            path = bfs(tileX, tileY, tx, ty);
        }

        if (moveTimer >= MOVE_INTERVAL_MS && !path.isEmpty()) {
            moveTimer = 0;
            int[] next = path.remove(0);
            tileX = next[0];
            tileY = next[1];
        }
    }

    // ---- HUNT logic: chase known target location -----------
    private void updateHunt() {
        if (path.isEmpty() && huntTargetX != -1) {
            path = bfs(tileX, tileY, huntTargetX, huntTargetY);
        }

        if (moveTimer >= MOVE_INTERVAL_MS / 2 && !path.isEmpty()) { // Faster in hunt mode
            moveTimer = 0;
            int[] next = path.remove(0);
            tileX = next[0];
            tileY = next[1];
        }

        // Once it reaches the target, revert to patrol
        if (path.isEmpty()) {
            mode        = SentinelMode.PATROL;
            huntTargetX = -1;
            huntTargetY = -1;
        }
    }

    // ---- BFS Pathfinding -----------------------------------
    private List<int[]> bfs(int fromX, int fromY, int toX, int toY) {
        int[][] prev  = new int[TileMap.ROWS][TileMap.COLS];
        int[][] prevX = new int[TileMap.ROWS][TileMap.COLS];
        int[][] prevY = new int[TileMap.ROWS][TileMap.COLS];
        for (int[] row : prev)  Arrays.fill(row, -1);

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{fromX, fromY});
        prev[fromY][fromX] = 0;

        int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int cx = cur[0], cy = cur[1];

            if (cx == toX && cy == toY) {
                // Reconstruct path
                List<int[]> result = new ArrayList<>();
                int rx = toX, ry = toY;
                while (rx != fromX || ry != fromY) {
                    result.add(0, new int[]{rx, ry});
                    int nx = prevX[ry][rx];
                    int ny = prevY[ry][rx];
                    rx = nx; ry = ny;
                }
                return result;
            }

            for (int[] d : dirs) {
                int nx = cx + d[0], ny = cy + d[1];
                if (TileMap.isWalkable(nx, ny) && prev[ny][nx] == -1) {
                    prev[ny][nx]  = 1;
                    prevX[ny][nx] = cx;
                    prevY[ny][nx] = cy;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return new ArrayList<>(); // No path found
    }

    // ---- Called when panic buffer starts -------------------
    public void setHuntTarget(int tx, int ty) {
        mode        = SentinelMode.HUNT;
        huntTargetX = tx;
        huntTargetY = ty;
        path        = bfs(tileX, tileY, tx, ty);
    }

    // ---- Collision with player -----------------------------
    public boolean overlaps(Player player) {
        return tileX == player.getTileX() && tileY == player.getTileY();
    }

    // ---- Reset for new game --------------------------------
    public void reset(int startX, int startY) {
        tileX       = startX;
        tileY       = startY;
        mode        = SentinelMode.PATROL;
        path        = new ArrayList<>();
        moveTimer   = 0;
        patrolTimer = 0;
        huntTargetX = -1;
        huntTargetY = -1;
    }

    // ---- Getters -------------------------------------------
    public int          getTileX()  { return tileX; }
    public int          getTileY()  { return tileY; }
    public SentinelMode getMode()   { return mode; }
}
