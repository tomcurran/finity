package org.tomcurran.finity.tool;

import java.awt.Color;

import org.tomcurran.finity.figure.AcceptStateDecorator;
import org.tomcurran.finity.figure.FiniteStateFigure;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.ActionTool;

public class AcceptStateTool extends ActionTool {

	public AcceptStateTool(DrawingView view) {
		super(view);
	}

	@Override
	public void action(Figure figure) {
		if (figure instanceof AcceptStateDecorator) {
			FiniteStateFigure fsFigure = (FiniteStateFigure) ((DecoratorFigure) figure).peelDecoration();
			drawing().replace(figure, fsFigure);
			fsFigure.setAccepting(false);
		} else if (figure instanceof FiniteStateFigure) {
			drawing().replace(figure, new AcceptStateDecorator(figure, Color.GREEN));
			FiniteStateFigure fsFigure = (FiniteStateFigure) figure;
			fsFigure.setAccepting(true);
		}
	}

}
