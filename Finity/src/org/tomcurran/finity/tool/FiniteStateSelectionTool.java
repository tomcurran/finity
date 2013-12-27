/*
 * @(#)MySelectionTool.java 5.1
 *
 */

package org.tomcurran.finity.tool;

import java.awt.event.MouseEvent;

import org.tomcurran.finity.figure.AcceptStateDecorator;
import org.tomcurran.finity.figure.FiniteStateFigure;
import org.tomcurran.finity.figure.connection.FiniteTransitionConnection;
import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;

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
		if (f instanceof AcceptStateDecorator) {
			f = ((DecoratorFigure) f).peelDecoration();
			printFSF(f);
		} else if (f instanceof FiniteStateFigure) {
			printFSF(f);
		} else if (f instanceof FiniteTransitionConnection) {
			FiniteTransitionConnection ftFigure = (FiniteTransitionConnection) f;
			System.out.printf("FiniteTransitionConnection: %s%n", ftFigure.getModel().getLabel());
		}
	}

	private void printFSF(Figure f) {
		FiniteStateFigure fsFigure = (FiniteStateFigure) f;
		FiniteState fs = fsFigure.getModel();
		System.out.printf("FiniteStateFigure: %s (accepting=%b)%n", fs.getLabel(), fs.isAccepting());
	}

	@Override
	public void mouseDown(MouseEvent e, int x, int y) {
		if (e.getClickCount() == 2) {
			Figure figure = drawing().findFigure(e.getX(), e.getY());
			if (figure != null) {
				inspectFigure(figure);
				return;
			}
			System.out.printf("FiniteStateMachine: nodes=%d edges=%d%n", fsm.getNodeSet().size(), fsm.getEdgeSet().size());
		}
		super.mouseDown(e, x, y);
	}

}
