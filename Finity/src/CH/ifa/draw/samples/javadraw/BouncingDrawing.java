/*
 * @(#)BouncingDrawing.java 5.1
 *
 */

package CH.ifa.draw.samples.javadraw;

import java.util.Enumeration;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.StandardDrawing;
import CH.ifa.draw.util.Animatable;


public class BouncingDrawing extends StandardDrawing implements Animatable {
    /*
     * Serialization support.
     */
    private static final long serialVersionUID = -8566272817418441758L;
    @SuppressWarnings("unused")
	private int bouncingDrawingSerializedDataVersion = 1;

    @Override
	public synchronized Figure add(Figure figure) {
        if (!(figure instanceof AnimationDecorator))
            figure = new AnimationDecorator(figure);
        return super.add(figure);
    }

    @Override
	public void animationStep() {
        Enumeration<Figure> k = figures();
        while (k.hasMoreElements())
            ((AnimationDecorator) k.nextElement()).animationStep();
    }

    @Override
	public synchronized Figure remove(Figure figure) {
        Figure f = super.remove(figure);
        if (f instanceof AnimationDecorator)
            return ((AnimationDecorator) f).peelDecoration();
        return f;
    }

    @Override
	public synchronized void replace(Figure figure, Figure replacement) {
        if (!(replacement instanceof AnimationDecorator))
            replacement = new AnimationDecorator(replacement);
        super.replace(figure, replacement);
    }
}
