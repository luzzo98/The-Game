package sd.View;

import sd.Controller.WaitingController;
import sd.Utils.ScreenAdapter;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.List;

/**
 * An implementation of {@link WaitingGUI} used to create and display a GUI where the user can wait the other player
 * and see the participants before start the game.
 */
public class SimpleWaitingGUI implements WaitingGUI {

    private final WaitingController controller;
    private final String playerName;
    private final String hostName;
    private final JFrame frame;
    private final JButton startButton;
    private static final int GAP = 10;
    private JPanel center;

    /**
     * Setup some elements of the GUI.
     *
     * @param controller the {@link WaitingController} to used to comunicate the start of the game.
     * @param hostName the player who started the game.
     * @param playerName the player who uses the GUI.
     */
    public SimpleWaitingGUI(final WaitingController controller, final String hostName, final String playerName) {
        this.controller = controller;
        this.frame = new JFrame("The Game");
        this.startButton = new JButton("Inizia partita");
        this.hostName = hostName;
        this.playerName = playerName;
    }

    @Override
    public void create() {
        frame.setSize(ScreenAdapter.waitingGUIDimension());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(GAP, GAP));

        JPanel north = new JPanel();
        JPanel south = new JPanel();
        center = new JPanel();
        frame.add(north, BorderLayout.NORTH);
        frame.add(south, BorderLayout.SOUTH);
        frame.add(center, BorderLayout.CENTER);

        JLabel label = new JLabel("Giocatori in attesa:");
        label.setForeground(Color.RED);
        north.add(label);
        center.setLayout(new GridLayout(0, 1));
        south.add(startButton);

        boolean isHost = playerName.equals(hostName);
        if (!isHost) {
            startButton.setEnabled(false);
        }
        startButton.addActionListener(actionEvent -> controller.comunicateStart());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void updatePlayers(final List<String> playerNames) {
        center.removeAll();
        playerNames.forEach(playerName -> center.add(new JLabel(playerName, SwingConstants.CENTER)));
        center.updateUI();
    }

    @Override
    public void dispose() {
        frame.dispose();
    }
}
