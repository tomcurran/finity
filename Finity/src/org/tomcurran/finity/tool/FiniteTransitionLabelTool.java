package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.FiniteTransitionFigure;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.TextHolder;

public class FiniteTransitionLabelTool extends LabelTextTool {

	private FiniteTransitionFigure transitionFigure;

	public FiniteTransitionLabelTool(DrawingView view) {
		super(view);
	}

	@Override
	public boolean isTargetFigure(Figure pressedFigure) {
		if (pressedFigure instanceof FiniteTransitionFigure) {
			transitionFigure = (FiniteTransitionFigure) pressedFigure;
			return true;
		}
		return false;
	}

	@Override
	public TextHolder getTextHolder() {
		return transitionFigure.getLabelFigure();
	}

	@Override
	public void processText(String text) {
		if (text == null || text.isEmpty()) {
			return;
		}
		transitionFigure.setLabel(text.charAt(0));
	}

}
