package sd.Utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

/**
 * Utility class with static methods to get resources.
 */
public class ResourceManager {

    private static final ClassLoader CLASS_LOADER = ResourceManager.class.getClassLoader();
    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGHT = 40;

    /**
     * Method used to obtain the icon of the down arrow for descending decks.
     *
     * @return an {@link ImageIcon} of the down arrow.
     */
    public static ImageIcon getDownIcon() {
        Image down;
        try {
            down = ImageIO.read(Objects.requireNonNull(CLASS_LOADER.getResourceAsStream("down-arrow.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(down.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH));
    }

    /**
     * Method used to obtain the icon of the up arrow for ascending decks.
     *
     * @return an {@link ImageIcon} of the up arrow.
     */
    public static ImageIcon getUpIcon() {
        Image up;
        try {
            up = ImageIO.read(Objects.requireNonNull(CLASS_LOADER.getResourceAsStream("up-arrow.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(up.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH));
    }
}
