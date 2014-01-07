package org.tomcurran.finity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tomcurran.finity.figure.StartMarkerFigure;
import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransitionException;
import org.tomcurran.finity.tool.AcceptStateTool;
import org.tomcurran.finity.tool.Animator;
import org.tomcurran.finity.tool.FiniteStateCreationTool;
import org.tomcurran.finity.tool.FiniteStateLabelTool;
import org.tomcurran.finity.tool.FiniteStateSelectionTool;
import org.tomcurran.finity.tool.FiniteTransitionLabelTool;
import org.tomcurran.finity.tool.FiniteTransitionTool;
import org.tomcurran.finity.tool.StartConnectionTool;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.ElbowConnection;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;
import CH.ifa.draw.util.Animatable;

public class Finity extends DrawApplication implements Animatable {

	private static final long serialVersionUID = 4056934322593307168L;
	private FiniteStateMachine fsm;
	private JMenuItem resetMenu;
	private JMenuItem pauseMenu;
	private JMenuItem startMenu;

	public Finity() {
		super("Finity");
		fsm = new FiniteStateMachine("01");
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
		palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool",
				tool));

		tool = new CreationTool(view(), new StartMarkerFigure());
		palette.add(createToolButton(IMAGES + "ELLIPSE", "Start Marker Tool",
				tool));

		tool = new FiniteStateCreationTool(view(), fsm);
		palette.add(createToolButton(IMAGES + "RRECT", "Finite State Tool",
				tool));

		tool = new FiniteTransitionTool(view(), fsm);
		palette.add(createToolButton(IMAGES + "CONN", "Finite Transition Tool",
				tool));

		tool = new StartConnectionTool(view(), fsm);
		palette.add(createToolButton(IMAGES + "OCONN", "Start Connection Tool",
				tool));

		tool = new FiniteStateLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT",
				"Finite State Label Tool", tool));

		tool = new FiniteTransitionLabelTool(view());
		palette.add(createToolButton(IMAGES + "TEXT",
				"Finite Transition Label Tool", tool));

		tool = new AcceptStateTool(view());
		palette.add(createToolButton(IMAGES + "BORDDEC",
				"Accepting State Tool", tool));
	}

	@Override
	protected Tool createSelectionTool() {
		return new FiniteStateSelectionTool(view(), fsm);
	}

	@Override
	protected void createMenus(JMenuBar mb) {
		super.createMenus(mb);
		mb.add(createTransitionMenu());
	}

	protected JMenu createTransitionMenu() {
		JMenu menu = new JMenu("Finite State Machine");
		startMenu = new JMenuItem("Start Transitioning");
		menu.add(startMenu);
		startMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				startTransitioning();
			}
		});
		pauseMenu = new JMenuItem("Pause Transitioning");
		pauseMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				pauseTransitioning();
			}
		});
		pauseMenu.setEnabled(false);
		resetMenu = new JMenuItem("Reset Machine");
		resetMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				resetMachine();
			}
		});
		resetMenu.setEnabled(false);
		menu.add(startMenu);
		menu.add(pauseMenu);
		menu.add(resetMenu);
		return menu;
	}

	private void startTransitioning() {
//		if (!fsm.isValid()) {
//			JOptionPane.showMessageDialog(
//					this,
//					"Finite State Machine is not in a valid state",
//					"Machine State Error",
//					JOptionPane.ERROR_MESSAGE);
//		} else {
			if (inputString == null) {
				String input = (String)JOptionPane.showInputDialog(
						this,
						"Input a single state",
						"Finite State Machine Transition",
						JOptionPane.OK_CANCEL_OPTION);
				if (input == null) {
					return;
				}
				inputString = input;
				inputIndex = -1;
			}
			startMenu.setEnabled(false);
			pauseMenu.setEnabled(true);
			resetMenu.setEnabled(false);
			startAnimation();
//		}
	}

	private void pauseTransitioning() {
		endAnimation();
		startMenu.setEnabled(true);
		pauseMenu.setEnabled(false);
		resetMenu.setEnabled(true);
	}

	private void resetMachine() {
		fsm.setCurrentState(null);
		resetMenu.setEnabled(false);
		startMenu.setEnabled(true);
		inputString = null;
		inputIndex = -1;
		view().checkDamage();
	}

	private String inputString;
	private int inputIndex;

	private Animator animator;

	@Override
	public void animationStep() {
		if (inputIndex == -1) {
			fsm.setCurrentState(fsm.getStartState());
			inputIndex = 0;
			return;
		}
		try {
			fsm.transition(inputString.charAt(inputIndex++));
			if (inputIndex >= inputString.length()) {
				endAnimation();
				resetMenu.setEnabled(true);
				startMenu.setEnabled(false);
				pauseMenu.setEnabled(false);
				view().checkDamage();
				if (fsm.getCurrentState().isAccepting()) {
					JOptionPane.showMessageDialog(
							this,
							"Input accepted by the Finite State Machine",
							"Finite State Machine",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(
							this,
							"Input rejected by the Finite State Machine",
							"Finite State Machine",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (FiniteTransitionException e) {
			endAnimation();
			startMenu.setEnabled(false);
			pauseMenu.setEnabled(false);
			resetMenu.setEnabled(true);
			JOptionPane.showMessageDialog(
					this,
					"Transition not possible in this Finite State Machine",
					"Transition Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void startAnimation() {
		if (this instanceof Animatable && animator == null) {
			animator = new Animator(this, view(), 1000);
			animator.start();
		}
	}

	private void endAnimation() {
		if (animator != null) {
			animator.end();
			animator = null;
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		endAnimation();
	}

	public static void main(String[] args) {
		Finity window = new Finity();
		window.open();
	}

}
