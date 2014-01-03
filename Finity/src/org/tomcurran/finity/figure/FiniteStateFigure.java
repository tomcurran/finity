package org.tomcurran.finity.figure;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import org.tomcurran.finity.fsm.FiniteState;

import CH.ifa.draw.connector.ChopEllipseConnector;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.GroupFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;

public class FiniteStateFigure extends GroupFigure implements FiniteState {

	private static final long serialVersionUID = -2307029173581808928L;
	private static final int CIRCLE_SIZE = 50;

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
		circleFigure.setAttribute("FillColor", Color.WHITE);
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
		return new ChopEllipseConnector(this);
	}

	public TextFigure getLabelFigure() {
		return labelFigure;
	}

	public String getLabel() {
		return getLabelFigure().getText();
	}

	public void setLabel(String label) {
		labelFigure.setText(label);
		positionLabel();
	}

	private void positionLabel() {
		Point circleCentre = circleFigure.center();
		Rectangle labelDisplayBox = labelFigure.displayBox();
		final int halfLabelWidth = labelDisplayBox.width / 2;
		final int halfLabelHeight = labelDisplayBox.height / 2;
		labelFigure.displayBox(
				new Point(circleCentre.x - halfLabelWidth, circleCentre.y - halfLabelHeight),
				new Point(circleCentre.x + halfLabelWidth, circleCentre.y + halfLabelHeight));
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	public boolean isAccepting() {
		return accepting;
	}

}
