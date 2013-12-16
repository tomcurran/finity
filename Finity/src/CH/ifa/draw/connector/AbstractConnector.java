/*
 * @(#)AbstractConnector.java 5.1
 *
 */

package CH.ifa.draw.connector;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.util.Geom;

/**
 * AbstractConnector provides default implementation for
 * the Connector interface.
 * @see Connector
 */
public abstract class AbstractConnector implements Connector {
    /**
     * the owner of the connector
     */
    private Figure      fOwner;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -5170007865562687545L;
    @SuppressWarnings("unused")
	private int abstractConnectorSerializedDataVersion = 1;

    /**
     * Constructs a connector that has no owner. It is only
     * used internally to resurrect a connectors from a
     * StorableOutput. It should never be called directly.
     */
    public AbstractConnector() {
        fOwner = null;
    }

    /**
     * Constructs a connector with the given owner figure.
     */
    public AbstractConnector(Figure owner) {
        fOwner = owner;
    }

    /**
     * Tests if a point is contained in the connector.
     */
    @Override
	public boolean containsPoint(int x, int y) {
        return owner().containsPoint(x, y);
    }

    /**
     * Gets the display box of the connector.
     */
    @Override
	public Rectangle displayBox() {
        return owner().displayBox();
    }

    /**
     * Draws this connector. By default connectors are invisible.
     */
    @Override
	public void draw(Graphics g) {
        // invisible by default
    }

    @Override
	public Point findEnd(ConnectionFigure connection) {
        return findPoint(connection);
    }

    /**
     * Gets the connection point. Override when the connector
     * does not need to distinguish between the start and end
     * point of a connection.
     */
    protected Point findPoint(ConnectionFigure connection) {
        return Geom.center(displayBox());
    }

    @Override
	public Point findStart(ConnectionFigure connection) {
        return findPoint(connection);
    }

    /**
     * Gets the connector's owner.
     */
    @Override
	public Figure owner() {
        return fOwner;
    }

    /**
     * Reads the connector and its owner from a StorableInput.
     */
    @Override
	public void read(StorableInput dr) throws IOException {
        fOwner = (Figure)dr.readStorable();
    }

    /**
     * Stores the connector and its owner to a StorableOutput.
     */
    @Override
	public void write(StorableOutput dw) {
        dw.writeStorable(fOwner);
    }

}

