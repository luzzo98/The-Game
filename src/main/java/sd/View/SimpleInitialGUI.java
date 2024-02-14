package sd.View;

import sd.Akka.Actor.PlayerActor;
import sd.Akka.Actor.WaitingRoomActor;
import sd.Controller.InitialController;
import sd.Model.Difficulty;
import sd.Utils.ScreenAdapter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * An implementation of {@link InitialGUI} used to create and display a GUI where the user can insert his name, create
 * a new game or insert the name of a friend and partecipate to his game.
 */
public class SimpleInitialGUI implements InitialGUI {

    private final InitialController controller;
    private final JFrame frame;
    private final JTextField name;
    private final JTextField friend;
    private final JButton joinButton;
    private final JButton createButton;
    private final JComboBox<String> combo;
    private static final int TEXT_SPACE = 20;
    private static final int GAP = 20;

    /**
     * Setup some elements of the GUI.
     *
     * @param controller the {@link InitialController} used to comunicate with the Akka cluster.
     */
    public SimpleInitialGUI(final InitialController controller) {
        this.controller = controller;
        this.frame = new JFrame("The Game");
        this.name = new JTextField(TEXT_SPACE);
        this.friend = new JTextField(TEXT_SPACE);
        this.joinButton = new JButton("Unisciti");
        this.createButton = new JButton("Crea");
        String[] options = {
                Difficulty.NORMAL.toString(), Difficulty.DIFFICULT.toString(), Difficulty.IMPOSSIBLE.toString()
        };
        combo = new JComboBox<>(options);
    }

    @Override
    public void create() {
        frame.setSize(ScreenAdapter.initialGUIDimension());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(GAP, GAP));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // NORTH
        JPanel north = new JPanel();
        frame.add(north, BorderLayout.NORTH);
        north.add(new JLabel("Inserisci il tuo nome:"));
        north.add(name);

        // CENTER
        JPanel center = new JPanel();
        frame.add(center, BorderLayout.CENTER);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // "join" components
        JPanel joinPanel = new JPanel();
        joinPanel.add(new JLabel("Inserisci il nome di un amico che ha gia' creato una partita"));
        JPanel textAndButtonPanel = new JPanel();
        textAndButtonPanel.add(friend);
        textAndButtonPanel.add(joinButton);
        joinPanel.add(textAndButtonPanel);
        center.add(joinPanel);

        // "or" component
        JLabel orLabel = new JLabel("Oppure");
        orLabel.setBorder(new EmptyBorder(GAP, 0, GAP * 2, 0));
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(orLabel);

        // "create" components
        JPanel createPanel = new JPanel();
        createPanel.add(new JLabel("Crea una nuova partita di difficolta':"));
        createPanel.add(combo);
        createPanel.add(createButton);
        center.add(createPanel);
        center.updateUI();

        // join an existing game
        joinButton.addActionListener(actionEvent -> {
            if (name.getText().isEmpty()) {
                showMissingNameDialog();
            } else if (friend.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Inserisci il nome di un altro giocatore",
                        "Giocatore non trovato", JOptionPane.WARNING_MESSAGE);
            } else {
                createPlayerAndWaitingRoom(friend.getText());
            }
        });

        // create a new game
        createButton.addActionListener(actionEvent -> {
            if (name.getText().isEmpty()) {
                showMissingNameDialog();
            } else {
                createPlayerAndWaitingRoom(name.getText());
            }
        });
    }

    /**
     * Create the {@link PlayerActor} and the {@link WaitingRoomActor} if it's the host; send also
     * a message to the {@link PlayerActor} to create the {@link WaitingGUI} and update the players in list.
     *
     * @param hostName the name of the player who created the game (could be the player name itself)
     */
    private void createPlayerAndWaitingRoom(final String hostName) {
        frame.dispose();
        String playerName = name.getText();
        String difficultyAsString = playerName.equals(hostName) ? String.valueOf(combo.getSelectedItem()) : "";
        controller.createPlayerAndWaitingActors(playerName, hostName, difficultyAsString);
    }

    /**
     * Create and show a dialog to comunicate that the user forgets to insert his name in the text field.
     */
    private void showMissingNameDialog() {
        JOptionPane.showMessageDialog(frame, "Inserisci il tuo nome", "Nome assente", JOptionPane.ERROR_MESSAGE);
    }
}
