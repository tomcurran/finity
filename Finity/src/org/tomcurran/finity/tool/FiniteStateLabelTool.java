package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.FiniteStateFigure;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.TextHolder;

public class FiniteStateLabelTool extends LabelTextTool {

	private FiniteStateFigure stateFigure;

	public FiniteStateLabelTool(DrawingView view) {
		super(view);
	}

	@Override
	public boolean isTargetFigure(Figure pressedFigure) {
		if (pressedFigure instanceof DecoratorFigure) {
			pressedFigure = ((DecoratorFigure) pressedFigure).peelDecoration();
		}
		if (pressedFigure instanceof FiniteStateFigure) {
			stateFigure = (FiniteStateFigure) pressedFigure;
			return true;
		}
		return false;
	}

	@Override
	public TextHolder getTextHolder() {
		return stateFigure.getLabelFigure();
	}

	@Override
	public void processText(String text) {
		if (text == null || text.isEmpty()) {
			return;
		}
		if (text.length() > 4) {
			text = text.substring(0, 4);
		}
		stateFigure.setLabel(text);
	}

}
