package org.tomcurran.finity;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.tomcurran.finity.fsm.FiniteStateMachine;
import org.tomcurran.finity.fsm.FiniteTransitionException;
import org.tomcurran.finity.tool.Animator;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.Animatable;

public class FSMSimulator implements Animatable {

	private DrawingView view;
	private JMenuItem resetMenu;
	private JMenuItem pauseMenu;
	private JMenuItem startMenu;
	private String inputString;
	private int inputIndex;
	private Animator animator;

	public FSMSimulator(DrawingView view) {
		this.view = view;
		inputString = null;
		inputIndex = -1;
	}

	public void start() {
		if (!FiniteStateMachine.getInstance().isValid()) {
			JOptionPane.showMessageDialog(
					(Component) view,
					"Finite State Machine is not in a valid state",
					"Machine State Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			if (inputString == null) {
				String input = (String)JOptionPane.showInputDialog(
						(Component) view,
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
		}
	}

	public void pause() {
		endAnimation();
		startMenu.setEnabled(true);
		pauseMenu.setEnabled(false);
		resetMenu.setEnabled(true);
	}

	public void reset() {
		FiniteStateMachine.getInstance().setCurrentState(null);
		resetMenu.setEnabled(false);
		startMenu.setEnabled(true);
		inputString = null;
		inputIndex = -1;
		view.checkDamage();
	}

	@Override
	public void animationStep() {
		FiniteStateMachine fsm = FiniteStateMachine.getInstance();
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
				view.checkDamage();
				if (fsm.getCurrentState().isAccepting()) {
					JOptionPane.showMessageDialog(
							(Component) view,
							"Input accepted by the Finite State Machine",
							"Finite State Machine",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(
							(Component) view,
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
					(Component) view,
					"Transition not possible in this Finite State Machine",
					"Transition Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void startAnimation() {
		if (this instanceof Animatable && animator == null) {
			animator = new Animator(this, view, 1000);
			animator.start();
		}
	}

	protected void endAnimation() {
		if (animator != null) {
			animator.end();
			animator = null;
		}
	}

	public JMenu createMenu() {
		JMenu menu = new JMenu("FSM Simulator");
		startMenu = new JMenuItem("Start Transitioning");
		menu.add(startMenu);
		startMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				start();
			}
		});
		pauseMenu = new JMenuItem("Pause Transitioning");
		pauseMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				pause();
			}
		});
		pauseMenu.setEnabled(false);
		resetMenu = new JMenuItem("Reset Machine");
		resetMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				reset();
			}
		});
		resetMenu.setEnabled(false);
		menu.add(startMenu);
		menu.add(pauseMenu);
		menu.add(resetMenu);
		return menu;
	}

}
