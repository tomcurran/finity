/*
 * @(#)CutCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.Clipboard;

/**
 * Delete the selection and move the selected figures to
 * the clipboard.
 * @see Clipboard
 */
public class CutCommand extends FigureTransferCommand {

   /**
    * Constructs a cut command.
    * @param name the command name
    * @param view the target view
    */
    public CutCommand(String name, DrawingView view) {
        super(name, view);
    }

    @Override
	public void execute() {
        copySelection();
        deleteSelection();
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}


