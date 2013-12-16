/*
 * @(#)RectangleFigure.java 5.1
 *
 */

package CH.ifa.draw.figure;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Vector;

import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.handle.BoxHandleKit;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;


/**
 * A rectangle figure.
 */
public class RectangleFigure extends AttributeFigure {

    private Rectangle   fDisplayBox;

    /*
     * Serialization support.
     */
    private static final long serialVersionUID = 184722075881789163L;
    @SuppressWarnings("unused")
	private int rectangleFigureSerializedDataVersion = 1;

    public RectangleFigure() {
        this(new Point(0,0), new Point(0,0));
    }

    public RectangleFigure(Point origin, Point corner) {
        basicDisplayBox(origin,corner);
    }

    @Override
	public void basicDisplayBox(Point origin, Point corner) {
        fDisplayBox = new Rectangle(origin);
        fDisplayBox.add(corner);
    }

    @Override
	protected void basicMoveBy(int x, int y) {
        fDisplayBox.translate(x,y);
    }

    @Override
	public Rectangle displayBox() {
        return new Rectangle(
            fDisplayBox.x,
            fDisplayBox.y,
            fDisplayBox.width,
            fDisplayBox.height);
    }

    // MIW - drawing in the model?
    @Override
	public void drawBackground(Graphics g) {
        Rectangle r = displayBox();
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    @Override
	public void drawFrame(Graphics g) {
        Rectangle r = displayBox();
        g.drawRect(r.x, r.y, r.width-1, r.height-1);
    }

    @Override
	public Vector<Handle> handles() {
        Vector<Handle> handles = new Vector<Handle>();
        BoxHandleKit.addHandles(this, handles);
        return handles;
    }

    //-- store / load ----------------------------------------------

    @Override
	public void read(StorableInput dr) throws IOException {
        super.read(dr);
        fDisplayBox = new Rectangle(
            dr.readInt(),
            dr.readInt(),
            dr.readInt(),
            dr.readInt());
    }

    @Override
	public void write(StorableOutput dw) {
        super.write(dw);
        dw.writeInt(fDisplayBox.x);
        dw.writeInt(fDisplayBox.y);
        dw.writeInt(fDisplayBox.width);
        dw.writeInt(fDisplayBox.height);
    }

}
