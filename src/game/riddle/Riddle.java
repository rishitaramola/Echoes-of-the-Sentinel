package game.riddle;

/**
 * A single riddle — holds the question, hint, and accepted answers.
 * Case-insensitive matching; multiple accepted answers are supported.
 */
public class Riddle {

    private final String   question;
    private final String   hint;
    private final String[] acceptedAnswers;

    public Riddle(String question, String hint, String... acceptedAnswers) {
        this.question        = question;
        this.hint            = hint;
        this.acceptedAnswers = acceptedAnswers;
    }

    /**
     * Returns true if the player's answer matches any accepted answer.
     * Trims whitespace and compares case-insensitively.
     */
    public boolean checkAnswer(String input) {
        if (input == null) return false;
        String trimmed = input.trim().toLowerCase();
        for (String a : acceptedAnswers) {
            if (trimmed.equals(a.toLowerCase())) return true;
        }
        return false;
    }

    public String getQuestion()       { return question; }
    public String getHint()           { return hint; }
}
