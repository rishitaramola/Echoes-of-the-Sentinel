package game.core;

/**
 * TileMap defines the layout of the game world as a 2D grid.
 *
 * Tile values:
 *   0 = floor  (walkable)
 *   1 = wall   (solid)
 *   2 = exit   (player wins if they step here with 3 items)
 *
 * The map is 15 wide x 15 tall tiles.
 * Tile size in pixels is defined in the renderer (GamePanel).
 */
public class TileMap {

    public static final int COLS     = 15;
    public static final int ROWS     = 15;
    public static final int TILE_PX  = 48;   // pixels per tile

    public static final int FLOOR = 0;
    public static final int WALL  = 1;
    public static final int EXIT  = 2;

    // Exit tile position (matches the EXIT cell below)
    public static final int EXIT_TILE_X = 7;
    public static final int EXIT_TILE_Y = 0;

    // Map layout  (row 0 = top)
    private static final int[][] MAP = {
        {1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1},  // row 0  (exit top-centre)
        {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},  // row 1
        {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1},  // row 2
        {1, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1},  // row 3
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},  // row 4
        {1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1},  // row 5
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},  // row 6
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},  // row 7
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},  // row 8
        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},  // row 9
        {1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1},  // row 10
        {1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1},  // row 11
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},  // row 12
        {1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1},  // row 13
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},  // row 14
    };

    // ---- Tile queries ----------------------------------------
    public static int getTile(int col, int row) {
        if (col < 0 || col >= COLS || row < 0 || row >= ROWS) return WALL;
        return MAP[row][col];
    }

    public static boolean isWalkable(int col, int row) {
        int tile = getTile(col, row);
        return tile == FLOOR || tile == EXIT;
    }

    public static boolean isExit(int col, int row) {
        return getTile(col, row) == EXIT;
    }

    public static int[][] getMap() { return MAP; }
}
