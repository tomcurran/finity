/*
 * @(#)CopyCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.Clipboard;

/**
 * Copy the selection to the clipboard.
 * @see Clipboard
 */
public class CopyCommand extends FigureTransferCommand {

   /**
    * Constructs a copy command.
    * @param name the command name
    * @param view the target view
    */
    public CopyCommand(String name, DrawingView view) {
        super(name, view);
    }

    @Override
	public void execute() {
        copySelection();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}


