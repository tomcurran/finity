/*
 * @(#)FigureTransferCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.Clipboard;
import CH.ifa.draw.util.FigureSelection;

/**
 * Common base clase for commands that transfer figures
 * between a drawing and the clipboard.
 */
abstract class FigureTransferCommand extends Command {

    protected DrawingView fView;

   /**
    * Constructs a drawing command.
    * @param name the command name
    * @param view the target view
    */
    protected FigureTransferCommand(String name, DrawingView view) {
        super(name);
        fView = view;
    }

   /**
    * Copies the selection to the clipboard.
    */
    protected void copySelection() {
        FigureSelection selection = fView.getFigureSelection();
        Clipboard.getClipboard().setContents(selection);
    }

   /**
    * Deletes the selection from the drawing.
    */
    protected void deleteSelection() {
       fView.drawing().removeAll(fView.selection());
       fView.clearSelection();
    }

   /**
    * Inserts a vector of figures and translates them by the
    * given offset.
    */
    // MIW: Removed FigureEnumeration
    protected void insertFigures(Vector<Figure> figures, int dx, int dy) {
        Enumeration<Figure> e = figures.elements();
        while (e.hasMoreElements()) {
            Figure figure = e.nextElement();
            figure.moveBy(dx, dy);
            figure = fView.add(figure);
            fView.addToSelection(figure);
        }
    }

}


