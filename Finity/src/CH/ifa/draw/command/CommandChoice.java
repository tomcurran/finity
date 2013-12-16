/*
 * @(#)CommandChoice.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;


/**
 * A Command enabled choice. Selecting a choice executes the
 * corresponding command.
 *
 * @see Command
 */


public  class CommandChoice
        extends Choice implements ItemListener {

	private static final long serialVersionUID = -6126111738760505770L;
	private Vector<Command>   fCommands;

    public CommandChoice() {
        fCommands = new Vector<Command>(10);
        addItemListener(this);
    }

    /**
     * Adds a command to the menu.
     */
    public synchronized void addItem(Command command) {
        addItem(command.name());
        fCommands.addElement(command);
    }

    /**
     * Executes the command.
     */
    @Override
	public void itemStateChanged(ItemEvent e) {
        Command command = fCommands.elementAt(getSelectedIndex());
        command.execute();
    }
}


