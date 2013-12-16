/*
 * @(#)StandardDrawing.java 5.1
 *
 */

package CH.ifa.draw.standard;

// MIW not used:
// import CH.ifa.draw.util.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.figure.CompositeFigure;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingChangeEvent;
import CH.ifa.draw.framework.DrawingChangeListener;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.NullHandle;
import CH.ifa.draw.locator.RelativeLocator;

/**
 * The standard implementation of the Drawing interface.
 * 
 * @see Drawing
 */

// MIW: Added generic parameters throughout: DrawingChangeListener
// MIW: Used foreach to iterate vectors

public class StandardDrawing extends CompositeFigure implements Drawing {

	/**
	 * the registered listeners
	 */


	private transient Vector<DrawingChangeListener> fListeners;

	/**
	 * boolean that serves as a condition variable to lock the access to the
	 * drawing. The lock is recursive and we keep track of the current lock
	 * holder.
	 */
	private transient Thread fDrawingLockHolder = null;

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = -2602151437447962046L;

	// MIW: not used:
	// private int drawingSerializedDataVersion = 1;

	/**
	 * Constructs the Drawing.
	 */
	public StandardDrawing() {
		super();
		fListeners = new Vector<DrawingChangeListener>(2);
	}

	/**
	 * Adds a listener for this drawing.
	 */
	@Override
	public void addDrawingChangeListener(DrawingChangeListener listener) {
		fListeners.addElement(listener);
	}

	@Override
	public void basicDisplayBox(Point p1, Point p2) {
	}

	/**
	 * Gets the display box. This is the union of all figures.
	*/
	
	@Override
	public Rectangle displayBox() {
		if (fFigures.size() > 0) {
			Enumeration<Figure> k = figures();
			Rectangle r = k.nextElement().displayBox();
			while (k.hasMoreElements())
				r.add(k.nextElement().displayBox());
			return r;
		}
		return new Rectangle(0, 0, 0, 0);
	}

	/**
	 * Adds a listener for this drawing.
	 */
	@Override
	public Enumeration<DrawingChangeListener> drawingChangeListeners() {
		return fListeners.elements();
	}

	/**
	 * Invalidates a rectangle and merges it with the existing damaged area.
	 * 
	 * @see FigureChangeListener
	 */
	@Override
	public void figureInvalidated(FigureChangeEvent e) {
		if (fListeners != null) {
			for (DrawingChangeListener l : fListeners)
				l.drawingInvalidated(new DrawingChangeEvent(this, e.getInvalidatedRectangle()));
		}
	}

	/**
	 * Handles a removeFromDrawing request that is passed up the figure
	 * container hierarchy.
	 * 
	 * @see FigureChangeListener
	 */
	@Override
	public void figureRequestRemove(FigureChangeEvent e) {
		Figure figure = e.getFigure();
		if (fFigures.contains(figure)) {
			fFigures.removeElement(figure);
			figure.removeFromContainer(this); // will invalidate figure
			figure.release();
		} else
			System.out.println("Attempt to remove non-existing figure");
	}

	/**
	 * Forces an update
	 */
	@Override
	public void figureRequestUpdate(FigureChangeEvent e) {
		if (fListeners != null) {
			for (DrawingChangeListener l : fListeners)
				l.drawingRequestUpdate(new DrawingChangeEvent(this, null));
		}
	}

	/**
	 * Return's the figure's handles. This is only used when a drawing is nested
	 * inside another drawing.
	 */
	@Override
	public Vector<Handle> handles() {
		Vector<Handle> handles = new Vector<Handle>();
		handles.addElement(new NullHandle(this, RelativeLocator.northWest()));
		handles.addElement(new NullHandle(this, RelativeLocator.northEast()));
		handles.addElement(new NullHandle(this, RelativeLocator.southWest()));
		handles.addElement(new NullHandle(this, RelativeLocator.southEast()));
		return handles;
	}

	/**
	 * Acquires the drawing lock.
	 */
	@Override
	public synchronized void lock() {
		// recursive lock
		Thread current = Thread.currentThread();
		if (fDrawingLockHolder == current)
			return;
		while (fDrawingLockHolder != null) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
		fDrawingLockHolder = current;
	}

	private void readObject(ObjectInputStream s) throws ClassNotFoundException,
			IOException {

		s.defaultReadObject();

		fListeners = new Vector<DrawingChangeListener>(2);
	}

	/**
	 * Removes the figure from the drawing and releases it.
	 */

	@Override
	public synchronized Figure remove(Figure figure) {
		// ensure that we remove the top level figure in a drawing
		if (figure.listener() != null) {
			figure.listener().figureRequestRemove(
					new FigureChangeEvent(figure, null));
			return figure;
		}
		return null;
	}

	/**
	 * Removes a listener from this drawing.
	 */
	@Override
	public void removeDrawingChangeListener(DrawingChangeListener listener) {
		fListeners.removeElement(listener);
	}

	/**
	 * Releases the drawing lock.
	 */
	@Override
	public synchronized void unlock() {
		if (fDrawingLockHolder != null) {
			fDrawingLockHolder = null;
			notifyAll();
		}
	}
}
