package game.riddle;

import game.core.GameMode;
import game.core.KidsSubject;
import game.core.DifficultyLevel;
import java.util.Random;

/**
 * RiddleEngine - Procedural question generator.
 *
 * Kids Section:
 *   MATHS   - Simple addition, subtraction, multiplication (numbers < 20)
 *   ENGLISH - Missing letter puzzles, unscramble words, fill in the blank
 *   RIDDLES - Fun classic kid riddles, compound words
 *
 * Fun Play:
 *   EASY    - Standard arithmetic, simple logic riddles
 *   MEDIUM  - Algebraic expressions, Boolean gates, intermediate riddles
 *   HARD    - Complex simplification, advanced boolean, trick riddles
 *
 * All questions are procedurally generated or drawn from large pools so they
 * vary every single game.
 */
public class RiddleEngine {

    private final Random rng = new Random();

    public RiddleEngine() {}

    public Riddle generateRiddle(GameMode mode, KidsSubject subject, DifficultyLevel difficulty) {
        if (mode == GameMode.KIDS) {
            return switch (subject) {
                case MATHS   -> generateKidsMaths();
                case ENGLISH -> generateKidsEnglish();
                case RIDDLES -> generateKidsRiddles();
            };
        } else {
            return switch (difficulty) {
                case EASY   -> generateFunEasy();
                case MEDIUM -> generateFunMedium();
                case HARD   -> generateFunHard();
            };
        }
    }

    // =========================================================
    // KIDS - MATHS
    // =========================================================
    private Riddle generateKidsMaths() {
        int type = rng.nextInt(4);
        if (type == 0) {
            // Addition
            int a = rng.nextInt(15) + 1;
            int b = rng.nextInt(15) + 1;
            return new Riddle(
                "Maths Time!\nWhat is " + a + " + " + b + " ?",
                "Count up from the bigger number!",
                String.valueOf(a + b)
            );
        } else if (type == 1) {
            // Subtraction (always positive result)
            int b = rng.nextInt(10) + 1;
            int a = b + rng.nextInt(10) + 1;
            return new Riddle(
                "Maths Time!\nWhat is " + a + " - " + b + " ?",
                "Count down from " + a + " by " + b + " steps.",
                String.valueOf(a - b)
            );
        } else if (type == 2) {
            // Multiplication (small numbers)
            int a = rng.nextInt(5) + 2;
            int b = rng.nextInt(5) + 2;
            return new Riddle(
                "Maths Time!\nWhat is " + a + " x " + b + " ?",
                "Add " + a + " to itself " + b + " times!",
                String.valueOf(a * b)
            );
        } else {
            // Fill-in story problem
            String[] names = {"Mia", "Leo", "Zara", "Sam", "Ava", "Tom"};
            String[] things = {"apples", "balloons", "cookies", "stickers", "candies"};
            String name = names[rng.nextInt(names.length)];
            String thing = things[rng.nextInt(things.length)];
            int a = rng.nextInt(8) + 2;
            int b = rng.nextInt(5) + 1;
            return new Riddle(
                "Story Problem!\n" + name + " has " + a + " " + thing + ".\n" +
                name + " gives away " + b + ".\nHow many does " + name + " have left?",
                "It's a subtraction problem!",
                String.valueOf(a - b)
            );
        }
    }

    // =========================================================
    // KIDS - ENGLISH
    // =========================================================
    private Riddle generateKidsEnglish() {
        int type = rng.nextInt(3);
        if (type == 0) {
            // Missing letter puzzle
            String[][] wordsAndHints = {
                {"BANANA",  "It is a yellow fruit", "B_NANA", "2"},
                {"APPLE",   "It is a red or green fruit", "A_PLE", "2"},
                {"ORANGE",  "It is also a color", "OR_NGE", "3"},
                {"ELEPHANT","It is the biggest land animal", "EL_PHANT", "4"},
                {"BUTTERFLY","It starts as a caterpillar", "BUTT_RFLY", "5"},
                {"GARDEN",  "Where flowers grow", "GAR_EN", "4"},
                {"MONKEY",  "It loves bananas", "MON_EY", "4"},
                {"DOLPHIN", "A smart sea animal", "DOLP_IN", "5"},
                {"RAINBOW", "Seen after rain", "RAIN_OW", "4"},
                {"SCHOOL",  "Where you learn", "SC_OOL", "3"},
                {"RABBIT",  "A fast hopping animal", "RAB_IT", "4"},
                {"PENCIL",  "You write with it", "PEN_IL", "4"},
                {"CASTLE",  "Where kings and queens live", "CAS_LE", "4"},
                {"FLOWER",  "It blooms in spring", "FL_WER", "3"},
                {"JUNGLE",  "A wild forest", "JUN_LE", "4"},
            };
            int idx = rng.nextInt(wordsAndHints.length);
            String[] w = wordsAndHints[idx];
            return new Riddle(
                "English Puzzle!\nHint: " + w[1] + "\nFill in the missing letter:\n" + w[2],
                "The full word is: " + w[0].charAt(Integer.parseInt(w[3]) - 1),
                w[3]
            );
        } else if (type == 1) {
            // Unscramble simple words
            String[][] scrambles = {
                {"cat",    "TAC",    "A small furry pet that meows"},
                {"dog",    "GOD",    "A loyal pet that barks"},
                {"sun",    "UNS",    "It shines in the sky"},
                {"hat",    "TAH",    "You wear it on your head"},
                {"ball",   "LLAB",   "You play with it"},
                {"fish",   "SHIF",   "It swims in water"},
                {"bird",   "DRIB",   "It can fly"},
                {"star",   "RATS",   "It twinkles at night"},
                {"cake",   "KAEC",   "You eat it at birthdays"},
                {"tree",   "EETR",   "It has leaves and a trunk"},
                {"milk",   "KLIM",   "A white drink"},
                {"frog",   "GORF",   "It jumps and says ribbit"},
                {"book",   "KOOB",   "You read it"},
                {"rain",   "NIAR",   "It falls from clouds"},
                {"lamp",   "PMAL",   "It gives you light"},
            };
            int idx = rng.nextInt(scrambles.length);
            String[] s = scrambles[idx];
            return new Riddle(
                "Unscramble!\nHint: " + s[2] + "\nUnscramble the letters: " + s[1],
                "The word is: " + s[0],
                s[0]
            );
        } else {
            // First letter / starts with
            String[][] firstLetters = {
                {"Dog", "D", "A pet that barks"},
                {"Elephant", "E", "The biggest land animal"},
                {"Kite", "K", "You fly it in the sky"},
                {"Moon", "M", "It glows at night"},
                {"Nest", "N", "Where birds live"},
                {"Owl", "O", "A bird that is awake at night"},
                {"Parrot", "P", "A bird that can talk"},
                {"Queen", "Q", "She rules a kingdom"},
                {"Rose", "R", "A beautiful flower"},
                {"Tiger", "T", "A striped wild animal"},
                {"Umbrella", "U", "You open it when it rains"},
                {"Volcano", "V", "A mountain that spits fire"},
                {"Waterfall", "W", "Water falling from a height"},
                {"Fox", "F", "A sly orange animal"},
                {"Igloo", "I", "A house made of ice"},
            };
            int idx = rng.nextInt(firstLetters.length);
            String[] fl = firstLetters[idx];
            return new Riddle(
                "English Time!\n" + fl[2] + "\nWhat letter does this word start with?\n(Type just the single letter)",
                "Think about the word: " + fl[0],
                fl[1], fl[1].toLowerCase()
            );
        }
    }

    // =========================================================
    // KIDS - RIDDLES
    // =========================================================
    private Riddle generateKidsRiddles() {
        int type = rng.nextInt(2);
        if (type == 0) {
            // Compound words
            String[][] compounds = {
                {"butter", "fly",    "butterfly"},
                {"sun",    "flower", "sunflower"},
                {"rain",   "bow",    "rainbow"},
                {"star",   "fish",   "starfish"},
                {"jelly",  "fish",   "jellyfish"},
                {"fire",   "fly",    "firefly"},
                {"sea",    "horse",  "seahorse"},
                {"eye",    "brow",   "eyebrow"},
                {"foot",   "ball",   "football"},
                {"bed",    "room",   "bedroom"},
                {"cup",    "cake",   "cupcake"},
                {"sand",   "castle", "sandcastle"},
                {"fire",   "truck",  "firetruck"},
                {"water",  "fall",   "waterfall"},
                {"snow",   "flake",  "snowflake"},
                {"pine",   "apple",  "pineapple"},
                {"dragon", "fly",    "dragonfly"},
                {"book",   "worm",   "bookworm"},
                {"air",    "plane",  "airplane"},
                {"sun",    "shine",  "sunshine"},
            };
            int idx = rng.nextInt(compounds.length);
            String[] c = compounds[idx];
            return new Riddle(
                "Word Magic!\nCombine these two words:\n\"" + c[0] + "\" + \"" + c[1] + "\" = ?",
                "Just put the words together!",
                c[2]
            );
        } else {
            // Classic fun kid riddles
            String[][] riddles = {
                {"I have hands but cannot clap.\nWhat am I?", "clock", "a clock"},
                {"I have teeth but cannot bite.\nWhat am I?", "comb", "a comb"},
                {"I shine all day but am not the sun.\nI am in your pocket.\nWhat am I?", "phone", "a phone", "mobile"},
                {"I have keys but no locks.\nI have space but no room.\nWhat am I?", "keyboard", "a keyboard"},
                {"I get wetter as I dry.\nWhat am I?", "towel", "a towel"},
                {"I can fly without wings.\nI can be caught but not thrown.\nWhat am I?", "cold", "a cold"},
                {"The more you take, the more you leave behind.\nWhat am I?", "footsteps", "steps", "footprints"},
                {"I have a face but no eyes.\nI have hands but no arms.\nWhat am I?", "clock", "a clock"},
                {"I am always in front of you but cannot be seen.\nWhat am I?", "future", "the future"},
                {"I speak without a mouth.\nI repeat without ears.\nWhat am I?", "echo", "an echo"},
                {"I have a tail and a head but no body.\nWhat am I?", "coin", "a coin"},
                {"I go up but never come down.\nWhat am I?", "age", "your age"},
                {"What has four legs in the morning, two at noon, and three at night?", "human", "man", "a human"},
                {"I am not alive yet I can grow.\nI do not have lungs but need air.\nWhat am I?", "fire", "a fire"},
                {"What can run but never walks,\nhas a mouth but never talks?", "river", "a river"},
            };
            int idx = rng.nextInt(riddles.length);
            String[] r = riddles[idx];
            String question = r[0];
            String[] answers = new String[r.length - 1];
            System.arraycopy(r, 1, answers, 0, r.length - 1);
            return new Riddle(question, "Think carefully!", answers);
        }
    }

    // =========================================================
    // FUN PLAY - EASY
    // =========================================================
    private Riddle generateFunEasy() {
        int type = rng.nextInt(3);
        if (type == 0) {
            // Simple arithmetic with brackets
            int a = rng.nextInt(10) + 2;
            int b = rng.nextInt(10) + 2;
            int c = rng.nextInt(10) + 1;
            int ans = a + b - c;
            return new Riddle(
                "Solve:\n(" + a + " + " + b + ") - " + c + " = ?",
                "Add first, then subtract.",
                String.valueOf(ans)
            );
        } else if (type == 1) {
            // Simple word riddles
            String[][] riddles = {
                {"I have cities, but no houses.\nI have mountains, but no trees.\nWhat am I?", "map", "a map"},
                {"I am light as a feather,\nbut even the strongest person can't hold me for long.\nWhat am I?", "breath", "your breath"},
                {"What is always coming but never arrives?", "tomorrow", "the future"},
                {"What has a head, a tail, but no body?", "coin", "a coin"},
                {"What goes up when rain comes down?", "umbrella", "an umbrella"},
                {"What can you catch but not throw?", "cold", "a cold"},
                {"I have hands but cannot feel.\nI tell time but cannot speak.\nWhat am I?", "clock", "a clock"},
                {"What is full of holes but still holds water?", "sponge", "a sponge"},
                {"What gets bigger the more you take away?", "hole", "a hole"},
                {"I have no voice yet speak to many.\nWhat am I?", "book", "a book"},
                {"What has an eye but cannot see?", "needle", "a needle"},
                {"What comes once in a minute,\ntwice in a moment,\nbut never in a thousand years?", "m", "the letter m"},
                {"The more you take, the more you leave behind.\nWhat am I?", "footsteps", "steps"},
                {"I can fill a room but take up no space.\nWhat am I?", "light", "light"},
                {"What has legs but cannot walk?", "table", "a table", "chair", "a chair"},
            };
            int idx = rng.nextInt(riddles.length);
            String[] r = riddles[idx];
            String[] answers = new String[r.length - 1];
            System.arraycopy(r, 1, answers, 0, r.length - 1);
            return new Riddle(r[0], "Think outside the box!", answers);
        } else {
            // Basic Boolean (single gate)
            boolean a = rng.nextBoolean();
            boolean b = rng.nextBoolean();
            boolean isAnd = rng.nextBoolean();
            boolean ans = isAnd ? (a && b) : (a || b);
            String op = isAnd ? "AND" : "OR";
            return new Riddle(
                "Logic Gate:\nA = " + String.valueOf(a).toUpperCase() +
                "\nB = " + String.valueOf(b).toUpperCase() +
                "\nResult of A " + op + " B ?",
                "For " + op + ": " + (isAnd ? "both must be TRUE." : "at least one must be TRUE."),
                String.valueOf(ans).toLowerCase(), String.valueOf(ans).toUpperCase()
            );
        }
    }

    // =========================================================
    // FUN PLAY - MEDIUM
    // =========================================================
    private Riddle generateFunMedium() {
        int type = rng.nextInt(3);
        if (type == 0) {
            // Multi-step arithmetic with operator precedence
            int a = rng.nextInt(8) + 2;
            int b = rng.nextInt(6) + 2;
            int c = rng.nextInt(12) + 1;
            int ans = (a * b) - c;
            return new Riddle(
                "Solve (follow order of operations):\n(" + a + " x " + b + ") - " + c + " = ?",
                "Multiply first, then subtract!",
                String.valueOf(ans)
            );
        } else if (type == 1) {
            // Boolean with NOT operator
            boolean a = rng.nextBoolean();
            boolean b = rng.nextBoolean();
            int gateType = rng.nextInt(3);
            boolean ans;
            String question;
            String hint;
            if (gateType == 0) {
                ans = !(a && b);
                question = "Logic Gates:\nA = " + String.valueOf(a).toUpperCase() +
                       "\nB = " + String.valueOf(b).toUpperCase() +
                       "\nResult of NOT (A AND B) ?";
                hint = "First evaluate A AND B, then flip the result.";
            } else if (gateType == 1) {
                ans = !(a || b);
                question = "Logic Gates:\nA = " + String.valueOf(a).toUpperCase() +
                           "\nB = " + String.valueOf(b).toUpperCase() +
                           "\nResult of NOT (A OR B) ?";
                hint = "First evaluate A OR B, then flip the result.";
            } else {
                ans = (!a) && b;
                question = "Logic Gates:\nA = " + String.valueOf(a).toUpperCase() +
                           "\nB = " + String.valueOf(b).toUpperCase() +
                           "\nResult of (NOT A) AND B ?";
                hint = "Flip A first, then check if both are TRUE.";
            }
            return new Riddle(question, hint, String.valueOf(ans).toLowerCase(), String.valueOf(ans).toUpperCase());
        } else {
            // Trick / lateral-thinking riddles
            String[][] riddles = {
                {"I speak without mouth and hear without ears.\nI have no body, but come alive with wind.\nWhat am I?", "echo", "an echo"},
                {"The more of this there is, the less you see.\nWhat is it?", "darkness", "dark", "the dark"},
                {"I shave every day, but my beard stays the same.\nWhat am I?", "barber", "a barber"},
                {"I have cities with no houses.\nMountains with no trees.\nWater with no fish.\nWhat am I?", "map", "a map"},
                {"What word is spelled incorrectly in every dictionary?", "incorrectly", "wrong"},
                {"What can you break, even if you never pick it up or touch it?", "promise", "a promise"},
                {"What goes through a door but never goes in or comes out?", "keyhole", "a keyhole"},
                {"I am not alive but still grow.\nI have no lungs but need air.\nI have no mouth but water kills me.\nWhat am I?", "fire", "a fire"},
                {"How many months have 28 days?", "12", "all", "all of them", "every month"},
                {"A man walks into a room and shoots himself.\nHe walks out alive. How?", "mirror", "photo", "his reflection"},
                {"If you throw me from the window,\nI will leave a grieving wife.\nBring me back, but put me in the door,\nand I will never leave.", "n", "the letter n"},
                {"What is always in front of you but can't be seen?", "future", "the future"},
                {"Forward I am heavy; backward I am not. What am I?", "ton", "a ton"},
                {"I have no beginning, end, or middle. What am I?", "circle", "a circle", "doughnut"},
                {"What do you bury when it's alive and dig up when it's dead?", "plant", "a plant"},
            };
            int idx = rng.nextInt(riddles.length);
            String[] r = riddles[idx];
            String[] answers = new String[r.length - 1];
            System.arraycopy(r, 1, answers, 0, r.length - 1);
            return new Riddle(r[0], "Think logically!", answers);
        }
    }

    // =========================================================
    // FUN PLAY - HARD
    // =========================================================
    private Riddle generateFunHard() {
        int type = rng.nextInt(3);
        if (type == 0) {
            // Complex multi-step expressions
            int a = rng.nextInt(6) + 3;
            int b = rng.nextInt(4) + 2;
            int c = rng.nextInt(5) + 1;
            int d = rng.nextInt(7) + 1;
            int ans = (a * b) + (c * d);
            return new Riddle(
                "Simplify:\n(" + a + " x " + b + ") + (" + c + " x " + d + ") = ?",
                "Solve each bracket separately, then add.",
                String.valueOf(ans)
            );
        } else if (type == 1) {
            // Full XOR / advanced boolean
            boolean a = rng.nextBoolean();
            boolean b = rng.nextBoolean();
            boolean c = rng.nextBoolean();
            int gateType = rng.nextInt(3);
            boolean ans;
            String question;
            String hint;
            if (gateType == 0) {
                // XOR (A OR B) AND (NOT (A AND B))
                boolean xor = (a || b) && !(a && b);
                ans = xor;
                question = "XOR Gate:\nA = " + String.valueOf(a).toUpperCase() +
                           "\nB = " + String.valueOf(b).toUpperCase() +
                           "\nResult of A XOR B ?";
                hint = "XOR is TRUE only when exactly one input is TRUE.";
            } else if (gateType == 1) {
                ans = (a && b) || (!a && c);
                question = "Complex Logic:\nA=" + String.valueOf(a).toUpperCase() +
                           ", B=" + String.valueOf(b).toUpperCase() +
                           ", C=" + String.valueOf(c).toUpperCase() +
                           "\n(A AND B) OR (NOT A AND C) = ?";
                hint = "Evaluate each pair in brackets first.";
            } else {
                ans = !(a || b) && c;
                question = "Multi-Gate:\nA=" + String.valueOf(a).toUpperCase() +
                           ", B=" + String.valueOf(b).toUpperCase() +
                           ", C=" + String.valueOf(c).toUpperCase() +
                           "\nNOT(A OR B) AND C = ?";
                hint = "Inner brackets first, then NOT, then AND.";
            }
            return new Riddle(question, hint, String.valueOf(ans).toLowerCase(), String.valueOf(ans).toUpperCase());
        } else {
            // Deep trick / philosophical riddles
            String[][] riddles = {
                {"The person who makes it, sells it.\nThe person who buys it, never uses it.\nThe person who uses it, doesn't know they're using it.\nWhat is it?", "coffin", "a coffin"},
                {"I am the beginning of the end,\nand the end of time and space.\nI am essential to creation,\nand I surround every place.\nWhat am I?", "e", "the letter e"},
                {"What can run but never walks,\nhas a mouth but never talks,\nhas a head but never weeps,\nhas a bed but never sleeps?", "river", "a river"},
                {"A woman shoots her husband.\nThen she holds him underwater for 5 minutes.\nA little later, they go out to dinner together.\nHow?", "photo", "photograph", "she took his photo"},
                {"You see a house. It has 4 walls, each facing south.\nA bear walks by. What color is the bear?", "white", "white bear", "polar", "polar bear"},
                {"Three doctors say that John is their brother.\nJohn says he has no brothers.\nWho is lying?", "nobody", "none", "no one", "the doctors are sisters"},
                {"You can always find me in the past.\nI can be created in the present,\nbut the future can never taint me.\nWhat am I?", "history", "memory", "the past"},
                {"There are two sisters: one gives birth to the other,\nand she in turn gives birth to the first.\nWho are the two sisters?", "day and night", "night and day"},
                {"What word in the English language does the\nfollowing:\nThe first two letters signify a male,\nthe first three letters signify a female,\nthe first four letters signify a great one,\nwhile the entire word signifies a great woman?", "heroine", "a heroine"},
                {"I am an odd number.\nTake away one letter and I become even.\nWhat number am I?", "seven", "7"},
                {"A rooster lays an egg on the barn roof.\nWhich way does it roll?", "roosters don't lay eggs", "it doesn't", "no way"},
                {"What disappears the moment you say its name?", "silence", "the silence"},
                {"If there are 3 apples and you take away 2,\nhow many apples do you have?", "2", "two"},
                {"What do you get if you add 2 to 200 four times?", "808", "202+202+202+202"},
                {"How far can a dog run into the woods?", "halfway", "half", "half way"},
            };
            int idx = rng.nextInt(riddles.length);
            String[] r = riddles[idx];
            String[] answers = new String[r.length - 1];
            System.arraycopy(r, 1, answers, 0, r.length - 1);
            return new Riddle(r[0], "Think very carefully, these are tricky!", answers);
        }
    }
}
