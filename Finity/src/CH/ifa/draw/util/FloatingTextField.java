/*
 * @(#)FloatingTextField.java 5.1
 *
 */

package CH.ifa.draw.util;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import CH.ifa.draw.figure.TextFigure;

/**
 * A text field overlay that is used to edit a TextFigure.
 * A FloatingTextField requires a two step initialization:
 * In a first step the overlay is created and in a
 * second step it can be positioned.
 *
 * @see TextFigure
 */
public  class FloatingTextField extends Object {

    private JTextField   fEditWidget;
    private JComponent   fContainer;

    public FloatingTextField() {
        fEditWidget = new JTextField(20);
    }

    /**
     * Adds an action listener
     */
	public void addActionListener(ActionListener listener) {
	    fEditWidget.addActionListener(listener);
	}

    /**
     * Creates the overlay for the given Component.
     */
    public void createOverlay(JComponent container) {
        createOverlay(container, null);
    }

    /**
     * Creates the overlay for the given Container using a
     * specific font.
     */
    public void createOverlay(JComponent container, Font font) {
        container.add(fEditWidget, 0);
        if (font != null)
            fEditWidget.setFont(font);
        fContainer = container;
    }

    /**
     * Removes the overlay.
     */
    public void endOverlay() {
	    fContainer.requestFocus();
	    if (fEditWidget == null)
	        return;
	    fEditWidget.setVisible(false);
	    fContainer.remove(fEditWidget);

	    Rectangle bounds = fEditWidget.getBounds();
	    fContainer.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Gets the preferred size of the overlay.
     */
    public Dimension getPreferredSize(int cols) {
    	//MIW: with move to JTextfield replaced:
    	// return fEditWidget.getPreferredSize(cols);
        return fEditWidget.getPreferredSize();
    }

    /**
     * Gets the text contents of the overlay.
     */
    public String getText() {
        return fEditWidget.getText();
    }

    /**
     * Remove an action listener
     */
	public void removeActionListener(ActionListener listener) {
	    fEditWidget.removeActionListener(listener);
	}

    /**
     * Positions the overlay.
     */
    public void setBounds(Rectangle r, String text) {
	    fEditWidget.setText(text);
        fEditWidget.setBounds(r.x, r.y, r.width, r.height);
        fEditWidget.setVisible(true);
	    fEditWidget.selectAll();
	    fEditWidget.requestFocus();
    }
}

