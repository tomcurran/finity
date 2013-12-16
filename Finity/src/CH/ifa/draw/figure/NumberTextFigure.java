/*
 * @(#)NumberTextFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import CH.ifa.draw.util.FloatingTextField;

/**
 * A TextFigure specialized to edit numbers.
 */
public  class NumberTextFigure extends TextFigure {

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -4056859232918336475L;
    @SuppressWarnings("unused")
	private int numberTextFigureSerializedDataVersion = 1;

    /**
     * Gets the numerical value of the contained text.
     * return the value or 0 in the case of an illegal number format.
     */
    public int getValue() {
        int value = 0;
        try {
            value = Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            value = 0;
        }
        return value;
    }

    /**
     * Gets the number of columns to be used by the text overlay.
     * @see FloatingTextField
     */
    @Override
	public int overlayColumns() {
        return Math.max(4, getText().length());
    }

    /**
     * Sets the numberical value of the contained text.
     */
    public void setValue(int value) {
        setText(Integer.toString(value));
    }

}
