/*
 * @(#)DuplicateCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.util.Vector;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.FigureSelection;

/**
 * Duplicate the selection and select the duplicates.
 */
public class DuplicateCommand extends FigureTransferCommand {

   /**
    * Constructs a duplicate command.
    * @param name the command name
    * @param view the target view
    */
    public DuplicateCommand(String name, DrawingView view) {
        super(name, view);
    }

    @Override
	public void execute() {
        FigureSelection selection = fView.getFigureSelection();

        fView.clearSelection();

        @SuppressWarnings("unchecked")
		Vector<Figure> figures = (Vector<Figure>)selection.getData(FigureSelection.TYPE);
        insertFigures(figures, 10, 10);
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}


