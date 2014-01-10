package org.tomcurran.finity.figure;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import org.tomcurran.finity.connector.StateConnector;
import org.tomcurran.finity.fsm.FiniteState;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.util.Geom;

import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.GroupFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;

public class FiniteStateFigure extends GroupFigure implements FiniteState, Observer {

	private static final long serialVersionUID = -2307029173581808928L;
	private static final int CIRCLE_SIZE = 50;
	private static final Color FILL_COLOR = Color.WHITE;

	private static int counter = 1;

	private TextFigure labelFigure;
	private Figure circleFigure;
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
		circleFigure = new EllipseFigure(
				new Point(0, 0),
				new Point(CIRCLE_SIZE, CIRCLE_SIZE));
		circleFigure.setAttribute("FillColor", FILL_COLOR);
		labelFigure = new TextFigure();
		labelFigure.setAttribute("FontName", Font.SANS_SERIF);
		labelFigure.setAttribute("FontStyle", Font.BOLD);
		labelFigure.setAttribute("FontSize", 16);
		labelFigure.setReadOnly(true);
		add(circleFigure);
		add(labelFigure);
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
		return new StateConnector(this);
	}

	public TextFigure getLabelFigure() {
		return labelFigure;
	}

	public String getLabel() {
		return getLabelFigure().getText();
	}

	public void setLabel(String label) {
		labelFigure.setText(label);
		labelFigure.displayBox(Geom.centreAt(circleFigure.center(), labelFigure.displayBox()));
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	public boolean isAccepting() {
		return accepting;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof FiniteStateMachine) {
			FiniteStateMachine fsm = (FiniteStateMachine) o;
			FiniteState currentState = fsm.getCurrentState();
			final Color currentColor = new Color(0x3366FF);
			Color c = ((Color)circleFigure.getAttribute("FillColor"));
			if (currentState == this && !c.equals(currentColor)) {
				circleFigure.setAttribute("FillColor", currentColor);
			} else if ((currentState == null || currentState != this) && !c.equals(FILL_COLOR)) {
				circleFigure.setAttribute("FillColor", FILL_COLOR);
			}
		}
	}

}
