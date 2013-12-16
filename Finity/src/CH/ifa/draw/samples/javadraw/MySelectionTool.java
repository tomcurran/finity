/*
 * @(#)MySelectionTool.java 5.1
 *
 */

package CH.ifa.draw.samples.javadraw;

import java.awt.event.MouseEvent;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.tool.SelectionTool;

/**
 * A SelectionTool that interprets double clicks to inspect the clicked figure
 */

public  class MySelectionTool extends SelectionTool {

    public MySelectionTool(DrawingView view) {
        super(view);
    }

    protected void inspectFigure(Figure f) {
        System.out.println("inspect figure"+f);
    }

    /**
     * Handles mouse down events and starts the corresponding tracker.
     */
    @Override
	public void mouseDown(MouseEvent e, int x, int y) {
        if (e.getClickCount() == 2) {
            Figure figure = drawing().findFigure(e.getX(), e.getY());
            if (figure != null) {
                inspectFigure(figure);
                return;
            }
        }
        super.mouseDown(e, x, y);
    }

}
