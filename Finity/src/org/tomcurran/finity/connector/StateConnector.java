package org.tomcurran.finity.connector;

import java.awt.Point;
import java.awt.Rectangle;

import CH.ifa.draw.connector.AbstractConnector;
import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.Geom;

public class StateConnector extends AbstractConnector {

	private static final long serialVersionUID = 7851164814974238373L;

	private static final double ANGLE_DELTA = Math.toRadians(10);

    public StateConnector() { // only used for Storable implementation
    }

    public StateConnector(Figure owner) {
        super(owner);
    }

    protected Point chop(Figure target, Point from, double angleDelta) {
    	Rectangle r = target.displayBox();
        double angle = Geom.pointToAngle(r, from);
	    return Geom.ovalAngleToPoint(r, angle + angleDelta);
    }
   
    protected Point chopStart(Figure target, Point from) {
    	return chop(target, from, ANGLE_DELTA);
    }

    protected Point chopEnd(Figure target, Point from) {
    	return chop(target, from, -ANGLE_DELTA);
    }

    @Override
	public Point findEnd(ConnectionFigure connection) {
        Figure endFigure = connection.end().owner();
        Rectangle r1 = connection.start().displayBox();
        Point r1c = null;

        if (connection.pointCount() == 2)
            r1c = new Point(r1.x + r1.width/2, r1.y + r1.height/2);
         else
            r1c = connection.pointAt(connection.pointCount()-2);

        return chopEnd(endFigure, r1c);
    }

    @Override
	public Point findStart(ConnectionFigure connection) {
        Figure startFigure = connection.start().owner();
        Rectangle r2 = connection.end().displayBox();
        Point r2c = null;

        if (connection.pointCount() == 2)
            r2c = new Point(r2.x + r2.width/2, r2.y + r2.height/2);
         else
            r2c = connection.pointAt(1);

        return chopStart(startFigure, r2c);
    }

}
