package org.tomcurran.finity.figure.connection;

import java.awt.Rectangle;

import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection {

	private static final long serialVersionUID = -3487562166427672499L;

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
		FiniteStateMachine.getInstance().addEdge((FiniteState)start, (FiniteState)end, transition);
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		FiniteStateMachine.getInstance().removeEdge(transition);
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

}
