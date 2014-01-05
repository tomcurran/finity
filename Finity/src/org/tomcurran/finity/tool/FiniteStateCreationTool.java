package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureChangeListener;
import CH.ifa.draw.tool.CreationTool;

public class FiniteStateCreationTool extends CreationTool implements FigureChangeListener {

	private FiniteStateMachine fsm;

	public FiniteStateCreationTool(DrawingView view, FiniteStateMachine fsm) {
		super(view);
		this.fsm = fsm;
	}

	@Override
	protected Figure createFigure() {
		FiniteStateFigure fsFigure = new FiniteStateFigure();
		fsm.addNode(fsFigure);
		fsFigure.addFigureChangeListener(this);
		return fsFigure;
	}

	@Override
	public void figureRemoved(FigureChangeEvent e) {
		Figure figure = e.getFigure();
		if (figure instanceof FiniteState) {
			fsm.removeNode((FiniteState) figure);
		}
	}

	@Override
	public void figureChanged(FigureChangeEvent e) {
	}

	@Override
	public void figureInvalidated(FigureChangeEvent e) {
	}

	@Override
	public void figureRequestRemove(FigureChangeEvent e) {
	}

	@Override
	public void figureRequestUpdate(FigureChangeEvent e) {
	}

}
