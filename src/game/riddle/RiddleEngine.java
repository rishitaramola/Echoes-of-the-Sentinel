package game.riddle;

import java.util.ArrayList;
import java.util.List;

/**
 * RiddleEngine stores all 4 riddles (one per bio-equipment item).
 * Riddles cover logic, math, and pattern-based thinking as per the proposal.
 *
 * To add more riddles: just add a new Riddle(...) to the list.
 * To make them external (stretch goal): load from a JSON/txt file via java.io.
 */
public class RiddleEngine {

    private final List<Riddle> riddles = new ArrayList<>();

    public RiddleEngine() {
        loadRiddles();
    }

    private void loadRiddles() {
        // --- Riddle 0: Oxygen Mask ---
        riddles.add(new Riddle(
            "I have cities, but no houses live there.\n" +
            "I have mountains, but no trees grow there.\n" +
            "I have water, but no fish swim there.\n" +
            "I have roads, but no cars drive there.\n" +
            "What am I?",
            "Think about something you use to navigate.",
            "map", "a map"
        ));

        // --- Riddle 1: Neural Implant ---
        riddles.add(new Riddle(
            "A logic gate problem:\n\n" +
            "  A = TRUE,  B = FALSE\n" +
            "  C = A AND (NOT B)\n" +
            "  D = C OR  B\n\n" +
            "What is the value of D?",
            "Evaluate NOT B first, then work left to right.",
            "true", "TRUE", "1"
        ));

        // --- Riddle 2: Bio-Scanner ---
        riddles.add(new Riddle(
            "What is the next number in this sequence?\n\n" +
            "  1, 1, 2, 3, 5, 8, 13, ___",
            "Each number is the sum of the two before it.",
            "21"
        ));

        // --- Riddle 3: Stasis Pod ---
        riddles.add(new Riddle(
            "I speak without a mouth and hear without ears.\n" +
            "I have no body, but I come alive with wind.\n" +
            "What am I?",
            "Think about what bounces off cave walls.",
            "echo", "an echo"
        ));
    }

    /**
     * Returns the riddle associated with a given item index.
     * Index wraps safely so it's always valid.
     */
    public Riddle getRiddleFor(int index) {
        return riddles.get(index % riddles.size());
    }

    public int getRiddleCount() { return riddles.size(); }
}
