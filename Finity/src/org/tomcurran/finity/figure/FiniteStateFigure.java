package org.tomcurran.finity.figure;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import org.tomcurran.finity.fsm.FiniteState;

import CH.ifa.draw.connector.ChopEllipseConnector;
import CH.ifa.draw.figure.GroupFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;

public class FiniteStateFigure extends GroupFigure implements FiniteState {

	private static final long serialVersionUID = -2307029173581808928L;
	private static int counter = 1;

	private TextFigure label;
	private boolean accepting;

	public FiniteStateFigure() {
		this(String.format("S%s", counter++), false);
	}

	public FiniteStateFigure(String label, boolean accepting) {
		initialise();
		setLabel(label);
		setAccepting(accepting);
	}

	private void initialise() {
		final int CIRCLE_SIZE = 40;
		final int HALF_CIRCLE_SIZE = (int) (CIRCLE_SIZE * 0.5);
		final int QUARTER_CIRCLE_SIZE = (int) (CIRCLE_SIZE * 0.25);

		Figure cicle = new CircleFigure(new Point(HALF_CIRCLE_SIZE,
				HALF_CIRCLE_SIZE), new Point(CIRCLE_SIZE, CIRCLE_SIZE));
		label = new TextFigure();
		label.moveBy(QUARTER_CIRCLE_SIZE, QUARTER_CIRCLE_SIZE);
		label.setAttribute("FontName", Font.SANS_SERIF);
		label.setAttribute("FontStyle", Font.BOLD);
		label.setAttribute("FontSize", 16);
		label.setReadOnly(true);

		add(cicle);
		add(label);
	}

	@Override
	public void basicDisplayBox(Point origin, Point corner) {
		Rectangle r = displayBox();
		moveBy((int) origin.getX() - r.x, (int) origin.getY() - r.y);
	}

	@Override
	public boolean canConnect() {
		return true;
	}

	@Override
	public Connector connectorAt(int x, int y) {
		return new ChopEllipseConnector(this);
	}

	public TextFigure getLabelFigure() {
		return label;
	}

	public String getLabel() {
		return getLabelFigure().getText();
	}

	public void setLabel(String label) {
		getLabelFigure().setText(label);
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	public boolean isAccepting() {
		return accepting;
	}

}
