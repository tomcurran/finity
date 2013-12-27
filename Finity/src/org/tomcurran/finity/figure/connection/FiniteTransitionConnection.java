package org.tomcurran.finity.figure.connection;

import java.util.Observable;
import java.util.Observer;

import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Figure;

public class FiniteTransitionConnection extends LineConnection implements Observer {

	private static final long serialVersionUID = -3487562166427672499L;

	private FiniteTransition model;
	private FiniteStateMachine fsm;

	public FiniteTransitionConnection(FiniteStateMachine fsm) {
		this.fsm = fsm;
		model = new FiniteTransition();
		model.addObserver(this);
		setAttribute("ArrowMode", ARROW_TIP_END);
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		return start instanceof FiniteStateFigure
				&& end instanceof FiniteStateFigure;
	}

	@Override
	protected void handleConnect(Figure start, Figure end) {
		fsm.addEdge(((FiniteStateFigure)start).getModel(), ((FiniteStateFigure)end).getModel(), model);
	}

	@Override
	protected void handleDisconnect(Figure start, Figure end) {
		fsm.removeEdge(model);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public FiniteTransition getModel() {
		return model;
	}

}
