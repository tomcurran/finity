/*
 * @(#)MySelectionTool.java 5.1
 *
 */

package org.tomcurran.finity.tool;

import java.awt.event.MouseEvent;

import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.SelectionTool;

public class FiniteStateSelectionTool extends SelectionTool {

	private FiniteStateMachine fsm;

	public FiniteStateSelectionTool(DrawingView view, FiniteStateMachine fsm) {
		super(view);
		this.fsm = fsm;
	}

	protected void inspectFigure(Figure f) {
		if (f instanceof DecoratorFigure) {
			f = ((DecoratorFigure) f).peelDecoration();
		}
		if (f instanceof FiniteState) {
			FiniteState fsFigure = (FiniteState) f;
			System.out.printf("FiniteStateFigure: %s (accepting=%b)%n", fsFigure.getLabel(), fsFigure.isAccepting());
		} else if (f instanceof FiniteTransition) {
			FiniteTransition ftFigure = (FiniteTransition) f;
			System.out.printf("FiniteTransition: %s%n", ftFigure.getLabel());
		}
	}

	@Override
	public void mouseDown(MouseEvent e, int x, int y) {
		if (e.getClickCount() == 2) {
			Figure figure = drawing().findFigure(e.getX(), e.getY());
			if (figure != null) {
				inspectFigure(figure);
				return;
			}
			System.out.printf("FiniteStateMachine: states=%d transitions=%d%n", fsm.getStateCount(), fsm.getTransitionCount());
			FiniteState start = fsm.getStartState();
			if (start != null) {
				System.out.printf("\t- start label: %s%n", start.getLabel());
			}
			FiniteState current = fsm.getCurrentState();
			if (current != null) {
				System.out.printf("\t- current label: %s%n", current.getLabel());
			}
		}
		super.mouseDown(e, x, y);
	}

}
