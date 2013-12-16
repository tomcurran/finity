/*
 * @(#)SendToBackCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.FigureEnumeration;
import CH.ifa.draw.util.ReverseFigureEnumerator;

/**
 * A command to send the selection to the back of the drawing.
 */
public class SendToBackCommand extends Command {

    private DrawingView fView;

   /**
    * Constructs a send to back command.
    * @param name the command name
    * @param view the target view
    */
    public SendToBackCommand(String name, DrawingView view) {
        super(name);
        fView = view;
    }

    @Override
	public void execute() {
       FigureEnumeration k = new ReverseFigureEnumerator(fView.selectionZOrdered());
       while (k.hasMoreElements()) {
            fView.drawing().sendToBack(k.nextFigure());
        }
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}


