/*
 * @(#)PaletteListener.java 5.1
 *
 */

package CH.ifa.draw.palette;

/**
 * Interface for handling palette events.
 *
 * @see PaletteButton
 */

public interface PaletteListener {
    /**
     * The user moved the mouse over the palette entry.
     */
    void paletteUserOver(PaletteButton button, boolean inside);

    /**
     * The user selected a palette entry. The selected button is
     * passed as an argument.
     */
    void paletteUserSelected(PaletteButton button);
}
