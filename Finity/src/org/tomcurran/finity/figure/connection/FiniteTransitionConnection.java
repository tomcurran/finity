package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection {

	private static final long serialVersionUID = -3487562166427672499L;

	private ArcConnect arcConnect;

	private FiniteTransition transition;

	public FiniteTransitionConnection(FiniteTransition transition) {
		this.transition = transition;
		setStartDecoration(null);
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		if (start instanceof DecoratorFigure) {
			start = ((DecoratorFigure) start).peelDecoration();
		}
		if (end instanceof DecoratorFigure) {
			end = ((DecoratorFigure) end).peelDecoration();
		}
		return (start instanceof FiniteState) && (end instanceof FiniteState);
	}

	@Override
	protected void handleConnect(Figure start, Figure end) {
		FiniteStateMachine.getInstance().addTransition((FiniteState)start, (FiniteState)end, transition);
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		FiniteStateMachine.getInstance().removeTransition(transition);
	}

	@Override
	public int findSegment(int x, int y) {
		// prevents line becoming polyline
		return -1;
	}

	@Override
	public Rectangle displayBox() {
		// prevent Enumeration error when adding connection to group
		if (!points().hasMoreElements()) {
			return new Rectangle();
		}
		return super.displayBox();
	}

	public Point controlPoint() {
		if (fPoints.size() < 2) {
			return null;
		}
		Point p = fPoints.get(1);
		return new Point(p.x, p.y);
	}

	@Override
	public void endPoint(int x, int y) {
		willChange();
		Figure startFigure = startFigure();
		Figure endFigure = endFigure();
		if (startFigure != null && endFigure != null && startFigure == endFigure) {
			if (!(arcConnect instanceof SelfArConnect)) {
				arcConnect = new SelfArConnect(this);
			}
		} else {
			if (!(arcConnect instanceof StandardArcConnect)) {
				arcConnect = new StandardArcConnect(this);
			}
		}
		Point end = new Point(x, y);
		Point control = arcConnect.controlPoint();
		if (fPoints.size() < 2) {
			fPoints.addElement(control);
			fPoints.addElement(end);
		} else {
			fPoints.setElementAt(control, 1);
			fPoints.setElementAt(end, 2);
		}
		changed();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(getFrameColor());
		arcConnect.draw(g);
		decorate(g);
	}

	private void decorate(Graphics g) {
		if (fStartDecoration != null) {
			Point p1 = fPoints.elementAt(0);
			Point p2 = fPoints.elementAt(1);
			fStartDecoration.draw(g, p1.x, p1.y, p2.x, p2.y);
		}
		if (fEndDecoration != null) {
			Point p3 = fPoints.elementAt(fPoints.size() - 2);
			Point p4 = fPoints.elementAt(fPoints.size() - 1);
			fEndDecoration.draw(g, p4.x, p4.y, p3.x, p3.y);
		}
	}

}
