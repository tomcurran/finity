package org.tomcurran.finity.figure.connection;

import java.util.Enumeration;

import org.tomcurran.finity.figure.StartMarkerFigure;
import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

public class StartStateConnection extends LineConnection {

	private static final long serialVersionUID = 284491772436946094L;

	private DrawingView view;

	public StartStateConnection(DrawingView view) {
		this.view = view;
		setAttribute("ArrowMode", ARROW_TIP_END);
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		if (start instanceof DecoratorFigure) {
			start = ((DecoratorFigure) start).peelDecoration();
		}
		if (end instanceof DecoratorFigure) {
			end = ((DecoratorFigure) end).peelDecoration();
		}
		return (start instanceof StartMarkerFigure) && (end instanceof FiniteState);
	}

	@Override
	protected void handleConnect(Figure start, Figure end) {
		removeStartConnections();
		FiniteStateMachine.getInstance().setStartState((FiniteState) end);
	}

	private void removeStartConnections() {
		if (FiniteStateMachine.getInstance().getStartState() != null) {
			Enumeration<Figure> figures = view.drawing().figures();
			while (figures.hasMoreElements()) {
				Figure figure = figures.nextElement();
				if (figure instanceof StartStateConnection && figure != this) {
					view.drawing().remove(figure);
				}
			}
		}
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		FiniteStateMachine.getInstance().setStartState(null);
	}

	@Override
	public int findSegment(int x, int y) {
		// prevents line becoming polyline
		return -1;
	}


}
