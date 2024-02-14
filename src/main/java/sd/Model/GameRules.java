package sd.Model;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Class with static method that define the rules of the game.
 */
public class GameRules {

    private static final int LOWER_CARD = 2;
    private static final int HIGHER_CARD = 98;
    private static final int CARDS_FOR_ONE = 8;
    private static final int CARDS_FOR_TWO = 7;
    private static final int CARDS_FOR_MORE = 6;
    private static final int TRICK_VALUE = 10;
    private static final int NORMAL_AMOUNT = 2;
    private static final int HARD_AMOUNT = 3;

    /**
     * Method that define the cards present in the main deck.
     *
     * @return a {@link List} with the value of the cards in the main deck.
     */
    public static List<Integer> mainDeckCards() {
        return IntStream.rangeClosed(LOWER_CARD, HIGHER_CARD).boxed().toList();
    }

    /**
     * Method that define the number of cards that each player must have int his hand at the beginning of his turn
     * (if possible).
     *
     * @param numberOfPlayers the number of player in this match.
     * @param difficulty the {@link Difficulty} of the game.
     * @return the number of cards each player must have in hand.
     */
    public static int cardsInHand(final int numberOfPlayers, final Difficulty difficulty) {
        int normal = numberOfPlayers == 1 ? CARDS_FOR_ONE : numberOfPlayers == 2 ? CARDS_FOR_TWO : CARDS_FOR_MORE;
        return difficulty == Difficulty.IMPOSSIBLE ? normal - 1 : normal;
    }

    /**
     * Check if a card il playable in an ascending deck (the one which starts from 1).
     *
     * @param deckValue the value of the last card played in the deck.
     * @param cardSelected the card the player want to play.
     * @return true if the play is possible, false otherwise.
     */
    public static boolean isAscValid(final int deckValue, final int cardSelected) {
        return deckValue < cardSelected || deckValue == cardSelected + TRICK_VALUE;
    }

    /**
     * Check if a card il playable in a descending deck (the one which starts from 99).
     *
     * @param deckValue the value of the last card played in the deck.
     * @param cardSelected the card the player want to play.
     * @return true if the play is possible, false otherwise.
     */
    public static boolean isDescValid(final int deckValue, final int cardSelected) {
        return deckValue > cardSelected || deckValue == cardSelected - TRICK_VALUE;
    }

    /**
     * Calculate how many cards have to be played each turn based on the difficulty chosen.
     *
     * @param difficulty the difficulty of this match.
     * @return the minimum number of cards to play.
     */
    public static int cardsPerTurn(final Difficulty difficulty) {
        return switch (difficulty) {
            case NORMAL -> NORMAL_AMOUNT;
            case DIFFICULT, IMPOSSIBLE -> HARD_AMOUNT;
        };
    }

    /**
     * Method to obtain the value of the lower card a player can draw.
     *
     * @return the value of the lower card.
     */
    public static int getLowerCard() {
        return LOWER_CARD;
    }

    /**
     * Method to obtain the value of the higher card a player can draw.
     *
     * @return the value of the higher card.
     */
    public static int getHigherCard() {
        return HIGHER_CARD;
    }

    /**
     * Method to obtain the value (to add or subtract) of a card in order to play it even if it is in the opposite
     * direction to the deck order. This play is called "trick"
     *
     * @return the value to add or subtract to do the trick.
     */
    public static int getTrickValue() {
        return TRICK_VALUE;
    }

    /**
     * Method to obtain the number of cards to have in hand if there is only one player.
     *
     * @return the number of cards the player must have in hand.
     */
    public static int getCardsForOne() {
        return CARDS_FOR_ONE;
    }

    /**
     * Method to obtain the number of cards to have in hand if there are two players.
     *
     * @return the number of cards the player must have in hand.
     */
    public static int getCardsForTwo() {
        return CARDS_FOR_TWO;
    }

    /**
     * Method to obtain the number of cards to have in hand if there are more than two players.
     *
     * @return the number of cards the player must have in hand.
     */
    public static int getCardsForMore() {
        return CARDS_FOR_MORE;
    }

    /**
     * Method to obtain the number of cards to play each turn in the normal mode.
     *
     * @return the number of cards to draw.
     */
    public static int getNormalAmount() {
        return NORMAL_AMOUNT;
    }

    /**
     * Method to obtain the number of cards to play each turn in the hard mode.
     *
     * @return the number of cards to draw.
     */
    public static int getHardAmount() {
        return HARD_AMOUNT;
    }
}
