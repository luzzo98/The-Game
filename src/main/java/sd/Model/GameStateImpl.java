package sd.Model;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * An implementation of {@link GameState}; implements {@link Serializable} permit to be sent as a part of a message.
 */
public class GameStateImpl implements GameState, Serializable {

    private final List<Player> players = new LinkedList<>();
    private final List<Integer> mainDeck = new LinkedList<>();
    private final List<Integer> lastCards = new LinkedList<>();
    private final Difficulty difficulty;

    /**
     * Create an implementation of {@link GameState}.
     *
     * @param playerNames a {@link List} with the names of the players.
     * @param difficulty the {@link Difficulty} of the game.
     */
    public GameStateImpl(final List<String> playerNames, final Difficulty difficulty) {
        this.difficulty = difficulty;
        playerNames.forEach(name -> players.add(new PlayerImpl(name)));
        int lowerDeckValue = GameRules.getLowerCard() - 1;
        int higherDeckValue = GameRules.getHigherCard() + 1;
        lastCards.addAll(List.of(lowerDeckValue, lowerDeckValue, higherDeckValue, higherDeckValue));
        // remove the next comment if you want to try the win easily and comment the next two lines
//        mainDeck.addAll(IntStream.rangeClosed(2, 20).boxed().toList());
        mainDeck.addAll(GameRules.mainDeckCards());
        Collections.shuffle(mainDeck);
    }

    @Override
    public void initialHand() {
        players.forEach(player -> drawCards(player, GameRules.cardsInHand(players.size(), difficulty)));
    }

    @Override
    public List<Integer> draw(final Player player) {
        if (players.contains(player)) {
            drawCards(player, GameRules.cardsInHand(players.size(), difficulty) - player.getHand().size());
            return player.getHand();
        } else {
            throw new RuntimeException(player.getName() + ": player not found when trying to draw");
        }
    }

    @Override
    public Optional<Player> getPlayer(final String playerName) {
        return players.stream().filter(player -> player.getName().equals(playerName)).findFirst();
    }

    @Override
    public LinkedHashMap<String, Integer> countPlayersCards() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        players.forEach(player -> map.put(player.getName(), player.getHand().size()));
        return map;
    }

    @Override
    public void playedCard(final Player player, final int card, final int deckNumber) {
        player.playedCard(card);
        lastCards.set(deckNumber, card);
    }

    @Override
    public int countMainDeckCards() {
        return mainDeck.size();
    }

    @Override
    public int cardNotPlayed() {
        return mainDeck.size() + players.stream().mapToInt(player -> player.getHand().size()).sum();
    }

    @Override
    public boolean validMoveExist(final Player player) {
        for (int card : player.getHand()) {
            for (int i = 0; i < 4; i++) {
                if (i < 2) {
                    if (GameRules.isAscValid(lastCards.get(i), card)) {
                        return true;
                    }
                } else {
                    if (GameRules.isDescValid(lastCards.get(i), card)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isWin() {
        return players.stream().allMatch(player -> player.getHand().isEmpty()) && mainDeck.isEmpty();
    }

    /**
     * Method used to draw the cards from the main deck after a player ends his turn, and set his new hand.
     *
     * @param player the {@link Player} who has to draw.
     * @param cardsToDraw the number of cards played by the player this turn, the same number of cards he has to draw.
     */
    private void drawCards(final Player player, final int cardsToDraw) {
        List<Integer> drawnCards = mainDeck.subList(0, Math.min(cardsToDraw, mainDeck.size()));
        List<Integer> newHand = new LinkedList<>(drawnCards);
        newHand.addAll(player.getHand());
        Collections.sort(newHand);
        player.setHand(newHand);
        mainDeck.removeAll(newHand);
    }
}
