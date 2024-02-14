package sd.Utils;

import sd.View.SimpleGameGUI;
import sd.View.SimpleInitialGUI;
import sd.View.SimpleWaitingGUI;

import java.awt.Toolkit;
import java.awt.Dimension;

/**
 * Utility class with static methods to prevent the insertion of hardcoded amount of pixel in frame's dimension.
 */
public class ScreenAdapter {

    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    private static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();
    private static final int WIDTH_SCALE = 3;
    private static final int HEIGHT_SCALE = 4;
    private static final int MEDIUM_HEIGHT_SCALE = 3;
    private static final double BIG_WIDTH_SCALE = 2.5;
    private static final double BIG_HEIGHT_SCALE = 1.5;

    /**
     * Method used to dimension the frame of the {@link SimpleInitialGUI}.
     *
     * @return the {@link Dimension} at which to set the GUI.
     */
    public static Dimension initialGUIDimension() {
        return new Dimension(SCREEN_WIDTH / WIDTH_SCALE, SCREEN_HEIGHT / MEDIUM_HEIGHT_SCALE);
    }

    /**
     * Method used to dimension the frame of the {@link SimpleWaitingGUI}.
     *
     * @return the {@link Dimension} at which to set the GUI.
     */
    public static Dimension waitingGUIDimension() {
        return new Dimension(SCREEN_WIDTH / WIDTH_SCALE, SCREEN_HEIGHT / HEIGHT_SCALE);
    }

    /**
     * Method used to dimension the frame of the {@link SimpleGameGUI}.
     *
     * @return the {@link Dimension} at which to set the GUI.
     */
    public static Dimension gameGUIDimension() {
        return new Dimension((int) (SCREEN_WIDTH / BIG_WIDTH_SCALE), (int) (SCREEN_HEIGHT / BIG_HEIGHT_SCALE));
    }
}
