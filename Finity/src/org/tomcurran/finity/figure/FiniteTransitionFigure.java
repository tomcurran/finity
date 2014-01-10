package org.tomcurran.finity.figure;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import org.tomcurran.finity.figure.connection.FiniteTransitionConnection;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransition;

import CH.ifa.draw.figure.GroupFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.Handle;

public class FiniteTransitionFigure extends GroupFigure implements ConnectionFigure, FiniteTransition {

	private static final long serialVersionUID = -3656819892973184317L;

	private FiniteTransitionConnection connection;
	private TextFigure labelFigure;

	public FiniteTransitionFigure() {
		connection = new FiniteTransitionConnection(this);
		labelFigure = new TextFigure();
		labelFigure.setAttribute("FontName", Font.SANS_SERIF);
		labelFigure.setAttribute("FontStyle", Font.BOLD);
		labelFigure.setAttribute("FontSize", 16);
		String alphabet = FiniteStateMachine.getInstance().getAlphabet();
		if (alphabet.length() > 1) {
			setLabel(alphabet.charAt(0));
		}
		add(connection);
		add(labelFigure);
		connection.addFigureChangeListener(this);
	}

	@Override
	public void figureChanged(FigureChangeEvent e) {
		if (e.getFigure() == connection) {
			positionLabel();
		} else {
			super.figureChanged(e);
		}
	}

	private void positionLabel() {
		Point controlPoint = connection.controlPoint();
		if (controlPoint != null) {
			Rectangle labelDisplayBox = labelFigure.displayBox();
			final int halfLabelWidth = labelDisplayBox.width / 2;
			final int halfLabelHeight = labelDisplayBox.height / 2;
			labelFigure.displayBox(
					new Point(controlPoint.x - halfLabelWidth, controlPoint.y - halfLabelHeight),
					new Point(controlPoint.x + halfLabelWidth, controlPoint.y + halfLabelHeight));
		}
	}

	public TextFigure getLabelFigure() {
		return labelFigure;
	}

	@Override
	public boolean canConnect() {
		return false;
	}


	// FiniteTransition

	@Override
	public char getLabel() {
		return labelFigure.getText().charAt(0);
	}

	@Override
	public void setLabel(char label) {
		this.labelFigure.setText(String.valueOf(label));
	}


	// adapts line connection

	@Override
	public Vector<Handle> handles() {
		return connection.handles();
	}

	@Override
	public boolean canConnect(Figure start, Figure end) {
		return connection.canConnect(start, end);
	}

	@Override
	public void connectEnd(Connector end) {
		connection.connectEnd(end);
	}

	@Override
	public boolean connectsSame(ConnectionFigure other) {
		return connection.connectsSame(other);
	}

	@Override
	public void connectStart(Connector start) {
		connection.connectStart(start);
	}

	@Override
	public void disconnectEnd() {
		connection.disconnectEnd();
	}

	@Override
	public void disconnectStart() {
		connection.disconnectStart();
	}

	@Override
	public Connector end() {
		return connection.end();
	}

	@Override
	public Point endPoint() {
		return connection.endPoint();
	}

	@Override
	public void endPoint(int x, int y) {
		connection.endPoint(x, y);
	}

	@Override
	public boolean joinSegments(int x, int y) {
		return connection.joinSegments(x, y);
	}

	@Override
	public Point pointAt(int index) {
		return connection.pointAt(index);
	}

	@Override
	public int pointCount() {
		return connection.pointCount();
	}

	@Override
	public void setPointAt(Point p, int index) {
		connection.setPointAt(p, index);
	}

	@Override
	public int splitSegment(int x, int y) {
		return connection.splitSegment(x, y);
	}

	@Override
	public Connector start() {
		return connection.start();
	}

	@Override
	public Point startPoint() {
		return connection.startPoint();
	}

	@Override
	public void startPoint(int x, int y) {
		connection.startPoint(x, y);
	}

	@Override
	public void updateConnection() {
		connection.updateConnection();
	}

}
