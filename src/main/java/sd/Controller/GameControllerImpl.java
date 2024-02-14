package sd.Controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.Cluster;
import sd.Akka.Actor.GameStateActor;
import sd.Akka.Messages.EndTurnMsg;
import sd.Akka.Messages.GameOverMsg;
import sd.Akka.Messages.PlayedCardMsg;
import sd.Akka.Messages.RematchMsg;
import sd.Model.Difficulty;
import sd.Model.GameState;
import sd.Model.Player;
import sd.View.SimpleGameGUI;
import sd.Model.GameRules;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class with the implementation of the {@link GameController} interface.
 */
public class GameControllerImpl implements GameController {

    private final SimpleGameGUI gui;
    private final String playerName;
    private final String nextPlayerName;
    private Player player;
    private final Difficulty difficulty;
    private final ActorRef playerActor;
    private final ActorRef gameStateActor;
    private GameState gameState;
    private final ActorSystem system;

    /**
     * Create an implementation of a {@link GameController}.
     * A {@link GameState} and a {@link SimpleGameGUI} will be created.
     *
     * @param playerName the name of this player.
     * @param playerNames the name of all players.
     * @param difficulty the difficulty of the game.
     * @param playerActor the {@link ActorRef} of the player used to recreate the {@link sd.View.WaitingGUI} if he wants
     *                    a rematch.
     * @param gameStateActor the {@link ActorRef} of the {@link GameStateActor} used to say to the other players
     *                       which cards are played.
     * @param nextPlayerName the name of the next player who will play after the end of the turn.
     * @param system the {@link ActorSystem} that coordinate the actors and manage the environment.
     */
    public GameControllerImpl(final String playerName, final List<String> playerNames, final Difficulty difficulty,
                              final ActorRef playerActor, final ActorRef gameStateActor, final String nextPlayerName,
                              final ActorSystem system) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.playerActor = playerActor;
        this.gameStateActor = gameStateActor;
        this.nextPlayerName = nextPlayerName;
        this.system = system;
        gui = new SimpleGameGUI(this, playerName, playerNames);
    }

    @Override
    public void startGame(final GameState gameState, final boolean isFirst) {
        this.gameState = gameState;
        if (gameState.getPlayer(playerName).isPresent()) { // if the player exists, the game starts
            gui.create(gameState.countMainDeckCards());
            player = gameState.getPlayer(playerName).get();
            gui.clearAndSetHand(player.getHand());
            gui.setCardsEnabled(isFirst);
        } else {
            throw new RuntimeException("Player not found when starting the game");
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        gui.setCardsEnabled(enabled);
    }

    @Override
    public void updateAfterPlayerMove(final String playerName, final int cardValue, final int deckNumber) {
        if (gameState.getPlayer(playerName).isPresent()) {
            gameState.playedCard(gameState.getPlayer(playerName).get(), cardValue, deckNumber);
            gui.updateAfterPlayerMove(playerName, cardValue, deckNumber);
        } else {
            throw new IllegalArgumentException("Player not found");
        }
    }

    @Override
    public void updatePlayerInfo() {
        gui.updatePlayersInfo();
    }

    @Override
    public void playedCard(final int cardValue, final int deckNumber) {
        gameState.playedCard(player, cardValue, deckNumber);
        gameStateActor.tell(new PlayedCardMsg(null, playerName, cardValue, deckNumber), ActorRef.noSender());
    }

    @Override
    public void endOfTurn() {
        gameStateActor.tell(new EndTurnMsg(null, playerName, nextPlayerName), ActorRef.noSender());
    }

    @Override
    public void rematch() {
        playerActor.tell(new RematchMsg(null), ActorRef.noSender());
    }

    @Override
    public LinkedHashMap<String, Integer> countPlayersCards() {
        return gameState.countPlayersCards();
    }

    @Override
    public List<Integer> draw(final String playerName) {
        if (gameState.getPlayer(playerName).isPresent()) {
            return gameState.draw(gameState.getPlayer(playerName).get());
        } else {
            throw new IllegalArgumentException("Player not found");
        }
    }

    @Override
    public int countMainDeckCards() {
        return gameState.countMainDeckCards();
    }

    @Override
    public void updateRemainingCards() {
        gui.updateRemainingCards();
    }

    @Override
    public int cardsNotPlayed() {
        return gameState.cardNotPlayed();
    }

    @Override
    public boolean isWin() {
        return gameState.isWin();
    }

    @Override
    public void checkIfValidMoveExist() {
        if (!player.getHand().isEmpty() && !gameState.validMoveExist(player)) {
            notifyGameOverToOthers();
        }
    }

    @Override
    public boolean checkIfValidMoveExist(final String playerName) {
        if (gameState.getPlayer(playerName).isPresent()) {
            return gameState.validMoveExist(gameState.getPlayer(playerName).get());
        } else {
            throw new IllegalArgumentException("Player not found");
        }
    }

    @Override
    public void comunicateGameOver() {
        gui.comunicateGameOver();
    }

    @Override
    public void notifyGameOverToOthers() {
        gameStateActor.tell(new GameOverMsg(null), ActorRef.noSender());
    }

    @Override
    public boolean canFinishTurn(final int cardsPlayedThisTurn) {
        return cardsPlayedThisTurn >= GameRules.cardsPerTurn(difficulty) || gameState.countMainDeckCards() == 0;
    }

    @Override
    public boolean isAscValid(final int deckValue, final int cardSelected) {
        return GameRules.isAscValid(deckValue, cardSelected);
    }

    @Override
    public boolean isDescValid(final int deckValue, final int cardSelected) {
        return GameRules.isDescValid(deckValue, cardSelected);
    }

    @Override
    public void exitGame() {
        Cluster.get(system).leave(Cluster.get(system).selfAddress());
        System.exit(0);
    }
}
