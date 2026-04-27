package game.ui;

import game.core.*;

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

    private JButton pauseBtn, resumeBtn, restartBtn;
    private JPanel controlPanel;

    private static final String MENU = "MENU";
    private static final String GAME = "GAME";

    private GameMode lastMode = GameMode.FUN_PLAY;
    private KidsSubject lastSubject = KidsSubject.MATHS;
    private DifficultyLevel lastDifficulty = DifficultyLevel.EASY;
    private float lastSpeed = 1.0f;

    // -------------------------------------------------------

    private void restartGame() {
        stopGame();
        gamePanel.resumeGame();
        startGame(lastMode, lastSubject, lastDifficulty, lastSpeed);
    }

    // -------------------------------------------------------

    public GameWindow() {
        super("Echoes of the Sentinel");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        menuPanel = new MenuPanel(this);
        gamePanel = new GamePanel(gsm);

        root.add(menuPanel, MENU);
        root.add(gamePanel, GAME);

        setLayout(new BorderLayout());
        add(root, BorderLayout.CENTER);

        // ---- CONTROL PANEL ----
        controlPanel = new JPanel();

        pauseBtn = new JButton("Pause");
        resumeBtn = new JButton("Resume");
        restartBtn = new JButton("Restart");

        pauseBtn.setToolTipText("Pause the game");
        resumeBtn.setToolTipText("Resume the game");
        restartBtn.setToolTipText("Restart the game");

        controlPanel.add(pauseBtn);
        controlPanel.add(resumeBtn);
        controlPanel.add(restartBtn);

        add(controlPanel, BorderLayout.NORTH);
        controlPanel.setVisible(false);

        // ---- BUTTON ACTIONS ----
        pauseBtn.addActionListener(e -> {
            if (gsm.getCurrentState() != GameState.RIDDLE_STASIS) {
                gamePanel.pauseGame();
            }
        });

        resumeBtn.addActionListener(e -> {
            if (gamePanel != null) gamePanel.resumeGame();
        });

        restartBtn.addActionListener(e -> {
            gamePanel.resumeGame();
            startGame(lastMode, lastSubject, lastDifficulty, lastSpeed);
        });

        pack();
        setLocationRelativeTo(null);

        // ---- RIDDLE PANEL INIT ----
        gamePanel.initRiddlePanel(this);

        // ---- STATE LISTENER ----
        gsm.addListener(state -> SwingUtilities.invokeLater(() -> {

            updatePauseButtonState();

            if (state == GameState.WIN || state == GameState.LOSE) {
                handleGameOver(state);
            }
        }));

        showMenu();
    }

    // -------------------------------------------------------

    public void showMenu() {
        stopGame();
        controlPanel.setVisible(false);
        cards.show(root, MENU);
        menuPanel.requestFocusInWindow();
    }

    // -------------------------------------------------------

    public void startGame(GameMode mode, KidsSubject subject,
                          DifficultyLevel difficulty, float speedMultiplier) {

        lastMode = mode;
        lastSubject = subject;
        lastDifficulty = difficulty;
        lastSpeed = speedMultiplier;

        gsm.startGame(mode, subject, difficulty, speedMultiplier);

        gameLoop = new GameLoop(gsm, gamePanel::repaint);

        int w = TileMap.COLS * TileMap.TILE_PX;
        int h = TileMap.ROWS * TileMap.TILE_PX + 60;

        gamePanel.setPreferredSize(new Dimension(w, h));

        pack();
        setLocationRelativeTo(null);

        gamePanel.setGameLoop(gameLoop);
        gamePanel.resumeGame();

        controlPanel.setVisible(true);

        cards.show(root, GAME);
        gamePanel.requestFocusInWindow();

        gameLoop.start();
    }

    // -------------------------------------------------------

    private void stopGame() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }

    // -------------------------------------------------------

    private void handleGameOver(GameState result) {
        stopGame();

        int score = gsm.getScore();

        String msg = result == GameState.WIN
                ? "YOU ESCAPED!\nYou retrieved all needed equipment and survived.\n\nScore: " + score
                : "YOU WERE CAUGHT!\nThe Sentinel found you. Better luck next time.\n\nScore: " + score;

        String title = result == GameState.WIN
                ? "Mission Complete"
                : "Mission Failed";

        int choice = JOptionPane.showOptionDialog(
                this,
                msg,
                title,
                JOptionPane.YES_NO_OPTION,
                result == GameState.WIN
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.ERROR_MESSAGE,
                null,
                new String[]{"Play Again", "Main Menu"},
                "Play Again"
        );

        if (choice == JOptionPane.YES_OPTION) {
            startGame(lastMode, lastSubject, lastDifficulty, lastSpeed);
        } else {
            showMenu();
        }
    }

    // -------------------------------------------------------

    private void updatePauseButtonState() {
        GameState state = gsm.getCurrentState();

        if (state == GameState.RIDDLE_STASIS) {
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(false);
            pauseBtn.setToolTipText("Cannot pause during puzzle");
        } else {
            pauseBtn.setEnabled(true);
            resumeBtn.setEnabled(true);
            pauseBtn.setToolTipText("Pause the game");
        }
    }
}
