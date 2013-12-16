/*
 * @(#)AnimationDecorator.java 5.1
 *
 */

package CH.ifa.draw.samples.javadraw;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;


public class AnimationDecorator extends DecoratorFigure {

    private int fXVelocity;
    private int fYVelocity;

    private static final long serialVersionUID = 7894632974364110685L;
    @SuppressWarnings("unused")
	private int animationDecoratorSerializedDataVersion = 1;

    public AnimationDecorator() { }

    public AnimationDecorator(Figure figure) {
        super(figure);
        fXVelocity = 4;
        fYVelocity = 4;
    }

    public void animationStep() {
	    int xSpeed = fXVelocity;
	    int ySpeed = fYVelocity;
	    Rectangle box = displayBox();

	    // MIW - here's the coords for the animation subset of the drawing - weak that it is so localised
	    // Set to same size as DrawingView
	    if ((box.x + box.width > 430) && (xSpeed > 0))
    		xSpeed = -xSpeed;

	    if ((box.y + box.height > 406) && (ySpeed > 0))
    		ySpeed = -ySpeed;

        if ((box.x < 0) && (xSpeed < 0))
            xSpeed = -xSpeed;

        if ((box.y < 0) && (ySpeed < 0))
            ySpeed = -ySpeed;

	    velocity(xSpeed, ySpeed);
	    moveBy(xSpeed, ySpeed);
	}

    @Override
	public synchronized void basicDisplayBox(Point origin, Point corner) {
        super.basicDisplayBox(origin, corner);
    }

    @Override
	public synchronized void basicMoveBy(int x, int y) {
	    super.basicMoveBy(x, y);
	}

	// guard concurrent access to display box

	@Override
	public synchronized Rectangle displayBox() {
        return super.displayBox();
    }

    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        fXVelocity = dr.readInt();
        fYVelocity = dr.readInt();
    }

    public Point velocity() {
        return new Point(fXVelocity, fYVelocity);
    }

    //-- store / load ----------------------------------------------

    public void velocity(int xVelocity, int yVelocity) {
        fXVelocity = xVelocity;
        fYVelocity = yVelocity;
    }

    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fXVelocity);
        dw.writeInt(fYVelocity);
    }

}
