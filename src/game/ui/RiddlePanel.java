package game.ui;

import game.core.GameStateManager;
import game.riddle.Riddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * RiddlePanel is shown as a proper JDialog during Temporal Stasis.
 * Using JDialog instead of an overlay panel fixes all focus and click issues
 * because the dialog gets its own native window with proper event handling.
 */
public class RiddlePanel {

    private static final Color BG_COLOR   = new Color(15, 10, 35);
    private static final Color BORDER_COL = new Color(140, 90, 200);
    private static final Color TEXT_COL   = new Color(220, 200, 255);
    private static final Color HINT_COL   = new Color(160, 140, 200);
    private static final Color INPUT_BG   = new Color(30, 20, 55);
    private static final Color BTN_SUBMIT = new Color(80, 50, 140);
    private static final Color BTN_OTHER  = new Color(60, 40, 80);

    private final GameStateManager gsm;
    private final JFrame           parentFrame;

    private JDialog    dialog;
    private JLabel     questionLabel;
    private JLabel     hintLabel;
    private JTextField inputField;
    private JLabel     feedbackLabel;

    private Riddle  currentRiddle;
    private boolean hintRevealed = false;

    public RiddlePanel(GameStateManager gsm, JFrame parentFrame) {
        this.gsm         = gsm;
        this.parentFrame = parentFrame;
    }

    private void buildDialog() {
        dialog = new JDialog(parentFrame, "Temporal Stasis", false);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setUndecorated(true);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_COLOR);
        root.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COL, 2),
            BorderFactory.createEmptyBorder(10, 16, 14, 16)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets  = new Insets(5, 8, 5, 8);
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.gridx   = 0;
        c.weightx = 1.0;

        JLabel title = new JLabel("-- TEMPORAL STASIS --", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 15));
        title.setForeground(new Color(180, 140, 255));
        c.gridy = 0; root.add(title, c);

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        questionLabel.setForeground(TEXT_COL);
        c.gridy = 1; root.add(questionLabel, c);

        inputField = new JTextField(24);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 15));
        inputField.setBackground(INPUT_BG);
        inputField.setForeground(TEXT_COL);
        inputField.setCaretColor(TEXT_COL);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COL, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        inputField.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) submit();
            }
        });
        c.gridy = 2; root.add(inputField, c);

        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Monospaced", Font.BOLD, 11));
        feedbackLabel.setForeground(new Color(255, 100, 100));
        c.gridy = 3; root.add(feedbackLabel, c);

        hintLabel = new JLabel(" ", SwingConstants.CENTER);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        hintLabel.setForeground(HINT_COL);
        c.gridy = 4; root.add(hintLabel, c);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        JButton submitBtn = makeButton("SUBMIT", BTN_SUBMIT);
        JButton hintBtn   = makeButton("HINT",   BTN_OTHER);
        JButton skipBtn   = makeButton("SKIP",   BTN_OTHER);
        submitBtn.addActionListener(e -> submit());
        hintBtn.addActionListener(e -> revealHint());
        skipBtn.addActionListener(e -> { hide(); gsm.skipRiddle(); });
        btnRow.add(submitBtn); btnRow.add(hintBtn); btnRow.add(skipBtn);
        c.gridy = 5; root.add(btnRow, c);

        dialog.setContentPane(root);
        dialog.pack();
    }

    public void show(Riddle riddle) {
        if (dialog == null) buildDialog();
        this.currentRiddle = riddle;
        this.hintRevealed  = false;
        String q = "<html><div style='text-align:center;width:340px'>" +
            riddle.getQuestion().replace("\n", "<br>") + "</div></html>";
        questionLabel.setText(q);
        hintLabel.setText(" ");
        feedbackLabel.setText(" ");
        inputField.setText("");
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
    }

    public void hide() {
        if (dialog != null) dialog.setVisible(false);
    }

    public boolean isVisible() {
        return dialog != null && dialog.isVisible();
    }

    private void submit() {
        if (currentRiddle == null) return;
        String answer = inputField.getText().trim();
        if (answer.isEmpty()) {
            feedbackLabel.setText("Please type an answer first.");
            inputField.requestFocusInWindow();
            return;
        }
        hide();
        gsm.submitRiddleAnswer(answer);
    }

    private void revealHint() {
        if (currentRiddle != null && !hintRevealed) {
            hintLabel.setText("Hint: " + currentRiddle.getHint());
            hintRevealed = true;
            dialog.pack();
            dialog.setLocationRelativeTo(parentFrame);
        }
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(100, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
