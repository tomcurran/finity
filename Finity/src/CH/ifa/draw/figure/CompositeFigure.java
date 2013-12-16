/*
 * @(#)CompositeFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.util.ReverseFigureEnumerator;

/**
 * A Figure that is composed of several figures. A CompositeFigure
 * doesn't define any layout behavior. It is up to subclassers to
 * arrange the contained figures.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld012.htm>Composite</a></b><br>
 * CompositeFigure enables to treat a composition of figures like
 * a single figure.<br>
 * @see Figure
 */

public abstract class CompositeFigure
                extends AbstractFigure
                implements FigureChangeListener {

    /**
     * The figures that this figure is composed of
     * @see #add
     * @see #remove
     */
	// MIW: Added Figures
    protected Vector<Figure> fFigures;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 7408153435700021866L;
    // MIW Not used:
    //private int compositeFigureSerializedDataVersion = 1;

    // MIW: Added Figures
    protected CompositeFigure() {
        fFigures = new Vector<Figure>();
    }

    /**
     * Adds a figure to the list of figures. Initializes the
     * the figure's container.
     */
    public Figure add(Figure figure) {
        if (!fFigures.contains(figure)) {
            fFigures.addElement(figure);
            figure.addToContainer(this);
        }
        return figure;
    }

    /**
     * Adds a vector of figures.
     * @see #add
     */
    // MIW: Added Figure
    public void addAll(Vector<Figure> newFigures) {
        Enumeration<Figure> k = newFigures.elements();
        while (k.hasMoreElements())
            add(k.nextElement());
    }

    /**
     * Moves all the given figures by x and y. Doesn't announce
     * any changes. Subclassers override
     * basicMoveBy. Clients usually call moveBy.
     * @see moveBy
     */
    @Override
	protected void basicMoveBy(int x, int y) {
        Enumeration<Figure> k = figures();
        while (k.hasMoreElements())
            k.nextElement().moveBy(x,y);
    }

    /**
     * Brings a figure to the front.
     */
    public synchronized void bringToFront(Figure figure) {
        if (fFigures.contains(figure)) {
            fFigures.removeElement(figure);
            fFigures.addElement(figure);
            figure.changed();
        }
    }

    /**
     * Draws all the contained figures
     * @see Figure#draw
     */
    // MIW: Changed to Enumeration<Figure>
    @Override
	public void draw(Graphics g) {
        Enumeration<Figure> k = figures();
        while (k.hasMoreElements())
            k.nextElement().draw(g);
    }

    /**
     * Gets a figure at the given index.
     */
    // MIW: Removed cast (FIgure) fFigures.elementAt(i);
    public Figure figureAt(int i) {
        return fFigures.elementAt(i);
    }

    @Override
	public void figureChanged(FigureChangeEvent e) {
    }

    /**
     * Gets number of child figures.
     */
    public int figureCount() {
        return fFigures.size();
    }

    /**
     * Propagates the figureInvalidated event to my listener.
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
     * Propagates the requestUpdate request up to the container.
     * @see FigureChangeListener
     */
    @Override
	public void figureRequestUpdate(FigureChangeEvent e) {
        if (listener() != null)
            listener().figureRequestUpdate(e);
    }

    /**
     * Returns an Enumeration for accessing the contained figures.
     * The figures are returned in the drawing order.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    @Override
	public final Enumeration<Figure> figures() {
    	Enumeration<Figure> eFigs = fFigures.elements();
        return eFigs;
    }

    /**
     * Returns an Enumeration for accessing the contained figures
     * in the reverse drawing order.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public final Enumeration<Figure> figuresReverse() {
        return new ReverseFigureEnumerator(fFigures);
    }

    /**
     * Finds a top level Figure. Use this call for hit detection that
     * should not descend into the figure's children.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Figure findFigure(int x, int y) {
        Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            if (figure.containsPoint(x, y))
                return figure;
        }
        return null;
    }

    /**
     * Finds a top level Figure that intersects the given rectangle.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Figure findFigure(Rectangle r) {
        Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            Rectangle fr = figure.displayBox();
            if (r.intersects(fr))
                return figure;
        }
        return null;
    }

    /**
     * Finds a top level Figure that intersects the given rectangle.
     * It supresses the passed
     * in figure. Use this method to ignore a figure
     * that is temporarily inserted into the drawing.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Figure findFigure(Rectangle r, Figure without) {
        if (without == null)
            return findFigure(r);
        Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            Rectangle fr = figure.displayBox();
            if (r.intersects(fr) && !figure.includes(without))
                return figure;
        }
        return null;
    }

    /**
     * Finds a figure but descends into a figure's
     * children. Use this method to implement <i>click-through</i>
     * hit detection, that is, you want to detect the inner most
     * figure containing the given point.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    @Override
	public Figure findFigureInside(int x, int y) {
        Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement().findFigureInside(x, y);
            if (figure != null)
                return figure;
        }
        return null;
    }

    /**
     * Finds a figure but descends into a figure's
     * children. It supresses the passed
     * in figure. Use this method to ignore a figure
     * that is temporarily inserted into the drawing.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Figure findFigureInsideWithout(int x, int y, Figure without) {
       Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            if (figure != without) {
                Figure found = figure.findFigureInside(x, y);
                if (found != null)
                    return found;
            }
        }
        return null;
    }

    /**
     * Finds a top level Figure, but supresses the passed
     * in figure. Use this method to ignore a figure
     * that is temporarily inserted into the drawing.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param without the figure to be ignored during
     * the find.
     */
    // MIW: Replaced FigureEnumeration by Enumeration<Figure>
    public Figure findFigureWithout(int x, int y, Figure without) {
        if (without == null)
            return findFigure(x, y);
        Enumeration<Figure> k = figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            if (figure.containsPoint(x, y) && !figure.includes(without))
                return figure;
        }
        return null;
    }

    /**
     * Checks if the composite figure has the argument as one of
     * its children.
     */
    @Override
	public boolean includes(Figure figure) {
        if (super.includes(figure))
            return true;

        Enumeration<Figure> k = figures();
        while (k.hasMoreElements()) {
            Figure f = k.nextElement();
            if (f.includes(figure))
                return true;
        }
        return false;
    }

    /**
     * Removes a figure from the figure list, but
     * doesn't release it. Use this method to temporarily
     * manipulate a figure outside of the drawing.
     */
    public synchronized Figure orphan(Figure figure) {
        fFigures.removeElement(figure);
        return figure;
    }

    /**
     * Removes a vector of figures from the figure's list
     * without releasing the figures.
     * @see orphan
     */
    public void orphanAll(Vector<Figure> newFigures) {
        Enumeration<Figure> k = newFigures.elements();
        while (k.hasMoreElements())
            orphan(k.nextElement());
    }

    /**
     * Reads the contained figures from StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        int size = dr.readInt();
        fFigures = new Vector<Figure>(size);
        for (int i=0; i<size; i++)
            add((Figure)dr.readStorable());
    }

    private void readObject(ObjectInputStream s)
        throws ClassNotFoundException, IOException {

        s.defaultReadObject();

        Enumeration<Figure> k = figures();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            figure.addToContainer(this);
        }
    }

    /**
     * Releases the figure and all its children.
     */
    @Override
	public void release() {
        super.release();
        Enumeration<Figure> k = figures();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            figure.release();
        }
    }

    /**
     * Removes a figure from the composite.
     * @see #removeAll
     */
    public Figure remove(Figure figure) {
        if (fFigures.contains(figure)) {
            figure.removeFromContainer(this);
            fFigures.removeElement(figure);
        }
        return figure;
    }

    /**
     * Removes all children.
     * @see #remove
     */
    // MIW: Removed FigureEnumeration and replaced by Enumeration<Figure>
    public void removeAll() {
    	Enumeration<Figure> k = figures();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            figure.removeFromContainer(this);
        }
        fFigures.removeAllElements();
    }

    /**
     * Removes a vector of figures.
     * @see #remove
     */
    // MIW: Added Figure
    public void removeAll(Vector<Figure> figures) {
        Enumeration<Figure> k = figures.elements();
        while (k.hasMoreElements())
            remove(k.nextElement());
    }

    /**
     * Replaces a figure in the drawing without
     * removing it from the drawing.
     */
    public synchronized void replace(Figure figure, Figure replacement) {
        int index = fFigures.indexOf(figure);
        if (index != -1) {
            replacement.addToContainer(this);   // will invalidate figure
            figure.changed();
            fFigures.setElementAt(replacement, index);
        }
    }

    /**
     * Sends a figure to the back of the drawing.
     */
    public synchronized void sendToBack(Figure figure) {
        if (fFigures.contains(figure)) {
            fFigures.removeElement(figure);
            fFigures.insertElementAt(figure,0);
            figure.changed();
        }
    }

    /**
     * Writes the contained figures to the StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fFigures.size());
        Enumeration<Figure> k = fFigures.elements();
        while (k.hasMoreElements())
            dw.writeStorable(k.nextElement());
    }
}
