/*
 * @(#)LocatorConnector.java 5.1
 *
 */

package CH.ifa.draw.connector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * A LocatorConnector locates connection points with
 * the help of a Locator. It supports the definition
 * of connection points to semantic locations.
 * @see Locator
 * @see Connector
 */
public class LocatorConnector extends AbstractConnector {

    /**
     * The standard size of the connector. The display box
     * is centered around the located point.
     */
    public static final int SIZE = 8;

    private Locator  fLocator;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 5062833203337604181L;
    @SuppressWarnings("unused")
	private int locatorConnectorSerializedDataVersion = 1;

    public LocatorConnector() { // only used for Storable
        fLocator = null;
    }

    public LocatorConnector(Figure owner, Locator l) {
        super(owner);
        fLocator = l;
    }

    /**
     * Tests if a point is contained in the connector.
     */
    @Override
	public boolean containsPoint(int x, int y) {
        return displayBox().contains(x, y);
    }

    /**
     * Gets the display box of the connector.
     */
    @Override
	public Rectangle displayBox() {
        Point p = fLocator.locate(owner());
        return new Rectangle(
                p.x - SIZE / 2,
                p.y - SIZE / 2,
                SIZE,
                SIZE);
    }

    /**
     * Draws this connector.
     */
    @Override
	public void draw(Graphics g) {
        Rectangle r = displayBox();

        g.setColor(Color.blue);
        g.fillOval(r.x, r.y, r.width, r.height);
        g.setColor(Color.black);
        g.drawOval(r.x, r.y, r.width, r.height);
    }

    protected Point locate(ConnectionFigure connection) {
        return fLocator.locate(owner());
    }

    /**
     * Reads the arrow tip from a StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        fLocator = (Locator)dr.readStorable();
    }

    /**
     * Stores the arrow tip to a StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeStorable(fLocator);
    }

}

