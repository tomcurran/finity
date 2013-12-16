/*
 * @(#)ChangeConnectionHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.Geom;

/**
 * ChangeConnectionHandle factors the common code for handles
 * that can be used to reconnect connections.
 *
 * @see ChangeConnectionEndHandle
 * @see ChangeConnectionStartHandle
 */
public abstract class ChangeConnectionHandle extends AbstractHandle {

    protected Connector         fOriginalTarget;
    protected Figure            fTarget;
    protected ConnectionFigure  fConnection;
    protected Point             fStart;

    /**
     * Initializes the change connection handle.
     */
    protected ChangeConnectionHandle(Figure owner) {
        super(owner);
        fConnection = (ConnectionFigure) owner();
        fTarget = null;
    }

    /**
     * Connect the connection with the given figure.
     */
    protected abstract void connect(Connector c);

    /**
     * Disconnects the connection.
     */
    protected abstract void disconnect();

    /**
     * Draws this handle.
     */
    @Override
	public void draw(Graphics g) {
        Rectangle r = displayBox();

        g.setColor(Color.green);
        g.fillRect(r.x, r.y, r.width, r.height);

        g.setColor(Color.black);
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    // MIW: Removed FigureEnumeration
    private Figure findConnectableFigure(int x, int y, Drawing drawing) {
        Enumeration<Figure> k = drawing.figuresReverse();
        while (k.hasMoreElements()) {
            Figure figure = k.nextElement();
            if (!figure.includes(fConnection) && figure.canConnect()) {
                if (figure.containsPoint(x, y))
                    return figure;
            }
        }
        return null;
    }

    private Connector findConnectionTarget(int x, int y, Drawing drawing) {
        Figure target = findConnectableFigure(x, y, drawing);

        if ((target != null) && target.canConnect()
             && target != fOriginalTarget
             && !target.includes(owner())
             && fConnection.canConnect(source().owner(), target)) {
                return findConnector(x, y, target);
        }
        return null;
    }

    protected Connector findConnector(int x, int y, Figure f) {
        return f.connectorAt(x, y);
    }

    /**
     * Connects the figure to the new target. If there is no
     * new target the connection reverts to its original one.
     */
    @Override
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
        Connector target = findConnectionTarget(x, y, view.drawing());
        if (target == null)
            target = fOriginalTarget;

        setPoint(x, y);
        connect(target);
        fConnection.updateConnection();
        if (fTarget != null) {
            fTarget.connectorVisibility(false);
            fTarget = null;
        }
    }

    /**
     * Disconnects the connection.
     */
    @Override
	public void invokeStart(int  x, int  y, DrawingView view) {
        fOriginalTarget = target();
        fStart = new Point(x, y);
        disconnect();
    }

 
    /**
     * Finds a new target of the connection.
     */
    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        Point p = new Point(x, y);
        Figure f = findConnectableFigure(x, y, view.drawing());
        // track the figure containing the mouse
        if (f != fTarget) {
            if (fTarget != null)
                fTarget.connectorVisibility(false);
            fTarget = f;
            if (fTarget != null)
                fTarget.connectorVisibility(true);
        }

        Connector target = findConnectionTarget(p.x, p.y, view.drawing());
        if (target != null)
            p = Geom.center(target.displayBox());
        setPoint(p.x, p.y);
    }
    
    /**
     * Sets the location of the target point.
     */
    protected abstract void setPoint(int x, int y);
    
    /**
     * Gets the side of the connection that is unaffected by
     * the change.
     */
    protected Connector source() {
        if (target() == fConnection.start())
            return fConnection.end();
        return fConnection.start();
    }
    
    /**
     * Returns the target connector of the change.
     */
    protected abstract Connector target();
}
