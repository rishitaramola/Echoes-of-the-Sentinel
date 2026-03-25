package game.ui;

import game.core.GameLoop;
import game.core.GameState;
import game.core.GameStateManager;

import javax.swing.*;
import java.awt.*;

/**
 * The top-level application window (JFrame).
 * Holds a CardLayout that swaps between the MenuPanel and GamePanel.
 */
public class GameWindow extends JFrame {

    private final GameStateManager gsm   = new GameStateManager();
    private final CardLayout       cards = new CardLayout();
    private final JPanel           root  = new JPanel(cards);

    private final MenuPanel menuPanel;
    private final GamePanel gamePanel;
    private       GameLoop  gameLoop;

    private static final String MENU = "MENU";
    private static final String GAME = "GAME";

    public GameWindow() {
        super("Echoes of the Sentinel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(gsm);

        root.add(menuPanel, MENU);
        root.add(gamePanel, GAME);
        add(root);
        pack();
        setLocationRelativeTo(null);

        // Pass 'this' JFrame to GamePanel so RiddlePanel (JDialog) has a parent
        gamePanel.initRiddlePanel(this);

        // Listen for win/lose
        gsm.addListener(state -> SwingUtilities.invokeLater(() -> {
            if (state == GameState.WIN || state == GameState.LOSE) {
                handleGameOver(state);
            }
        }));

        showMenu();
    }

    public void showMenu() {
        stopGame();
        cards.show(root, MENU);
        menuPanel.requestFocusInWindow();
    }

    public void startGame() {
        gsm.startGame();
        gameLoop = new GameLoop(gsm, gamePanel::repaint);

        int w = game.core.TileMap.COLS * game.core.TileMap.TILE_PX;
        int h = game.core.TileMap.ROWS * game.core.TileMap.TILE_PX + 60;
        gamePanel.setPreferredSize(new Dimension(w, h));
        pack();
        setLocationRelativeTo(null);

        gamePanel.setGameLoop(gameLoop);
        cards.show(root, GAME);
        gamePanel.requestFocusInWindow();
        gameLoop.start();
    }

    private void stopGame() {
        if (gameLoop != null) { gameLoop.stop(); gameLoop = null; }
    }

    private void handleGameOver(GameState result) {
        stopGame();
        String msg   = result == GameState.WIN
            ? "YOU ESCAPED!\nYou retrieved all needed equipment and survived."
            : "YOU WERE CAUGHT!\nThe Sentinel found you. Better luck next time.";
        String title = result == GameState.WIN ? "Mission Complete" : "Mission Failed";

        int choice = JOptionPane.showOptionDialog(
            this, msg, title,
            JOptionPane.YES_NO_OPTION,
            result == GameState.WIN ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE,
            null,
            new String[]{"Play Again", "Main Menu"},
            "Play Again"
        );

        if (choice == JOptionPane.YES_OPTION) startGame();
        else showMenu();
    }
}
