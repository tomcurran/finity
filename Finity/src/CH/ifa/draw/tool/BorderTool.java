/*
 * @(#)BorderTool.java 5.1
 *
 */

package CH.ifa.draw.tool;

import CH.ifa.draw.figure.BorderDecorator;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;

/**
 * BorderTool decorates the clicked figure with a BorderDecorator.
 *
 * @see BorderDecorator
 */
public  class BorderTool extends ActionTool {

    public BorderTool(DrawingView view) {
        super(view);
    }

    /**
    * Decorates the clicked figure with a border.
    */
    @Override
	public void action(Figure figure) {
        drawing().replace(figure, new BorderDecorator(figure));
    }
}
