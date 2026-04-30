package game.entity;

import game.core.TileMap;

/**
 * The player character.
 *
 * Stored in tile coordinates (tileX, tileY).
 * Pixel coordinates are derived in the renderer: px = tileX * TILE_PX + TILE_PX/2.
 */
public class Player {

    private int tileX;
    private int tileY;

    public Player(int startX, int startY) {
        this.tileX = startX;
        this.tileY = startY;
    }

    // ---- Movement (tile-based, wall-checked) ---------------
    public void move(int dx, int dy) {
        int newX = tileX + dx;
        int newY = tileY + dy;
        if (TileMap.isWalkable(newX, newY)) {
            tileX = newX;
            tileY = newY;
        }
    }

    // ---- Proximity check (adjacent = within 1 tile) --------
    public boolean isAdjacentTo(int tx, int ty) {
        return Math.abs(tileX - tx) <= 1 && Math.abs(tileY - ty) <= 1;
    }

    // ---- Win condition: standing on the exit tile ----------
    public boolean isOnExitTile() {
        return TileMap.isExit(tileX, tileY);
    }

    // ---- Reset for a new game run --------------------------
    public void reset(int startX, int startY) {
        tileX = startX;
        tileY = startY;
    }

    // ---- Getters -------------------------------------------
    public int getTileX() { return tileX; }
    public int getTileY() { return tileY; }
}
