package org.tomcurran.finity.figure.connection;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.QuadCurve2D;

import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection {

	private static final long serialVersionUID = -3487562166427672499L;
	private static final int CONTROL_POINT_DISTANCE = 40;

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
		Point end = new Point(x, y);
		Point start = startPoint();
		Point middle = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);
		Point perpend = new Point(end.y - start.y, -(end.x - start.x));
		double perpendLen = Math.sqrt((perpend.x * perpend.x) + (perpend.y * perpend.y));
		Point control = perpendLen == 0 ? middle : new Point(
						(int) (middle.x + ((perpend.x / perpendLen) * CONTROL_POINT_DISTANCE)),
						(int) (middle.y + ((perpend.y / perpendLen) * CONTROL_POINT_DISTANCE)));
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
		Graphics2D g2 = (Graphics2D) g;
		QuadCurve2D q = new QuadCurve2D.Float();
		q.setCurve(startPoint(), controlPoint(), endPoint());
		g2.draw(q);
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
