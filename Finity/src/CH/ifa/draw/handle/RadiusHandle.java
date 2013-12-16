/*
 * @(#)RadiusHandle.java 5.1
 *
 */

package CH.ifa.draw.handle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import CH.ifa.draw.figure.RoundRectangleFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.util.Geom;

/**
 * A Handle to manipulate the radius of a round corner rectangle.
 */
public class RadiusHandle extends AbstractHandle {

    private Point fRadius;
    private RoundRectangleFigure fOwner;
    private static final int OFFSET = 4;

    public RadiusHandle(RoundRectangleFigure owner) {
        super(owner);
        fOwner = owner;
    }

    @Override
	public void draw(Graphics g) {
        Rectangle r = displayBox();

        g.setColor(Color.yellow);
        g.fillOval(r.x, r.y, r.width, r.height);

        g.setColor(Color.black);
        g.drawOval(r.x, r.y, r.width, r.height);
    }

    @Override
	public void invokeStart(int  x, int  y, DrawingView view) {
        fRadius = fOwner.getArc();
        fRadius.x = fRadius.x/2;
        fRadius.y = fRadius.y/2;
    }

    @Override
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
        int dx = x-anchorX;
        int dy = y-anchorY;
        Rectangle r = fOwner.displayBox();
        int rx = Geom.range(0, r.width, 2*(fRadius.x + dx));
        int ry = Geom.range(0, r.height, 2*(fRadius.y + dy));
        fOwner.setArc(rx, ry);
    }

    @Override
	public Point locate() {
        Point radius = fOwner.getArc();
        Rectangle r = fOwner.displayBox();
        // MIW: Trying to address bug of RadiusHandle appearing outside DisplayBox. 
        // Limit coords to top left quadrant of DisplayBox. 
        // MIW: removed Point p = new Point(r.x+radius.x/2+OFFSET, r.y+radius.y/2+OFFSET);
        int rx = Math.min(r.x+radius.x/2+OFFSET, r.x + (r.width/2));
        int ry = Math.min(r.y+radius.y/2+OFFSET, r.y+(r.height/2));
        Point p = new Point(rx, ry); 
        return p;
    }
}

