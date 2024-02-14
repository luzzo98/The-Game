package sd.Model;

/**
 * Enumeration with the different difficulty levels of the game.
 */
public enum Difficulty {

    NORMAL,
    DIFFICULT,
    IMPOSSIBLE;

    private static final String NORMAL_ITA = "Normale";
    private static final String DIFFICULT_ITA = "Difficile";
    private static final String IMPOSSIBLE_ITA = "Impossibile";

    @Override
    public String toString() {
        return switch (this) {
            case NORMAL -> NORMAL_ITA;
            case DIFFICULT -> DIFFICULT_ITA;
            case IMPOSSIBLE -> IMPOSSIBLE_ITA;
        };
    }

    /**
     * Find the element of the enum from a {@link String}.
     *
     * @param difficulty the level of difficulty in {@link String} type.
     * @return the {@link Difficulty} version of the {@link String}.
     */
    public static Difficulty fromString(final String difficulty) {
        return switch (difficulty) {
            case NORMAL_ITA -> NORMAL;
            case DIFFICULT_ITA -> DIFFICULT;
            case IMPOSSIBLE_ITA -> IMPOSSIBLE;
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }
}
