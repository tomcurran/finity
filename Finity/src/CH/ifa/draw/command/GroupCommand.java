/*
 * @(#)GroupCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.util.Vector;

import CH.ifa.draw.figure.GroupFigure;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * Command to group the selection into a GroupFigure.
 *
 * @see GroupFigure
 */
public  class GroupCommand extends Command {

    private DrawingView fView;

   /**
    * Constructs a group command.
    * @param name the command name
    * @param view the target view
    */
    public GroupCommand(String name, DrawingView view) {
        super(name);
        fView = view;
    }

    @Override
	public void execute() {
        Vector<Figure> selected = fView.selectionZOrdered();
        Drawing drawing = fView.drawing();
        if (selected.size() > 0) {
            fView.clearSelection();
            drawing.orphanAll(selected);

            GroupFigure group = new GroupFigure();
            group.addAll(selected);
            fView.addToSelection(drawing.add(group));
        }
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}


