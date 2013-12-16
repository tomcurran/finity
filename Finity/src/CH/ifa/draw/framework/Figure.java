/*
 * @(#)Figure.java 5.1
 *
 */

package CH.ifa.draw.framework;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.figure.AbstractFigure;
import CH.ifa.draw.storable.Storable;

/**
 * The interface of a graphical figure. A figure knows
 * its display box and can draw itself. A figure can be
 * composed of several figures. To interact and manipulate
 * with a figure it can provide Handles and Connectors.<p>
 * A figure has a set of handles to manipulate its shape or attributes.
 * A figure has one or more connectors that define how
 * to locate a connection point.<p>
 * Figures can have an open ended set of attributes.
 * An attribute is identified by a string.<p>
 * Default implementations for the Figure interface are provided
 * by AbstractFigure.
 *
 * @see Handle
 * @see Connector
 * @see AbstractFigure
 */

public interface Figure
                extends Storable, Cloneable, Serializable {

    /**
     * Adds a listener for this figure.
     */
    public void addFigureChangeListener(FigureChangeListener l);

    /**
     * Sets the Figure's container and registers the container
     * as a figure change listener. A figure's container can be
     * any kind of FigureChangeListener. A figure is not restricted
     * to have a single container.
     */
    public void addToContainer(FigureChangeListener c);

    /**
     * Changes the display box of a figure. This method is
     * always implemented in figure subclasses.
     * It only changes
     * the displaybox and does not announce any changes. It
     * is usually not called by the client. Clients typically call
     * displayBox to change the display box.
     * @param origin the new origin
     * @param corner the new corner
     * @see #displayBox
     */
    public void basicDisplayBox(Point origin, Point corner);

    /**
     * Checks if this figure can be connected
     */
    public boolean canConnect();

    /**
     * Gets the figure's center
     */
    public Point center();

    /**
     * Informs that a figure has changed its display box.
     * This method also triggers an update call for its
     * registered observers.
     * @see #invalidate
     * @see #willChange
     *
     */
    public void changed();

    /**
     * Returns a Clone of this figure
     */
    public Object clone();

    /**
     * Returns the locator used to located connected text.
     */
    public Locator connectedTextLocator(Figure text);

    /**
     * Returns the connection inset. This is only a hint that
     * connectors can use to determine the connection location.
     * The inset defines the area where the display box of a
     * figure should not be connected.
     *
     */
    public Insets connectionInsets();

    /**
     * Gets a connector for this figure at the given location.
     * A figure can have different connectors at different locations.
     */
    public Connector connectorAt(int x, int y);

    /**
     * Sets whether the connectors should be visible.
     * Connectors can be optionally visible. Implement
     * this method and react on isVisible to turn the
     * connectors on or off.
     */
    public void connectorVisibility(boolean isVisible);

    /**
     * Checks if a point is inside the figure.
     */
    public boolean containsPoint(int x, int y);

    /**
     * Decomposes a figure into its parts. A figure is considered
     * as a part of itself.
     */
    public Enumeration<Figure> decompose();

    /**
     * Gets the display box of a figure
     * @see #basicDisplayBox
     */
    public Rectangle displayBox();

    /**
     * Changes the display box of a figure. Clients usually
     * invoke this method. It changes the display box
     * and announces the corresponding changes.
     * @param origin the new origin
     * @param corner the new corner
     * @see #displayBox
     */
    public void displayBox(Point origin, Point corner);

    /**
     * Changes the display box of a figure. This is a
     * convenience method. Implementors should only
     * have to override basicDisplayBox
     * @see #displayBox
     */
    public void displayBox(Rectangle r);

    /**
     * Draws the figure.
     * @param g the Graphics to draw into
     */
    public void draw(Graphics g);

    /**
     * Returns an Enumeration of the figures contained in this figure
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Enumeration<Figure> figures();

    /**
     * Returns the figure that contains the given point.
     */
    public Figure findFigureInside(int x, int y);

    /**
     * Returns the named attribute or null if a
     * a figure doesn't have an attribute.
     * All figures support the attribute names
     * FillColor and FrameColor
     */
    public Object getAttribute(String name);

    /**
     * Returns the handles used to manipulate
     * the figure. Handles is a Factory Method for
     * creating handle objects.
     *
     * @return a Vector of handles
     * @see Handle
     */
    // MIW Added generic parameter to Vector
    // Change to specific type - Handle?
    public Vector<Handle> handles();

    /**
     * Checks whether the given figure is contained in this figure.
     */
    public boolean includes(Figure figure);

    /**
     * Invalidates the figure. This method informs its listeners
     * that its current display box is invalid and should be
     * refreshed.
     */
    public void invalidate();

    /**
     * Checks if the Figure should be considered as empty.
     */
    public boolean isEmpty();

    /**
     * Gets the Figure's listeners.
     */
    public FigureChangeListener listener();

    /**
     * Moves the Figure to a new location.
     * @param x the x delta
     * @param y the y delta
     */
    public void moveBy(int dx, int dy);

    /**
     * Releases a figure's resources. Release is called when
     * a figure is removed from a drawing. Informs the listeners that
     * the figure is removed by calling figureRemoved.
     */
    public void release();

    /**
     * Removes a listener for this figure.
     */
    public void removeFigureChangeListener(FigureChangeListener l);

    /**
     * Removes a figure from the given container and unregisters
     * it as a change listener.
     */
    public void removeFromContainer(FigureChangeListener c);

    /**
     * Sets the named attribute to the new value
     */
    public void setAttribute(String name, Object value);

    /**
     * Gets the size of the figure
     */
    public Dimension size();

    /**
     * Informs that a figure is about to change such that its
     * display box is affected.
     * Here is an example of how it is used together with changed()
     * <pre>
     * public void move(int x, int y) {
     *      willChange();
     *      // change the figure's location
     *      changed();
     *  }
     * </pre>
     * @see #invalidate
     * @see #changed
     */
    public void willChange();
}
