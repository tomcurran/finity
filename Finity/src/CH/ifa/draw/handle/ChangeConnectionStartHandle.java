/*
 * @(#)ChangeConnectionStartHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Point;

import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;

/**
 * Handle to reconnect the
 * start of a connection to another figure.
 */

public class ChangeConnectionStartHandle extends ChangeConnectionHandle {

    /**
     * Constructs the connection handle for the given start figure.
     */
    public ChangeConnectionStartHandle(Figure owner) {
        super(owner);
    }

    /**
     * Sets the start of the connection.
     */
    @Override
	protected void connect(Connector c) {
        fConnection.connectStart(c);
    }

    /**
     * Disconnects the start figure.
     */
    @Override
	protected void disconnect() {
        fConnection.disconnectStart();
    }

    /**
     * Returns the start point of the connection.
     */
    @Override
	public Point locate() {
        return fConnection.startPoint();
    }

    /**
     * Sets the start point of the connection.
     */
    @Override
	protected void setPoint(int x, int y) {
        fConnection.startPoint(x, y);
    }

    /**
     * Gets the start figure of a connection.
     */
    @Override
	protected Connector target() {
        return fConnection.start();
    }
}
