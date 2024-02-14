package sd.Model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple implementation of {@link Player}; implements {@link Serializable} permit to be sent as a part of a message.
 */
public class PlayerImpl implements Player, Serializable {

    private final String name;
    private List<Integer> hand = new LinkedList<>();

    /**
     * Create a {@link Player}.
     *
     * @param name the name of the player.
     */
    public PlayerImpl(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setHand(final List<Integer> cards) {
        this.hand = new LinkedList<>(cards);
    }

    @Override
    public List<Integer> getHand() {
        return this.hand;
    }

    @Override
    public void playedCard(final Integer card) {
        hand.remove(card);
    }
}
