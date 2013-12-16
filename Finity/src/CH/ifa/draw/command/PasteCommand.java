/*
 * @(#)PasteCommand.java 5.1
 *
 */

package CH.ifa.draw.command;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Vector;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.util.Clipboard;
import CH.ifa.draw.util.FigureSelection;

/**
 * Command to insert the clipboard into the drawing.
 * @see Clipboard
 */
public class PasteCommand extends FigureTransferCommand {

   /**
    * Constructs a paste command.
    * @param name the command name
    * @param image the pathname of the image
    * @param view the target view
    */
    public PasteCommand(String name, DrawingView view) {
        super(name, view);
    }

    Rectangle bounds(Enumeration<Figure> k) {
        Rectangle r = k.nextElement().displayBox();
        while (k.hasMoreElements())
            r.add(k.nextElement().displayBox());
        return r;
    }

    @Override
	public void execute() {
        Point lastClick = fView.lastClick();
        FigureSelection selection = (FigureSelection)Clipboard.getClipboard().getContents();
        if (selection != null) {
            @SuppressWarnings("unchecked")
			Vector<Figure> figures = (Vector<Figure>)selection.getData(FigureSelection.TYPE);
            if (figures.size() == 0)
                return;

            Rectangle r = bounds(figures.elements());
            fView.clearSelection();

            insertFigures(figures, lastClick.x-r.x, lastClick.y-r.y);
            fView.checkDamage();
        }
    }
}


