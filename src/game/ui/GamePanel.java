package game.ui;

import game.core.*;
import game.entity.*;
import game.riddle.Riddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * GamePanel is the main rendering canvas.
 * The RiddlePanel is now a JDialog (not a child panel) so it gets
 * its own focus and all clicks/typing work correctly.
 */
public class GamePanel extends JPanel {

    private static final Color COL_FLOOR          = new Color(18, 14, 40);
    private static final Color COL_WALL           = new Color(35, 25, 65);
    private static final Color COL_EXIT_LOCKED    = new Color(40, 40, 60);
    private static final Color COL_EXIT_OPEN      = new Color(50, 200, 120);
    private static final Color COL_PLAYER         = new Color(130, 200, 255);
    private static final Color COL_SENTINEL       = new Color(220, 80, 120);
    private static final Color COL_ITEM           = new Color(200, 160, 80);
    private static final Color COL_ITEM_NEARBY    = new Color(255, 220, 100);
    private static final Color COL_HUD_BG         = new Color(8, 6, 20);
    private static final Color COL_HUD_TEXT       = new Color(200, 180, 240);
    private static final Color COL_PANIC_OVERLAY  = new Color(180, 30, 30, 80);
    private static final Color COL_STASIS_OVERLAY = new Color(60, 40, 120, 60);
    private static final Color COL_GRID           = new Color(30, 22, 55);

    private static final int TILE  = TileMap.TILE_PX;
    private static final int HUD_H = 60;

    private final GameStateManager gsm;
    private       GameLoop         gameLoop;
    private       RiddlePanel      riddlePanel; // JDialog-based, set after window is ready
    private boolean isPaused = false;

    // -------------------------------------------------------
    public GamePanel(GameStateManager gsm) {
        this.gsm = gsm;
        setBackground(COL_HUD_BG);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { handleKey(e.getKeyCode()); }
        });
    }

    /** Called by GameWindow after it has a JFrame reference to pass to RiddlePanel */
    public void initRiddlePanel(JFrame frame) {
        riddlePanel = new RiddlePanel(gsm, frame);

        gsm.addListener(state -> SwingUtilities.invokeLater(() -> {
            if (state == GameState.RIDDLE_STASIS) {
                Riddle r = gsm.getActiveRiddle();
                if (r != null) riddlePanel.show(r);
            } else {
                riddlePanel.hide();
                requestFocusInWindow();
            }
            repaint();
        }));
    }

    public void setGameLoop(GameLoop gl) {
        this.gameLoop = gl;
        this.isPaused = false; // reset pause on new game
    }

    // ---- Pause / Resume -----------------------------------
    public void pauseGame() {
        isPaused = true;
        gsm.setPaused(true);
        repaint();
    }

    public void resumeGame() {
        isPaused = false;
        gsm.setPaused(false);
        repaint();
        requestFocusInWindow();
    }

    public boolean isPaused() {
        return isPaused;
    }

    // ---- Input routing ------------------------------------
    private void handleKey(int key) {
        if (isPaused) return;

        if (gsm.getCurrentState() != GameState.EXPLORATION &&
            gsm.getCurrentState() != GameState.PANIC_BUFFER) return;

        switch (key) {
            case KeyEvent.VK_W, KeyEvent.VK_UP    -> gsm.movePlayer(0, -1);
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> gsm.movePlayer(0,  1);
            case KeyEvent.VK_A, KeyEvent.VK_LEFT  -> gsm.movePlayer(-1, 0);
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> gsm.movePlayer( 1, 0);
            case KeyEvent.VK_E, KeyEvent.VK_SPACE -> gsm.interact();
        }
        repaint();
    }

    // ---- Main render --------------------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // --- SCREEN SHAKE (PANIC FEEDBACK) ---
        GameState state = gsm.getCurrentState();
        if (state == GameState.PANIC_BUFFER) {
            int shakeX = (int)(Math.random() * 6 - 3);
            int shakeY = (int)(Math.random() * 6 - 3);
            g2.translate(shakeX, shakeY);
        }

        // ---- DRAW HUD ----
        drawHUD(g2);

        if (isPaused) {
            drawPauseOverlay(g2);
            return;
        }

        g2.translate(0, HUD_H);

        drawTiles(g2);
        drawItems(g2);
        drawPlayer(g2);
        drawSentinel(g2);
        drawInteractPrompt(g2);

        // --- VIGNETTE (FLASHLIGHT EFFECT) ---
        drawVignette(g2);

        if (state == GameState.RIDDLE_STASIS) {
            drawFullOverlay(g2, COL_STASIS_OVERLAY, "-- TEMPORAL STASIS --");
        } else if (state == GameState.PANIC_BUFFER) {
            drawPanicOverlay(g2);
        }
    }

    // ---- HUD -----------------------------------------------
    private void drawHUD(Graphics2D g) {
        g.setColor(COL_HUD_BG);
        g.fillRect(0, 0, getWidth(), HUD_H);

        String time = gameLoop != null
            ? String.format("%02d:%02d", gameLoop.getMinutes(), gameLoop.getSeconds())
            : "10:00";

        g.setFont(new Font("Monospaced", Font.BOLD, 26));
        g.setColor(timerColor());
        g.drawString("T: " + time, 16, 38);



        g.setFont(new Font("Monospaced", Font.BOLD, 18));
        g.setColor(COL_HUD_TEXT);
        g.drawString("ITEMS: " + gsm.getItemsCollected() + " / 3", 200, 38);

        // SCORE
        g.drawString("SCORE: " + gsm.getScore(), 380, 38);

        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g.setColor(stateColor());
        g.drawString(stateLabel(), 580, 38);

        g.setColor(new Color(60, 40, 100));
        g.fillRect(0, HUD_H - 2, getWidth(), 2);
    }

    private Color timerColor() {
        if (gameLoop == null) return COL_HUD_TEXT;
        int secs = gameLoop.getRemainingMs() / 1000;
        if (secs < 60)  return new Color(255, 80, 80);
        if (secs < 120) return new Color(255, 200, 80);
        return new Color(100, 220, 140);
    }

    private String stateLabel() {
        return switch (gsm.getCurrentState()) {
            case EXPLORATION   -> "[ EXPLORING ]";
            case RIDDLE_STASIS -> "[ TEMPORAL STASIS ]";
            case PANIC_BUFFER  -> "[ !! PANIC !! ]";
            default            -> "";
        };
    }

    private Color stateColor() {
        return switch (gsm.getCurrentState()) {
            case RIDDLE_STASIS -> new Color(160, 120, 255);
            case PANIC_BUFFER  -> new Color(255, 80, 80);
            default            -> COL_HUD_TEXT;
        };
    }

    // ---- Tile renderer ------------------------------------
    private void drawTiles(Graphics2D g) {
        int[][] map = TileMap.getMap();
        for (int row = 0; row < TileMap.ROWS; row++) {
            for (int col = 0; col < TileMap.COLS; col++) {
                int px = col * TILE, py = row * TILE;
                int tile = map[row][col];
                if (tile == TileMap.WALL) {
                    g.setColor(COL_WALL);
                    g.fillRect(px, py, TILE, TILE);
                    g.setColor(new Color(50, 35, 90));
                    g.fillRect(px + 3, py + 3, TILE - 6, TILE - 6);
                } else if (tile == TileMap.EXIT) {
                    g.setColor(gsm.isExitUnlocked() ? COL_EXIT_OPEN : COL_EXIT_LOCKED);
                    g.fillRect(px, py, TILE, TILE);
                    g.setFont(new Font("Monospaced", Font.BOLD, 12));
                    g.setColor(Color.WHITE);
                    g.drawString("EXIT", px + 8, py + 30);
                } else {
                    g.setColor(COL_FLOOR);
                    g.fillRect(px, py, TILE, TILE);
                    g.setColor(COL_GRID);
                    g.drawRect(px, py, TILE, TILE);
                }
            }
        }
    }

    // ---- Items --------------------------------------------
    private void drawItems(Graphics2D g) {
        for (BioEquipment item : gsm.getItems()) {
            if (item.isCollected()) continue;
            int px = item.getTileX() * TILE + TILE / 2;
            int py = item.getTileY() * TILE + TILE / 2;
            Color c = item.isNearby() ? COL_ITEM_NEARBY : COL_ITEM;
            g.setColor(c);
            int[] xs = {px, px+10, px, px-10};
            int[] ys = {py-12, py, py+12, py};
            g.fillPolygon(xs, ys, 4);
            g.setColor(c.darker());
            g.drawPolygon(xs, ys, 4);
            g.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g.setColor(c);
            g.drawString(item.getName(), px - 20, py + 22);
        }
    }

    // ---- Player -------------------------------------------
    private void drawPlayer(Graphics2D g) {
        Player p = gsm.getPlayer();
        int cx = p.getTileX() * TILE + TILE / 2;
        int cy = p.getTileY() * TILE + TILE / 2;
        int r  = 14;
        g.setColor(COL_PLAYER);
        g.fillOval(cx - r, cy - r, r * 2, r * 2);
        g.setColor(COL_HUD_BG);
        g.fillOval(cx + 3, cy - 5, 5, 5);
    }

    // ---- Sentinel -----------------------------------------
    private void drawSentinel(Graphics2D g) {
        Sentinel s = gsm.getSentinel();
        int cx = s.getTileX() * TILE + TILE / 2;
        int cy = s.getTileY() * TILE + TILE / 2;
        Color base = s.getMode() == Sentinel.SentinelMode.HUNT
            ? new Color(255, 60, 80) : COL_SENTINEL;
        int[] bx = {cx, cx+12, cx+8, cx+4, cx-4, cx-8, cx-12};
        int[] by = {cy-18, cy-5, cy+14, cy+14, cy+14, cy-5, cy-5};
        g.setColor(base);
        g.fillPolygon(bx, by, bx.length);
        g.setColor(new Color(255, 240, 60));
        g.fillOval(cx - 7, cy - 14, 5, 4);
        g.fillOval(cx + 2, cy - 14, 5, 4);
    }

    // ---- Interact prompt ----------------------------------
    private void drawInteractPrompt(Graphics2D g) {
        for (BioEquipment item : gsm.getItems()) {
            if (!item.isCollected() && item.isNearby()) {
                int px = item.getTileX() * TILE;
                int py = item.getTileY() * TILE - 16;
                g.setColor(new Color(255, 255, 200, 220));
                g.setFont(new Font("Monospaced", Font.BOLD, 11));
                g.drawString("[E] Interact", px + 2, py);
            }
        }
    }

    // ---- Overlays -----------------------------------------
    private void drawFullOverlay(Graphics2D g, Color tint, String label) {
        int w = TileMap.COLS * TILE, h = TileMap.ROWS * TILE;
        g.setColor(tint);
        g.fillRect(0, 0, w, h);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(new Color(200, 180, 255, 200));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label, (w - fm.stringWidth(label)) / 2, 30);
    }

    private void drawPanicOverlay(Graphics2D g) {
        int w = TileMap.COLS * TILE, h = TileMap.ROWS * TILE;
        g.setColor(COL_PANIC_OVERLAY);
        g.fillRect(0, 0, w, h);
        g.setColor(new Color(200, 30, 30, 180));
        g.setStroke(new BasicStroke(8));
        g.drawRect(4, 4, w - 8, h - 8);
        g.setStroke(new BasicStroke(1));
        int secs = (int) Math.ceil(gsm.getPanicBufferMs() / 1000.0);
        String msg = "!! PANIC - " + secs + "s !!";
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(new Color(255, 100, 100));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2 - 10);
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g.setColor(new Color(255, 200, 200));
        String sub = "The Sentinel is heading to your location!";
        g.drawString(sub, (w - g.getFontMetrics().stringWidth(sub)) / 2, h / 2 + 18);
    }

    private void drawPauseOverlay(Graphics2D g) {
        int w = getWidth();
        int h = getHeight();

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, w, h);

        String text = "PAUSED";
        g.setFont(new Font("Monospaced", Font.BOLD, 40));
        g.setColor(Color.WHITE);

        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2;

        g.drawString(text, x, y);
    }

    // ---- Vignette (flashlight effect) ---------------------
    private void drawVignette(Graphics2D g) {
        int w = getWidth();
        int h = getHeight();
        game.entity.Player p = gsm.getPlayer();

        int cx = p.getTileX() * TILE + TILE / 2;
        int cy = p.getTileY() * TILE + TILE / 2;

        // Fixed flashlight radius (battery removed)
        int radius = 400;

        float[] dist = {0.0f, 0.4f, 1.0f};
        Color[] colors = {
            new Color(255, 255, 220, 30),
            new Color(0, 0, 0, 0),
            new Color(0, 0, 0, 180)
        };

        RadialGradientPaint rgp = new RadialGradientPaint(
            new java.awt.geom.Point2D.Double(cx, cy),
            radius, dist, colors);

        g.setPaint(rgp);
        g.fillRect(0, 0, w, h);
    }

}
