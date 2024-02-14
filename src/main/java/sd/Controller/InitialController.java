package sd.Controller;

import sd.Model.Difficulty;

/**
 * Interface used to describe how to manage a {@link sd.View.InitialGUI}.
 */
public interface InitialController {

    /**
     * Create and display the {@link sd.View.InitialGUI} of the game.
     */
    void createInitialGUI();

    /**
     * Create the {@link sd.Akka.Actor.PlayerActor} and if he is the host also the
     * {@link sd.Akka.Actor.WaitingRoomActor}; it sends also a message to the player to consent the creation of
     * the {@link sd.View.WaitingGUI}.
     *
     * @param playerName a {@link String} with the name of the player.
     * @param hostName a {@link String} with the name of the host.
     * @param difficulty the {@link String} version of the {@link Difficulty}.
     */
    void createPlayerAndWaitingActors(String playerName, String hostName, String difficulty);
}
