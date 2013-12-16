/*
 * @(#)BringToFrontCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.util.Enumeration;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * BringToFrontCommand brings the selected figures in the front of
 * the other figures.
 *
 * @see SendToBackCommand
 */
public class BringToFrontCommand
       extends Command {

    private DrawingView fView;

   /**
    * Constructs a bring to front command.
    * @param name the command name
    * @param view the target view
    */
    public BringToFrontCommand(String name, DrawingView view) {
        super(name);
        fView = view;
    }

    // MIW: Changed to use Emeration<Figure> rather than FigureEnumeration
    @Override
	public void execute() {
       Enumeration<Figure> k = fView.selectionZOrdered().elements();
       while (k.hasMoreElements()) {
            fView.drawing().bringToFront(k.nextElement());
        }
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }
}


