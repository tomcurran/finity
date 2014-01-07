package org.tomcurran.finity;

import javax.swing.JPanel;

import org.tomcurran.finity.figure.FiniteTransitionFigure;
import org.tomcurran.finity.figure.StartMarkerFigure;
import org.tomcurran.finity.figure.connection.StartStateConnection;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.tool.AcceptStateTool;
import org.tomcurran.finity.tool.FiniteStateCreationTool;
import org.tomcurran.finity.tool.FiniteStateLabelTool;
import org.tomcurran.finity.tool.FiniteStateSelectionTool;
import org.tomcurran.finity.tool.FiniteTransitionLabelTool;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.ElbowConnection;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;

public class Finity extends DrawApplication {

	private static final long serialVersionUID = 4056934322593307168L;

	public Finity() {
		super("Finity");
		FiniteStateMachine.getInstance().setAlphabet("01");
	}

	@Override
	protected void createTools(JPanel palette) {
		super.createTools(palette);

		Tool tool = new CreationTool(view(), new EllipseFigure());
		palette.add(createToolButton(IMAGES + "ELLIPSE", "Ellipse Tool", tool));

		tool = new TextTool(view(), new TextFigure());
		palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		tool = new ConnectionTool(view(), new LineConnection());
		palette.add(createToolButton(IMAGES + "CONN", "Connection Tool", tool));

		tool = new ConnectionTool(view(), new ElbowConnection());
		palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool", tool));

		tool = new CreationTool(view(), new StartMarkerFigure());
		palette.add(createToolButton(IMAGES + "ELLIPSE", "Start Marker Tool", tool));

		tool = new FiniteStateCreationTool(view());
		palette.add(createToolButton(IMAGES + "RRECT", "Finite State Tool", tool));

		tool = new ConnectionTool(view(), new FiniteTransitionFigure());
		palette.add(createToolButton(IMAGES + "CONN", "Finite Transition Tool", tool));

		tool = new ConnectionTool(view(), new StartStateConnection(view()));
		palette.add(createToolButton(IMAGES + "OCONN", "Start Connection Tool", tool));

		tool = new FiniteStateLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT", "Finite State Label Tool", tool));

		tool = new FiniteTransitionLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT", "Finite Transition Label Tool", tool));

		tool = new AcceptStateTool(view());
		palette.add(createToolButton(IMAGES + "BORDDEC", "Accepting State Tool", tool));
	}

	@Override
	protected Tool createSelectionTool() {
		return new FiniteStateSelectionTool(view());
	}

	public static void main(String[] args) {
		Finity window = new Finity();
		window.open();
	}

}
