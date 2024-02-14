package sd.View;

import sd.Controller.GameController;
import sd.Utils.ResourceManager;
import sd.Utils.ScreenAdapter;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of {@link GameGUI} used to create and display a GUI where the user can play the game.
 */
public class SimpleGameGUI implements GameGUI {

    private final GameController controller;
    private final String playerName;
    private final List<String> playerNames;
    private int cardPlayedThisTurn = 0;
    private final AtomicInteger remainingCards = new AtomicInteger(0);
    private static final String DEFAULT_DECK_TEXT = "Mazzo: ";

    private final ButtonGroup btnGroup = new ButtonGroup();
    private final List<JToggleButton> cards = new LinkedList<>();
    private final List<JButton> decks = new LinkedList<>();
    private JToggleButton cardSelected;
    private JButton done;
    private JLabel deckLabel;

    private JFrame frame;
    private final ImageIcon upIcon;
    private final ImageIcon downIcon;
    private final JPanel north;
    private final JPanel south;
    private final JPanel center;
    private final JPanel west;
    private final JPanel east;


    private static final int GAP = 20;
    private static final int BIG_GAP = 50;

    /**
     * Setup some elements of the GUI.
     *
     * @param controller the {@link GameController} with which the GUI communicates.
     * @param playerName the name of the player that will use the GUI.
     * @param playerNames the names of all the players of the match.
     */
    public SimpleGameGUI(final GameController controller, final String playerName, final List<String> playerNames) {
        this.controller = controller;
        this.playerName = playerName;
        this.playerNames = playerNames;

        north = new JPanel();
        south = new JPanel();
        center = new JPanel();
        west = new JPanel();
        east = new JPanel();

        downIcon = ResourceManager.getDownIcon();
        upIcon = ResourceManager.getUpIcon();
    }

    @Override
    public void create(final int remainingCards) {
        initialSetup(remainingCards);
        setupCenterComponents();
        setupEastComponents();
        setupSouthComponents();
    }

    @Override
    public void setCardsEnabled(final boolean enabled) {
        cards.forEach(card -> card.setEnabled(enabled));
        if (enabled && cards.isEmpty()) {
            done.setEnabled(true);
            done.setBackground(Color.green);
        }
    }

    @Override
    public void clearAndSetHand(final List<Integer> newHand) {
        south.removeAll();
        cards.forEach(btnGroup::remove);
        cards.clear();
        newHand.forEach(this::addNewCard);
        cards.forEach(card -> {
            south.add(card);
            btnGroup.add(card);
        });
        south.add(done);
        south.updateUI();
    }

    @Override
    public void updateAfterPlayerMove(final String playerName, final int cardValue, final int deckNumber) {
        updatePlayersInfo();
        decks.get(deckNumber).setText(String.valueOf(cardValue));
        if (controller.isWin()) {
            handleFinish("Vittoria!", "Complimenti, hai vinto! \nVuoi fare una rivincita?");
        }
    }

    @Override
    public void comunicateGameOver() {
        handleFinish("Game Over!",
                "Peccato, hai perso! Rimanevano "
                        + controller.cardsNotPlayed()
                        + " carte da giocare.\nVuoi fare una rivincita?");
    }

    /**
     * Add a new card to the player's hand by creating a new button with the proper action listener.
     *
     * @param cardValue the value of the card to add.
     */
    private void addNewCard(final Integer cardValue) {
        JToggleButton newCard = new JToggleButton(cardValue.toString());
        newCard.addActionListener(actionEvent -> {
            enableValidDecks(cardValue);
            cardSelected = newCard;
        });
        cards.add(newCard);
    }

    @Override
    public void updatePlayersInfo() {
        east.removeAll();
        east.add(Box.createVerticalGlue());
        controller.countPlayersCards().forEach((player, count) -> {
            JLabel label = new JLabel(player + " : " + count);
            if (player.equals(playerName)) {
                label.setForeground(Color.RED);
            }
            east.add(label);
            east.add(Box.createVerticalGlue());
        });
        east.add(deckLabel);
        east.add(Box.createVerticalGlue());
        east.updateUI();
    }

    @Override
    public void updateRemainingCards() {
        deckLabel.setText(DEFAULT_DECK_TEXT + controller.countMainDeckCards());
        east.updateUI();
    }

    /**
     * Enable the decks buttons where the card selected can be played and disable the others.
     *
     * @param cardValue the value of the card selected.
     */
    private void enableValidDecks(final int cardValue) {
        List.of(0, 1).forEach(n -> decks.get(n).setEnabled(controller.isAscValid(btnVal(decks.get(n)), cardValue)));
        List.of(2, 3).forEach(n -> decks.get(n).setEnabled(controller.isDescValid(btnVal(decks.get(n)), cardValue)));
    }

    /**
     * Abbreviation to get the int value of a button.
     *
     * @param deck the button with the value to extract.
     * @return the int value.
     */
    private int btnVal(final AbstractButton deck) {
        return Integer.parseInt(deck.getText());
    }

    /**
     * Defines the behaviour of a deck button: removes the played card, updates the value of the deck, updates the info
     * and check if the game is finished. It also comunicate to the controller that the other player have to be
     * informed about the played card.
     *
     * @param deck the button of the deck.
     * @param deckNumber the number of the deck where the card was played to comunicate to the other players.
     * @return an {@link ActionListener} with the actions performed by the done button when pressed.
     */
    private ActionListener decksListener(final JButton deck, final int deckNumber) {
        return ActionEvent -> {
            btnGroup.remove(cardSelected);
            cards.remove(cardSelected);
            south.remove(cardSelected);
            south.updateUI();

            deck.setText(cardSelected.getText());
            controller.playedCard(Integer.parseInt(cardSelected.getText()), deckNumber);
            decks.forEach(d -> d.setEnabled(false));
            if (controller.canFinishTurn(++cardPlayedThisTurn)) {
                done.setEnabled(true);
                done.setBackground(Color.green);
            }
            updatePlayersInfo();
            checkIfIsFinished();
        };
    }

    /**
     * Defines the behaviour of the done button: it disables itself, draws and sets the new cards, updates the info,
     * checks if the game is finished and ends the turn of the player.
     *
     * @return an {@link ActionListener} with the actions performed by the done button when pressed.
     */
    private ActionListener doneListener() {
        return actionEvent -> {
            done.setEnabled(false);
            done.setBackground(Color.lightGray);

            clearAndSetHand(controller.draw(playerName));
            south.updateUI();
            remainingCards.set(controller.countMainDeckCards());
            deckLabel.setText(DEFAULT_DECK_TEXT + remainingCards.get());
            updatePlayersInfo();

            checkIfIsFinished();
            cardPlayedThisTurn = 0;
            decks.forEach(deck -> deck.setEnabled(false)); // needed because can select a card and then press "done"
            if (playerNames.size() > 1) {
                setCardsEnabled(false);
            }
            controller.endOfTurn();
        };
    }

    /**
     * Check if the game is finished with a win if all the cards are played or if there is a game over.
     */
    private void checkIfIsFinished() {
        if (controller.isWin()) {
            handleFinish("Vittoria!", "Complimenti, hai vinto! \nVuoi fare una rivincita?");
            return;
        }
        if (cards.isEmpty() || controller.canFinishTurn(cardPlayedThisTurn)
                || controller.checkIfValidMoveExist(playerName)) {
            return;
        }
        controller.notifyGameOverToOthers();
    }

    /**
     * Display a dialog at the end of the match that gives the possibility to make a rematch or quit the game.
     *
     * @param title the title of the dialog.
     * @param message the message of the dialog.
     */
    private void handleFinish(final String title, final String message) {
        int result = JOptionPane.showConfirmDialog(
                frame,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (result == 0) { // want a rematch
            frame.dispose();
            controller.rematch();
        } else { // exit game
            controller.exitGame();
        }
    }

    /**
     * Basic GUI operation like creation of the frame, the layout setting, positioning and dimensioning.
     *
     * @param remainingCards the number of the remaining cards in the main deck.
     */
    private void initialSetup(final int remainingCards) {
        this.remainingCards.set(remainingCards);
        frame = new JFrame("The Game");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(ScreenAdapter.gameGUIDimension());
        frame.setLayout(new BorderLayout(GAP, GAP));

        south.setBorder(new EmptyBorder(GAP, 0, GAP, 0));
        east.setBorder(new EmptyBorder(0, 0, 0, BIG_GAP));
        center.setBorder(new EmptyBorder(GAP, 0, 0, BIG_GAP));
        frame.add(north, BorderLayout.NORTH);
        frame.add(south, BorderLayout.SOUTH);
        frame.add(center, BorderLayout.CENTER);
        frame.add(west, BorderLayout.WEST);
        frame.add(east, BorderLayout.EAST);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Creation of the component in the central part of the GUI like the buttons for the decks, labels and icons.
     */
    private void setupCenterComponents() {
        GridLayout layout = new GridLayout(2, 4, 0, BIG_GAP);
        center.setLayout(layout);

        JLabel labelAsc1 = new JLabel("1");
        JLabel labelAsc2 = new JLabel("1");
        JLabel labelDesc1 = new JLabel("99");
        JLabel labelDesc2 = new JLabel("99");
        labelAsc1.setForeground(Color.RED);
        labelAsc2.setForeground(Color.RED);
        labelDesc1.setForeground(Color.RED);
        labelDesc2.setForeground(Color.RED);

        JLabel ascIcon1 = new JLabel(upIcon);
        JLabel ascIcon2 = new JLabel(upIcon);
        JLabel descIcon1 = new JLabel(downIcon);
        JLabel descIcon2 = new JLabel(downIcon);

        JButton btnDeck1 = new JButton("1");
        JButton btnDeck2 = new JButton("1");
        JButton btnDeck4 = new JButton("99");
        JButton btnDeck3 = new JButton("99");
        decks.addAll(List.of(btnDeck1, btnDeck2, btnDeck3, btnDeck4));
        for (int i = 0; i < 4; i++) {
            JButton deck = decks.get(i);
            deck.addActionListener(decksListener(deck, i));
            deck.setEnabled(false);
        }

        JPanel panel1 = setupDeckLabel(labelAsc1, ascIcon1);
        JPanel panel2 = setupDeckLabel(labelAsc2, ascIcon2);
        JPanel panel3 = setupDeckLabel(descIcon1, labelDesc1);
        JPanel panel4 = setupDeckLabel(descIcon2, labelDesc2);
        List.of(panel1, btnDeck1, panel2, btnDeck2, panel3, btnDeck3, panel4, btnDeck4)
                .forEach(center::add);
    }

    /**
     * Creation of the components in the east part of the GUI like the labels with the number of the cards remaining in
     * the deck and the labels of the players.
     */
    private void setupEastComponents() {
        east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
        deckLabel = new JLabel(DEFAULT_DECK_TEXT + remainingCards);
        deckLabel.setForeground(Color.BLUE);
        updatePlayersInfo();
    }

    /**
     * Creation of the "done" button.
     */
    private void setupSouthComponents() {
        done = new JButton("Finito");
        done.setEnabled(false);
        done.setBackground(Color.lightGray);
        done.addActionListener(doneListener());
    }

    /**
     * Method used to create a panel with two label one above the other and aligned to the center.
     *
     * @param top the {@link JLabel} that will be at the top.
     * @param bottom the {@link JLabel} that will be at the bottom.
     * @return a {@link JPanel} with the two aligned labels.
     */
    private JPanel setupDeckLabel(final JLabel top, final JLabel bottom) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(bottom);
        panel.add(Box.createRigidArea(new Dimension(0, GAP)));
        panel.add(top);
        panel.add(Box.createVerticalGlue());
        top.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }
}
