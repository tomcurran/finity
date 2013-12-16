/*
 * @(#)AbstractFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.connector.ChopBoxConnector;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.locator.RelativeLocator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.util.FigureChangeEventMulticaster;
import CH.ifa.draw.util.Geom;

/**
 * AbstractFigure provides default implementations for
 * the Figure interface.
 *
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld036.htm>Template Method</a></b><br>
 * Template Methods implement default and invariant behavior for
 * figure subclasses.
 * <hr>
 *
 * @see Figure
 * @see Handle
 */

public abstract class AbstractFigure implements Figure {

    /**
     * The listeners for a figure's changes.
     * @see #invalidate
     * @see #changed
     * @see #willChange
     */
    private transient FigureChangeListener fListener;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -10857585979273442L;
    // MIW: removed cause not used
    // private int abstractFigureSerializedDataVersion = 1;

    protected AbstractFigure() { }

    /**
     * Adds a listener for this figure.
     */
    @Override
	public void addFigureChangeListener(FigureChangeListener l) {
        fListener = FigureChangeEventMulticaster.add(fListener, l);
    }

    /**
     * Sets the Figure's container and registers the container
     * as a figure change listener. A figure's container can be
     * any kind of FigureChangeListener. A figure is not restricted
     * to have a single container.
     */
    @Override
	public void addToContainer(FigureChangeListener c) {
        addFigureChangeListener(c);
        invalidate();
    }

    /**
     * Sets the display box of a figure. This is the
     * method that subclassers override. Clients usually
     * call displayBox.
     * @see displayBox
     */
    @Override
	public abstract void basicDisplayBox(Point origin, Point corner);

    /**
     * Moves the figure. This is the
     * method that subclassers override. Clients usually
     * call displayBox.
     * @see moveBy
     */
    protected abstract void basicMoveBy(int dx, int dy);

    /**
     * Checks if this figure can be connected. By default
     * AbstractFigures can be connected.
     */
    @Override
	public boolean canConnect() {
        return true;
    }

    /**
     * Gets the center of a figure. A convenience
     * method that is rarely overridden.
     */
    @Override
	public Point center() {
        return Geom.center(displayBox());
    }

    /**
     * Informs that a figure changed the area of its display box.
     *
     * @see FigureChangeEvent
     * @see Figure#changed
     */
    @Override
	public void changed() {
        invalidate();
        if (fListener != null)
            fListener.figureChanged(new FigureChangeEvent(this));

    }

    /**
     * Clones a figure. Creates a clone by using the storable
     * mechanism to flatten the Figure to stream followed by
     * resurrecting it from the same stream.
     *
     * @see Figure#clone
     */
    @Override
	public Object clone() {
        Object clone = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream(200);
        try {
            ObjectOutput writer = new ObjectOutputStream(output);
            writer.writeObject(this);
            writer.close();
        } catch (IOException e) {
            System.out.println("Class not found: " + e);
        }

        InputStream input = new ByteArrayInputStream(output.toByteArray());
        try {
            ObjectInput reader = new ObjectInputStream(input);
            clone = reader.readObject();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e);
        }
        return clone;
    }

    /**
     * Returns the locator used to located connected text.
     */
    @Override
	public Locator connectedTextLocator(Figure text) {
        return RelativeLocator.center();
    }

    /**
     * Returns the connection inset. The connection inset
     * defines the area where the display box of a
     * figure can't be connected. By default the entire
     * display box can be connected.
     *
     */
    @Override
	public Insets connectionInsets() {
        return new Insets(0, 0, 0, 0);
    }

    /**
     * Returns the Figures connector for the specified location.
     * By default a ChopBoxConnector is returned.
     * @see ChopBoxConnector
     */
    @Override
	public Connector connectorAt(int x, int y) {
        return new ChopBoxConnector(this);
    }

    /**
     * Sets whether the connectors should be visible.
     * By default they are not visible and
     */
    @Override
	public void connectorVisibility(boolean isVisible) {
    }

    /**
     * Checks if a point is inside the figure.
     */
    @Override
	public boolean containsPoint(int x, int y) {
        return displayBox().contains(x, y);
    }

    /**
     * Decomposes a figure into its parts. It returns a Vector
     * that contains itself.
     * @return an Enumeration for a Vector with itself as the
     * only element.
     */
    // MIW: Added Figure to Vector
    @Override
	public Enumeration<Figure> decompose() {
        Vector<Figure> figures = new Vector<Figure>(1);
        figures.addElement(this);
        return figures.elements();
    }

    /**
     * Gets the display box of a figure.
     */
    @Override
	public abstract Rectangle displayBox();

    /**
     * Changes the display box of a figure. Clients usually
     * call this method. It changes the display box
     * and announces the corresponding change.
     * @param origin the new origin
     * @param corner the new corner
     * @see displayBox
     */
    @Override
	public void displayBox(Point origin, Point corner) {
        willChange();
        basicDisplayBox(origin, corner);
        changed();
    }

    /**
     * Changes the display box of a figure. This is a
     * convenience method. Implementors should only
     * have to override basicDisplayBox
     * @see displayBox
     */
    @Override
	public void displayBox(Rectangle r) {
        displayBox(new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height));
    }

    /**
     * Returns an Enumeration of the figures contained in this figure.
     * @see CompositeFigure
     */
    // MIW: Added Figure to Vector. Replaced FigureEnumeration by Enumeration<Figure>
    @Override
	public Enumeration<Figure> figures() {
        Vector<Figure> figures = new Vector<Figure>(1);
        figures.addElement(this);
        Enumeration<Figure> efigs=figures.elements();
        return efigs ;
    }

    /**
     * Returns the figure that contains the given point.
     * In contrast to containsPoint it returns its
     * innermost figure that contains the point.
     *
     * @see #containsPoint
     */
    @Override
	public Figure findFigureInside(int x, int y) {
        if (containsPoint(x, y))
            return this;
        return null;
    }

    /**
     * Returns the named attribute or null if a
     * a figure doesn't have an attribute.
     * By default
     * figures don't have any attributes getAttribute
     * returns null.
     */
    @Override
	public Object getAttribute(String name) {
        return null;
    }

    /**
     * Returns the handles of a Figure that can be used
     * to manipulate some of its attributes.
     * @return a Vector of handles
     * @see Handle
     */
    
    //MIW: Added Handle to Vector
    @Override
	public abstract Vector<Handle> handles();

    /**
     * Checks whether the given figure is contained in this figure.
     */
    @Override
	public boolean includes(Figure figure) {
        return figure == this;
    }

    /**
     * Invalidates the figure. This method informs the listeners
     * that the figure's current display box is invalid and should be
     * refreshed.
     */
    @Override
	public void invalidate() {
        if (fListener != null) {
            Rectangle r = displayBox();
            r.grow(2*Handle.HANDLESIZE, 2*Handle.HANDLESIZE);
            fListener.figureInvalidated(new FigureChangeEvent(this, r));
        }
    }

    /**
     * Checks if the figure is empty. The default implementation returns
     * true if the width or height of its display box is < 3
     * @see Figure#isEmpty
     */
    @Override
	public boolean isEmpty() {
        return (size().width < 3) || (size().height < 3);
    }

    /**
     * Gets the figure's listeners.
     */
    @Override
	public FigureChangeListener listener() {
        return fListener;
    }

    /**
     * Moves the figure by the given offset.
     */
    @Override
	public void moveBy(int dx, int dy) {
        willChange();
        basicMoveBy(dx, dy);
        changed();
    }

    /**
     * Reads the Figure from a StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
    }

    /**
     * A figure is released from the drawing. You never call this
     * method directly. Release notifies its listeners.
     * @see Figure#release
     */
    @Override
	public void release() {
        if (fListener != null)
            fListener.figureRemoved(new FigureChangeEvent(this));
    }

    /**
     * Removes a listener for this figure.
     */
    @Override
	public void removeFigureChangeListener(FigureChangeListener l) {
        fListener = FigureChangeEventMulticaster.remove(fListener, l);
    }

    /**
     * Removes a figure from the given container and unregisters
     * it as a change listener.
     */
    @Override
	public void removeFromContainer(FigureChangeListener c) {
        invalidate();
        removeFigureChangeListener(c);
        changed();
    }

    /**
     * Sets the named attribute to the new value. By default
     * figures don't have any attributes and the request is ignored.
     */
    @Override
	public void setAttribute(String name, Object value) {
    }

    /**
     * Gets the size of the figure. A convenience method.
     */
    @Override
	public Dimension size() {
        return new Dimension(displayBox().width, displayBox().height);
    }

    /**
     * Informs that a figure is about to change something that
     * affects the contents of its display box.
     *
     * @see Figure#willChange
     */
    @Override
	public void willChange() {
        invalidate();
    }

    /**
     * Stores the Figure to a StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
    }

}
