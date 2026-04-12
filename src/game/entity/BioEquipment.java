package game.entity;

/**
 * A collectible bio-equipment item placed on the map.
 * The player must interact with it and solve a riddle to collect it.
 */
public class BioEquipment {

    private final String name;
    private final int    tileX;
    private final int    tileY;
    private final int    riddleIndex; // which riddle to pull from RiddleEngine

    private boolean collected = false;
    private boolean nearby    = false; // true when player is adjacent (show prompt)

    public BioEquipment(String name, int tileX, int tileY, int riddleIndex) {
        this.name        = name;
        this.tileX       = tileX;
        this.tileY       = tileY;
        this.riddleIndex = riddleIndex;
    }

    public void collect()             { this.collected = true; }
    public void setNearby(boolean b)  { this.nearby = b; }
    public void reset()               { this.collected = false; this.nearby = false; }

    public String  getName()         { return name; }
    public int     getTileX()        { return tileX; }
    public int     getTileY()        { return tileY; }
    public int     getRiddleIndex()  { return riddleIndex; }
    public boolean isCollected()     { return collected; }
    public boolean isNearby()        { return nearby; }
}
