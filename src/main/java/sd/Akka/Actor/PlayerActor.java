package sd.Akka.Actor;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import sd.Akka.Messages.AddPlayerMsg;
import sd.Akka.Messages.CreateGameGUIMsg;
import sd.Akka.Messages.CreateWaitingGUIMsg;
import sd.Akka.Messages.GameOverMsg;
import sd.Akka.Messages.PlayedCardMsg;
import sd.Akka.Messages.RematchMsg;
import sd.Akka.Messages.StartTurnMsg;
import sd.Akka.Messages.UpdateWaitingRoomMsg;
import sd.Controller.GameController;
import sd.Controller.GameControllerImpl;
import sd.Controller.WaitingController;
import sd.Controller.WaitingControllerImpl;
import sd.Model.Difficulty;
import sd.View.WaitingGUI;

import java.util.List;

/**
 * An actor used to send and receive all the messages in which the player is involved. It's mainly used to comunicate
 * with the {@link WaitingRoomActor} before the game starts and with the {@link GameStateActor} during the game.
 */
public class PlayerActor extends AbstractLoggingActor {

    private final String name;
    private String hostName;
    private Difficulty difficulty;
    private ActorRef waitingActorRef;
    private WaitingController waitingController;
    private List<String> playersNames;
    private GameController gameController;

    /**
     * Create the {@link PlayerActor}. It's used by the props method to make Akka create the actor with his name as
     * parameter.
     *
     * @param name a {@link String} with the name of the player.
     */
    public PlayerActor(final String name) {
        this.name = name;
    }

    /**
     * Creates a Props configuration for the PlayerActor. This method is used to define the properties and
     * configuration for creating instances of the PlayerActor.
     *
     * @param name a {@link String} with the name of the player.
     * @return A {@link Props} object configured for the PlayerActor.
     */
    public static Props props(final String name) {
        return Props.create(PlayerActor.class, () -> new PlayerActor(name));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateWaitingGUIMsg.class, this::handleCreateWaitingGUI)
                .match(UpdateWaitingRoomMsg.class, this::handleUpdateWR)
                .match(CreateGameGUIMsg.class, this::handleCreateGameGUI)
                .match(StartTurnMsg.class, this::handleStartTurn)
                .match(PlayedCardMsg.class, this::handlePlayedCard)
                .match(GameOverMsg.class, this::handleGameOver)
                .match(RematchMsg.class, this::handleRematch)
                .matchAny(message -> System.out.println("\n# UNEXPECTED MESSAGE TO " + getSelf() + ": " + message))
                .build();
    }

    /**
     * Create the {@link sd.Controller.WaitingController}; the difficulty is taken from the content of the message and
     * the host name form a specific method of the message.
     *
     * @param createWaitingGUIMsg the {@link CreateGameGUIMsg} with difficulty and host name.
     */
    private void handleCreateWaitingGUI(final CreateWaitingGUIMsg createWaitingGUIMsg) {
        hostName = createWaitingGUIMsg.getHostName();
        if (name.equals(hostName)) { // only the difficulty choose by the host matter
            difficulty = Difficulty.fromString(createWaitingGUIMsg.getContent());
        }
        waitingController = new WaitingControllerImpl(hostName, name, difficulty);
        waitingController.create();
    }

    /**
     * Update the list of players in the {@link WaitingGUI} via the {@link sd.Controller.WaitingController} because
     * another player joined the game.
     *
     * @param updateWaitingRoomMsg the {@link UpdateWaitingRoomMsg} with the list of players' names.
     */
    private void handleUpdateWR(final UpdateWaitingRoomMsg updateWaitingRoomMsg) {
        waitingActorRef = sender(); // the sender is WaitingRoomActor
        playersNames = updateWaitingRoomMsg.getPlayerNames();
        waitingController.setWaitingRoomActorReference(waitingActorRef);
        waitingController.updatePlayers(playersNames);
    }

    /**
     * Dispose the {@link WaitingGUI} via the {@link WaitingController} and create the {@link GameController}; the game
     * starts and if the player is the host (his name is in the content of the message) it's his turn and can play.
     *
     * @param createGameGUIMsg the {@link CreateGameGUIMsg} with the host name and the name of the next player whose
     *                         turn starts after the end of this player's turn.
     */
    private void handleCreateGameGUI(final CreateGameGUIMsg createGameGUIMsg) {
        waitingController.dispose();
        // the sender() is GameStateActor
        gameController = new GameControllerImpl(name, playersNames, createGameGUIMsg.getDifficulty(), self(), sender(),
                createGameGUIMsg.getNextPlayerName(), getContext().getSystem());
        gameController.startGame(createGameGUIMsg.getGameState(), name.equals(createGameGUIMsg.getContent()));
    }

    /**
     * Make the turn of the next player starts and notify to the others that they have to update their
     * {@link sd.Model.GameState} to make him draw.
     *
     * @param startTurnMsg the {@link StartTurnMsg} with the name of the player who ended his turn and the name of the
     *                     next player whose turn will start.
     */
    private void handleStartTurn(final StartTurnMsg startTurnMsg) {
        if (!startTurnMsg.getContent().equals(name)) { // if this player isn't the one who ended his turn...
            gameController.draw(startTurnMsg.getContent()); // update the game state making him draw to stay consistent
            gameController.updatePlayerInfo();
            gameController.updateRemainingCards();
        }
        if (startTurnMsg.getNextPlayerName().equals(name)) { // check if it's the turn of this player
            gameController.setEnabled(true);
            gameController.checkIfValidMoveExist();
        }
    }

    /**
     * The {@link sd.View.GameGUI} is updated via the {@link GameController} to represent the actual state of the game
     * because a player played a card.
     *
     * @param playedCardMsg the {@link PlayedCardMsg} message with the info about the card played by another player and
     *                      the deck where it was played.
     */
    private void handlePlayedCard(final PlayedCardMsg playedCardMsg) {
        gameController.updateAfterPlayerMove(playedCardMsg.getContent(), // the name of the player who played the card
                playedCardMsg.getCardValue(), playedCardMsg.getDeckNumber());
    }

    /**
     * Comunicate the Game Over to the player with a dialog in the {@link sd.View.GameGUI} via the
     * {@link GameController}.
     *
     * @param gameOverMsg the {@link GameOverMsg} that comunicate the Game Over.
     */
    private void handleGameOver(final GameOverMsg gameOverMsg) {
        gameController.comunicateGameOver();
    }

    /**
     * Show again the {@link WaitingGUI} via a new {@link WaitingController} and send the message to the
     * {@link WaitingRoomActor} to add him again.
     *
     * @param rematchMsg the {@link RematchMsg} that comunicate the fact that the player want to play again.
     */
    private void handleRematch(final RematchMsg rematchMsg) {
        waitingController = new WaitingControllerImpl(hostName, name, difficulty);
        waitingController.create();
        waitingActorRef.tell(new AddPlayerMsg(hostName, name), self());
    }
}
