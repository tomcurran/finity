/*
 * @(#)CommandMenu.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class CommandMenu extends JMenu implements ActionListener {

	private static final long serialVersionUID = -3321022590870558951L;
	private Vector<Command> fCommands;

	public CommandMenu(String name) {
		super(name);
		fCommands = new Vector<Command>(10);
	}

	/**
	 * Executes the command.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int j = 0;
		Object source = e.getSource();
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			// ignore separators
			// a separator has a hyphen as its label
			if (item == null) // MIW: Separators are null JMenuItems
				continue;
			if (source == item) {
				Command cmd = fCommands.elementAt(j);
				cmd.execute();
				break;
			}
			j++;
		}
	}

	/**
	 * Adds a command to the menu. The item's label is the command's name.
	 */
	public synchronized void add(Command command) {
		JMenuItem m = new JMenuItem(command.name());
		m.addActionListener(this);
		add(m);
		fCommands.addElement(command);
	}

	/**
	 * Adds a command with the given short cut to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void add(Command command, char shortcut) {
		JMenuItem m = new JMenuItem(command.name(), shortcut);
		m.setName(command.name());
		m.addActionListener(this);
		add(m);
		fCommands.addElement(command);
	}

	public synchronized void checkEnabled() {
		int j = 0;
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			// ignore separators
			// a separator has a hyphen as its label
			// MIW: Test for separator - null. Best I could do.
			// if (item.getText().equals("-"))
			if (item != null) { // it's a separator
				Command cmd = fCommands.elementAt(j);
				item.setEnabled(cmd.isExecutable());
				j++;
			}
		}
	}

	/**
	 * Changes the enabling/disabling state of a named menu item.
	 */
	public synchronized void enable(String name, boolean state) {
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (item != null) {
				if (name.equals(item.getText())) {
					item.setEnabled(state);
					return;
				}
			}
		}
	}

	public synchronized void remove(Command command) {
		System.out.println("not implemented");
	}

	public synchronized void remove(MenuItem item) {
		System.out.println("not implemented");
	}
}
