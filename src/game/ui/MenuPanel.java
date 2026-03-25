package game.ui;

import javax.swing.*;
import java.awt.*;

/**
 * The main menu / title screen shown before a game starts.
 */
public class MenuPanel extends JPanel {

    private static final Color BG_COLOR    = new Color(10, 8, 28);
    private static final Color TITLE_COLOR = new Color(180, 140, 220);
    private static final Color TEXT_COLOR  = new Color(180, 170, 200);
    private static final Color BTN_COLOR   = new Color(90, 60, 130);
    private static final Color BTN_HOVER   = new Color(120, 80, 170);

    public MenuPanel(GameWindow window) {
        setBackground(BG_COLOR);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(720, 720));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(12, 0, 12, 0);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.CENTER;
        gbc.gridx   = 0;

        // Title
        JLabel title = new JLabel("ECHOES OF THE SENTINEL", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 28));
        title.setForeground(TITLE_COLOR);
        gbc.gridy = 0;
        add(title, gbc);

        // Subtitle
        JLabel sub = new JLabel("A Time-Stasis Stealth Challenge", SwingConstants.CENTER);
        sub.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sub.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        add(sub, gbc);

        // Spacer
        gbc.gridy = 2;
        add(Box.createVerticalStrut(20), gbc);

        // Instructions
        JLabel inst = new JLabel(
            "<html><div style='text-align:center;width:400px;'>" +
            "Collect <b>3 of 4</b> bio-equipment pieces.<br>" +
            "Escape to the <b>top exit</b> within <b>10 minutes</b>.<br>" +
            "Solve riddles to collect items — but the clock never stops.<br>" +
            "Fail a riddle, and the Sentinel hunts you for 3 seconds.<br><br>" +
            "<b>WASD / Arrow Keys</b> — Move &nbsp;|&nbsp; <b>E / Space</b> — Interact" +
            "</div></html>",
            SwingConstants.CENTER
        );
        inst.setFont(new Font("SansSerif", Font.PLAIN, 13));
        inst.setForeground(TEXT_COLOR);
        gbc.gridy = 3;
        add(inst, gbc);

        // Spacer
        gbc.gridy = 4;
        add(Box.createVerticalStrut(20), gbc);

        // Play button
        JButton playBtn = createButton("▶  START GAME");
        playBtn.addActionListener(e -> window.startGame());
        gbc.gridy = 5;
        add(playBtn, gbc);

        // Quit button
        JButton quitBtn = createButton("✕  QUIT");
        quitBtn.addActionListener(e -> System.exit(0));
        gbc.gridy = 6;
        add(quitBtn, gbc);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BTN_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(260, 48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e)  { btn.setBackground(BTN_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)   { btn.setBackground(BTN_COLOR); }
        });
        return btn;
    }
}
