package org.tomcurran.finity;

import java.io.File;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.tomcurran.finity.figure.FiniteTransitionFigure;
import org.tomcurran.finity.figure.StartMarkerFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.tool.AcceptStateTool;
import org.tomcurran.finity.tool.FiniteStateCreationTool;
import org.tomcurran.finity.tool.FiniteStateLabelTool;
import org.tomcurran.finity.tool.FiniteStateSelectionTool;
import org.tomcurran.finity.tool.FiniteTransitionLabelTool;
import org.tomcurran.finity.tool.SelfConnectionTool;
import org.tomcurran.finity.tool.StartConnectionTool;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.CreationTool;

public class Finity extends DrawApplication {

	private static final long serialVersionUID = 4056934322593307168L;
	private static final String FINITE_IMAGES = File.separator
			+ Finity.class.getPackage().getName().replace('.', File.separatorChar)
			+ File.separator + "images" + File.separator;

	private FSMSimulator simulator;

	public Finity() {
		super("Finity");
		FiniteStateMachine.getInstance().setAlphabet("10");
	}

	@Override
	protected void createTools(JPanel palette) {
		super.createTools(palette);

		Tool tool = new CreationTool(view(), new StartMarkerFigure());
		palette.add(createToolButton(FINITE_IMAGES + "START", "Start Marker Tool", tool));

		tool = new StartConnectionTool(view());
		palette.add(createToolButton(FINITE_IMAGES + "STARTCONN", "Start Connection Tool", tool));

		tool = new FiniteStateCreationTool(view());
		palette.add(createToolButton(FINITE_IMAGES + "STATE", "Finite State Tool", tool));

		tool = new SelfConnectionTool(view(), new FiniteTransitionFigure());
		palette.add(createToolButton(FINITE_IMAGES + "TRANSITION", "Finite Transition Tool", tool));

		tool = new FiniteStateLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT", "Finite State Label Tool", tool));

		tool = new FiniteTransitionLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT", "Finite Transition Label Tool", tool));

		tool = new AcceptStateTool(view());
		palette.add(createToolButton(FINITE_IMAGES + "ACCEPTSTATE", "Accepting State Tool", tool));
	}

	@Override
	protected Tool createSelectionTool() {
		return new FiniteStateSelectionTool(view());
	}

	@Override
	protected void createMenus(JMenuBar mb) {
		mb.add(createFileMenu());		
		mb.add(createEditMenu());
		mb.add(createAlignmentMenu());
		simulator = new FSMSimulator(view());
		mb.add(simulator.createMenu());
	}

	public static void main(String[] args) {
		Finity window = new Finity();
		window.open();
	}

}
