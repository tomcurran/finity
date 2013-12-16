/*
 * @(#)UngroupCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.util.Enumeration;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * Command to ungroup the selected figures.
 * @see GroupCommand
 */
public  class UngroupCommand extends Command {

    private DrawingView fView;

   /**
    * Constructs a group command.
    * @param name the command name
    * @param view the target view
    */
    public UngroupCommand(String name, DrawingView view) {
        super(name);
        fView = view;
    }
    
    // MIW: FigureEnumeration removed
    @Override
	public void execute() {
        Enumeration<Figure> selection = fView.selectionElements();
        fView.clearSelection();

 //		MIW: Not used:       Vector parts = new Vector();
        while (selection.hasMoreElements()) {
            Figure selected = selection.nextElement();
            Figure group = fView.drawing().orphan(selected);
            Enumeration<Figure> k = group.decompose();
            while (k.hasMoreElements())
                fView.addToSelection(fView.add(k.nextElement()));
        }
        fView.checkDamage();
    }

    @Override
	public boolean isExecutable() {
        return fView.selectionCount() > 0;
    }

}
