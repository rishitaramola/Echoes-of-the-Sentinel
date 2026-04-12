package game.ui;

import game.core.GameMode;
import game.core.KidsSubject;
import game.core.DifficultyLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * MenuPanel — hierarchical main menu.
 *
 * View 1 (MAIN):     Sentinel Speed Slider + [Kids Section] [Fun Play] [Quit]
 * View 2 (KIDS):     [English] [Maths] [Riddles] [Back]
 * View 3 (FUNPLAY):  [Easy] [Medium] [Hard] [Back]
 */
public class MenuPanel extends JPanel {

    // ---- Colour Palette ----
    private static final Color BG_COLOR      = new Color(10, 8, 28);
    private static final Color PANEL_BG      = new Color(18, 14, 45);
    private static final Color TITLE_COLOR   = new Color(190, 150, 255);
    private static final Color SUB_COLOR     = new Color(140, 200, 255);
    private static final Color TEXT_COLOR    = new Color(180, 170, 210);
    private static final Color ACCENT_COLOR  = new Color(80, 200, 180);
    private static final Color BTN_COLOR     = new Color(55, 40, 100);
    private static final Color BTN_HOVER     = new Color(90, 65, 155);
    private static final Color BTN_KIDS      = new Color(30, 90, 160);
    private static final Color BTN_KIDS_H    = new Color(50, 130, 220);
    private static final Color BTN_FUN       = new Color(110, 40, 60);
    private static final Color BTN_FUN_H     = new Color(160, 60, 90);
    private static final Color SLIDER_TRACK  = new Color(70, 50, 130);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     cardPanel  = new JPanel(cardLayout);
    private       JSlider    speedSlider;

    public MenuPanel(GameWindow window) {
        setBackground(BG_COLOR);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(720, 720));

        cardPanel.setBackground(BG_COLOR);
        cardPanel.add(buildMainView(window), "MAIN");
        cardPanel.add(buildKidsView(window), "KIDS");
        cardPanel.add(buildFunView(window),  "FUN");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "MAIN");
    }

    // =========================================================
    //  VIEW 1 — MAIN
    // =========================================================
    private JPanel buildMainView(GameWindow window) {
        JPanel panel = darkPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultGbc();

        // Title block
        gbc.gridy = 0;
        panel.add(buildTitle("ECHOES OF THE SENTINEL",
                             "A Time-Stasis Stealth Challenge"), gbc);

        // Spacer
        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Instructions
        gbc.gridy = 2;
        panel.add(buildInstructions(), gbc);

        // Spacer
        gbc.gridy = 3;
        panel.add(Box.createVerticalStrut(18), gbc);

        // Speed slider
        gbc.gridy = 4;
        panel.add(buildSpeedPanel(), gbc);

        // Spacer
        gbc.gridy = 5;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Kids Section button
        JButton kidsBtn = createButton("PLAY  -  KIDS SECTION", BTN_KIDS, BTN_KIDS_H);
        kidsBtn.addActionListener(e -> cardLayout.show(cardPanel, "KIDS"));
        gbc.gridy = 6;
        panel.add(kidsBtn, gbc);

        // Fun Play button
        JButton funBtn = createButton("PLAY  -  FUN PLAY", BTN_FUN, BTN_FUN_H);
        funBtn.addActionListener(e -> cardLayout.show(cardPanel, "FUN"));
        gbc.gridy = 7;
        panel.add(funBtn, gbc);

        // Quit button
        JButton quitBtn = createButton("QUIT", BTN_COLOR, BTN_HOVER);
        quitBtn.addActionListener(e -> System.exit(0));
        gbc.gridy = 8;
        panel.add(quitBtn, gbc);

        return panel;
    }

    // =========================================================
    //  VIEW 2 — KIDS SECTION
    // =========================================================
    private JPanel buildKidsView(GameWindow window) {
        JPanel panel = darkPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultGbc();

        gbc.gridy = 0;
        panel.add(buildTitle("KIDS SECTION",
                             "Choose your subject!"), gbc);

        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(20), gbc);

        // Category description panel
        gbc.gridy = 2;
        panel.add(buildCategoryDesc(
            "English  -  Missing letters, unscramble words\n" +
            "Maths    -  Addition, subtraction, multiplication\n" +
            "Riddles  -  Fun word puzzles and classic riddles"
        ), gbc);

        gbc.gridy = 3;
        panel.add(Box.createVerticalStrut(20), gbc);

        // English
        JButton engBtn = createButton("ENGLISH", BTN_KIDS, BTN_KIDS_H);
        engBtn.addActionListener(e -> window.startGame(GameMode.KIDS, KidsSubject.ENGLISH, DifficultyLevel.EASY, getSpeed()));
        gbc.gridy = 4;
        panel.add(engBtn, gbc);

        // Maths
        JButton mathBtn = createButton("MATHS", BTN_KIDS, BTN_KIDS_H);
        mathBtn.addActionListener(e -> window.startGame(GameMode.KIDS, KidsSubject.MATHS, DifficultyLevel.EASY, getSpeed()));
        gbc.gridy = 5;
        panel.add(mathBtn, gbc);

        // Riddles
        JButton riddleBtn = createButton("RIDDLES", BTN_KIDS, BTN_KIDS_H);
        riddleBtn.addActionListener(e -> window.startGame(GameMode.KIDS, KidsSubject.RIDDLES, DifficultyLevel.EASY, getSpeed()));
        gbc.gridy = 6;
        panel.add(riddleBtn, gbc);

        gbc.gridy = 7;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Back
        JButton backBtn = createButton("BACK TO MAIN MENU", BTN_COLOR, BTN_HOVER);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        gbc.gridy = 8;
        panel.add(backBtn, gbc);

        return panel;
    }

    // =========================================================
    //  VIEW 3 — FUN PLAY
    // =========================================================
    private JPanel buildFunView(GameWindow window) {
        JPanel panel = darkPanel(new GridBagLayout());
        GridBagConstraints gbc = defaultGbc();

        gbc.gridy = 0;
        panel.add(buildTitle("FUN PLAY",
                             "Choose your difficulty!"), gbc);

        gbc.gridy = 1;
        panel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 2;
        panel.add(buildCategoryDesc(
            "Easy    -  Classic riddles + simple arithmetic\n" +
            "Medium  -  Lateral riddles + boolean logic\n" +
            "Hard    -  XOR gates + deep trick questions"
        ), gbc);

        gbc.gridy = 3;
        panel.add(Box.createVerticalStrut(20), gbc);

        // Easy
        JButton easyBtn = createButton("EASY", new Color(30, 120, 60), new Color(45, 170, 90));
        easyBtn.addActionListener(e -> window.startGame(GameMode.FUN_PLAY, KidsSubject.MATHS, DifficultyLevel.EASY, getSpeed()));
        gbc.gridy = 4;
        panel.add(easyBtn, gbc);

        // Medium
        JButton medBtn = createButton("MEDIUM", new Color(140, 100, 20), new Color(200, 145, 30));
        medBtn.addActionListener(e -> window.startGame(GameMode.FUN_PLAY, KidsSubject.MATHS, DifficultyLevel.MEDIUM, getSpeed()));
        gbc.gridy = 5;
        panel.add(medBtn, gbc);

        // Hard
        JButton hardBtn = createButton("HARD", BTN_FUN, BTN_FUN_H);
        hardBtn.addActionListener(e -> window.startGame(GameMode.FUN_PLAY, KidsSubject.MATHS, DifficultyLevel.HARD, getSpeed()));
        gbc.gridy = 6;
        panel.add(hardBtn, gbc);

        gbc.gridy = 7;
        panel.add(Box.createVerticalStrut(10), gbc);

        // Back
        JButton backBtn = createButton("BACK TO MAIN MENU", BTN_COLOR, BTN_HOVER);
        backBtn.addActionListener(e -> cardLayout.show(cardPanel, "MAIN"));
        gbc.gridy = 8;
        panel.add(backBtn, gbc);

        return panel;
    }

    // =========================================================
    //  Helpers — components
    // =========================================================
    private JPanel buildTitle(String title, String subtitle) {
        JPanel p = darkPanel(new GridBagLayout());
        GridBagConstraints g = defaultGbc();

        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 26));
        lbl.setForeground(TITLE_COLOR);
        g.gridy = 0;
        p.add(lbl, g);

        JLabel sub = new JLabel(subtitle, SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 13));
        sub.setForeground(SUB_COLOR);
        g.gridy = 1;
        g.insets = new Insets(4, 0, 0, 0);
        p.add(sub, g);

        return p;
    }

    private JLabel buildInstructions() {
        JLabel inst = new JLabel(
            "<html><div style='text-align:center;width:400px;line-height:1.6'>" +
            "Collect <b>3 of 4</b> bio-equipment pieces.<br>" +
            "Escape to the <b>top exit</b> within <b>10 minutes</b>.<br>" +
            "Solve questions to collect items.<br>" +
            "Fail a question and the Sentinel hunts you for 3 seconds!<br><br>" +
            "<b>WASD / Arrow Keys</b> = Move &nbsp;|&nbsp; <b>E / Space</b> = Interact" +
            "</div></html>",
            SwingConstants.CENTER
        );
        inst.setFont(new Font("SansSerif", Font.PLAIN, 13));
        inst.setForeground(TEXT_COLOR);
        return inst;
    }

    private JLabel buildCategoryDesc(String text) {
        String html = "<html><div style='text-align:center;width:380px;line-height:1.7'>" +
                text.replace("\n", "<br>") + "</div></html>";
        JLabel lbl = new JLabel(html, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    private JPanel buildSpeedPanel() {
        JPanel p = darkPanel(new GridBagLayout());
        GridBagConstraints g = defaultGbc();
        g.insets = new Insets(2, 0, 2, 0);

        JLabel title = new JLabel("Sentinel Speed", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 15));
        title.setForeground(ACCENT_COLOR);
        g.gridy = 0;
        p.add(title, g);

        speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setBackground(BG_COLOR);
        speedSlider.setForeground(TEXT_COLOR);
        speedSlider.setFont(new Font("Monospaced", Font.PLAIN, 11));
        speedSlider.setPreferredSize(new Dimension(400, 55));
        speedSlider.setFocusable(false);

        // Custom label table
        java.util.Hashtable<Integer, JLabel> labels = new java.util.Hashtable<>();
        for (int i = 1; i <= 10; i++) {
            JLabel l = new JLabel(i + "X");
            l.setForeground(i <= 3 ? new Color(100, 220, 140) :
                            i <= 6 ? new Color(255, 200, 80)  :
                                     new Color(255, 80, 80));
            l.setFont(new Font("Monospaced", Font.BOLD, 10));
            labels.put(i, l);
        }
        speedSlider.setLabelTable(labels);

        g.gridy = 1;
        p.add(speedSlider, g);

        // Live speed label
        JLabel speedLbl = new JLabel("Speed: 1X  (Normal)", SwingConstants.CENTER);
        speedLbl.setFont(new Font("Monospaced", Font.PLAIN, 12));
        speedLbl.setForeground(TEXT_COLOR);
        speedSlider.addChangeListener(e -> {
            int v = speedSlider.getValue();
            String tag = v <= 2 ? "Normal" : v <= 4 ? "Fast" : v <= 6 ? "Very Fast" : v <= 8 ? "Extreme" : "INSANE";
            speedLbl.setText("Speed: " + v + "X  (" + tag + ")");
            speedLbl.setForeground(v <= 3 ? new Color(100, 220, 140) :
                                   v <= 6 ? new Color(255, 200, 80)  :
                                            new Color(255, 80, 80));
        });
        g.gridy = 2;
        p.add(speedLbl, g);

        return p;
    }

    // =========================================================
    //  Helpers — layout / styling
    // =========================================================
    private float getSpeed() {
        return speedSlider != null ? (float) speedSlider.getValue() : 1.0f;
    }

    private JPanel darkPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(BG_COLOR);
        return p;
    }

    private GridBagConstraints defaultGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx  = 0;
        g.gridy  = 0;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(8, 30, 8, 30);
        return g;
    }

    private JButton createButton(String text, Color normal, Color hover) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(normal);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(300, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(normal); }
        });
        return btn;
    }
}
