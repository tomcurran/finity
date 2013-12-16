/*
 * @(#)CommandButton.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


/**
 * A Command enabled button. Clicking the button executes the command.
 *
 * @see Command
 */

public  class CommandButton
        extends JButton implements ActionListener {

	private static final long serialVersionUID = -1978614103745493356L;
	private Command   fCommand;

    /**
     * Initializes the button with the given command.
     * The command's name is used as the label.
     */
    public CommandButton(Command command) {
    	super(command.name());
    	fCommand = command;
    	addActionListener(this);
    }

    /**
     * Executes the command. If the command's name was changed
     * as a result of the command the button's label is updated
     * accordingly.
     */
    @Override
	public void actionPerformed(ActionEvent e) {
        fCommand.execute();
        if (!getText().equals(fCommand.name()) ) {
            setText(fCommand.name());
        }
    }
}


