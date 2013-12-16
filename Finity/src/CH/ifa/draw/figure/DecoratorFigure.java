/*
 * @(#)DecoratorFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * DecoratorFigure can be used to decorate other figures with
 * decorations like borders. Decorator forwards all the
 * methods to their contained figure. Subclasses can selectively
 * override these methods to extend and filter their behavior.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld014.htm>Decorator</a></b><br>
 * DecoratorFigure is a decorator.
 *
 * @see Figure
 */

public abstract class DecoratorFigure
                extends AbstractFigure
                implements FigureChangeListener {

    /**
     * The decorated figure.
     */
    protected Figure fComponent;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 8993011151564573288L;
    @SuppressWarnings("unused")
	private int decoratorFigureSerializedDataVersion = 1;

    public DecoratorFigure() { }

    /**
     * Constructs a DecoratorFigure and decorates the passed in figure.
     */
    public DecoratorFigure(Figure figure) {
        decorate(figure);
    }

    /**
     * Forwards basicDisplayBox to its contained figure.
     */
    @Override
	public void basicDisplayBox(Point origin, Point corner) {
        fComponent.basicDisplayBox(origin, corner);
    }

    /**
     * Forwards basicMoveBy to its contained figure.
     */
    @Override
	protected void basicMoveBy(int x, int y) {
        // this will never be called. MIW weak - Exceptions?
    }

    /**
     * Forwards the canConnect to its contained figure..
     */
    @Override
	public boolean canConnect() {
        return fComponent.canConnect();
    }

    /**
     * Returns the locator used to located connected text.
     */
    @Override
	public Locator connectedTextLocator(Figure text) {
        return fComponent.connectedTextLocator(text);
    }

    /**
     * Forwards the connection insets to its contained figure..
     */
    @Override
	public Insets connectionInsets() {
        return fComponent.connectionInsets();
    }

    /**
     * Returns the Connector for the given location.
     */
    @Override
	public Connector connectorAt(int x, int y) {
        return fComponent.connectorAt(x, y);
    }

    /**
     * Forwards the connector visibility request to its component.
     */
    @Override
	public void connectorVisibility(boolean isVisible) {
        fComponent.connectorVisibility(isVisible);
    }

    /**
     * Forwards containsPoint to its contained figure.
     */
    @Override
	public boolean containsPoint(int x, int y) {
        return fComponent.containsPoint(x, y);
    }

    /**
     * Forwards decompose to its contained figure.
     */
    @Override
	public Enumeration<Figure> decompose() {
        return fComponent.decompose();
    }

    /**
     * Decorates the given figure.
     */
    public void decorate(Figure figure) {
        fComponent = figure;
        fComponent.addToContainer(this);
    }

    /**
     * Forwards displayBox to its contained figure.
     */
    @Override
	public Rectangle displayBox() {
        return fComponent.displayBox();
    }

    /**
     * Forwards draw to its contained figure.
     */
    @Override
	public void draw(Graphics g) {
        fComponent.draw(g);
    }

    @Override
	public void figureChanged(FigureChangeEvent e) {
    }

    /**
     * Propagates invalidate up the container chain.
     * @see FigureChangeListener
     */
    @Override
	public void figureInvalidated(FigureChangeEvent e) {
        if (listener() != null)
            listener().figureInvalidated(e);
    }

    @Override
	public void figureRemoved(FigureChangeEvent e) {
    }

    /**
     * Propagates the removeFromDrawing request up to the container.
     * @see FigureChangeListener
     */
    @Override
	public void figureRequestRemove(FigureChangeEvent e) {
        if (listener() != null)
            listener().figureRequestRemove(new FigureChangeEvent(this));
    }

    /**
     * Propagates figureRequestUpdate up the container chain.
     * @see FigureChangeListener
     */
    @Override
	public  void figureRequestUpdate(FigureChangeEvent e) {
        if (listener() != null)
            listener().figureRequestUpdate(e);
    }

    /**
     * Forwards figures to its contained figure.
     */
    // MIW: Replaced FigureEnumeration
    @Override
	public Enumeration<Figure> figures() {
        return fComponent.figures();
    }

    /**
     * Forwards findFigureInside to its contained figure.
     */
    @Override
	public Figure findFigureInside(int x, int y) {
        return fComponent.findFigureInside(x, y);
    }

    /**
     * Forwards getAttribute to its contained figure.
     */
    @Override
	public Object getAttribute(String name) {
        return fComponent.getAttribute(name);
    }

    /**
     * Forwards handles to its contained figure.
     */
    @Override
	public Vector<Handle> handles() {
        return fComponent.handles();
    }

    /**
     * Forwards includes to its contained figure.
     */
    @Override
	public boolean includes(Figure figure) {
        return (super.includes(figure) || fComponent.includes(figure));
    }

    /**
     * Forwards moveBy to its contained figure.
     */
    @Override
	public void moveBy(int x, int y) {
        fComponent.moveBy(x, y);
    }

    /**
     * Removes the decoration from the contained figure.
     */
    public Figure peelDecoration() {
        fComponent.removeFromContainer(this); //??? set the container to the listener()?
        return fComponent;
    }

    /**
     * Reads itself and the contained figure from the StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        decorate((Figure)dr.readStorable());
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException {

        s.defaultReadObject();

        fComponent.addToContainer(this);
    }

    /**
     * Releases itself and the contained figure.
     */
    @Override
	public void release() {
        super.release();
        fComponent.removeFromContainer(this);
        fComponent.release();
    }

    /**
     * Forwards setAttribute to its contained figure.
     */
    @Override
	public void setAttribute(String name, Object value) {
        fComponent.setAttribute(name, value);
    }

    /**
     * Writes itself and the contained figure to the StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeStorable(fComponent);
    }
}
