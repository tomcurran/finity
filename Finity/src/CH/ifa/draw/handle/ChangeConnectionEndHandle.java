/*
 * @(#)ChangeConnectionEndHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Point;

import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;

/**
 * A handle to reconnect the end point of
 * a connection to another figure.
 */

public class ChangeConnectionEndHandle extends ChangeConnectionHandle {

    /**
     * Constructs the connection handle.
     */
    public ChangeConnectionEndHandle(Figure owner) {
        super(owner);
    }

    /**
     * Sets the end of the connection.
     */
    @Override
	protected void connect(Connector c) {
        fConnection.connectEnd(c);
    }

    /**
     * Disconnects the end figure.
     */
    @Override
	protected void disconnect() {
        fConnection.disconnectEnd();
    }

    /**
     * Returns the end point of the connection.
     */
    @Override
	public Point locate() {
        return fConnection.endPoint();
    }

    /**
     * Sets the end point of the connection.
     */
    @Override
	protected void setPoint(int x, int y) {
        fConnection.endPoint(x, y);
    }

    /**
     * Gets the end figure of a connection.
     */
    @Override
	protected Connector target() {
        return fConnection.end();
    }
}

