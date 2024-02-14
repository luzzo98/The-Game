package sd.View;

import java.util.List;

/**
 * Defines the executable operations in the GUI where the user can wait other players.
 */
public interface WaitingGUI {

    /**
     * Create and display the GUI where the user can wait other player before start the game.
     */
    void create();

    /**
     * Refresh the list of players' names that will play the game.
     *
     * @param playerNames the names of the players.
     */
    void updatePlayers(List<String> playerNames);

    /**
     * Dispose the GUI.
     */
    void dispose();
}
