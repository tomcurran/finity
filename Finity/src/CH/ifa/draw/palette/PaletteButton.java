/*
 * @(#)PaletteButton.java 5.1
 *
 */

package CH.ifa.draw.palette;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/**
 * A palette button is a three state button. The states are normal pressed and
 * selected. It uses to the palette listener interface to notify about state
 * changes.
 * 
 * @see PaletteListener
 * @see PaletteLayout
 */

// MIW JPanel replaces Canvas in Swing
public abstract class PaletteButton extends JPanel 
		implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 4015046863142712415L;

	// static final int NORMAL = 1;
	// static final int PRESSED = 2;
	// static final int SELECTED = 3;
	// MIW replaced with enum

	private enum ButtonState {
		NORMAL, PRESSED, SELECTED
	};

	private PaletteListener fListener;
	private ButtonState fState;
	private ButtonState fOldState;

	/**
	 * Constructs a PaletteButton.
	 * 
	 * @param listener
	 *            the listener to be notified.
	 */
	public PaletteButton(PaletteListener listener) {
		fListener = listener;
		fState = fOldState = ButtonState.NORMAL;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (contains(e.getX(), e.getY()))
			fState = ButtonState.PRESSED;
		else
			fState = fOldState;
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (fState == ButtonState.PRESSED) // JDK1.1 on Windows sometimes loses
											// mouse released
			mouseDragged(e);
		fListener.paletteUserOver(this, false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		fListener.paletteUserOver(this, true);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		fOldState = fState;
		fState = ButtonState.PRESSED;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		fState = fOldState;
		repaint();
		if (contains(e.getX(), e.getY()))
			fListener.paletteUserSelected(this);
	}

	public String name() {
		return "";
	}

	public abstract void paintBackground(Graphics g);

	@Override
	public void paintComponent(Graphics g) { // MIW replaced paint by
												// paintComponent AWT -> Swing
		super.paintComponent(g); // MIW - Swing protocol
		paintBackground(g);

		switch (fState) {
		case PRESSED:
			paintPressed(g);
			break;
		case SELECTED:
			paintSelected(g);
			break;
		case NORMAL:
		default:
			paintNormal(g);
			break;
		}
	}

	public abstract void paintNormal(Graphics g);

	public abstract void paintPressed(Graphics g);

	public abstract void paintSelected(Graphics g);

	public void reset() {
		fState = ButtonState.NORMAL;
		repaint();
	}

	public void select() {
		fState = ButtonState.SELECTED;
		repaint();
	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

	public Object value() {
		return null;
	}
}
