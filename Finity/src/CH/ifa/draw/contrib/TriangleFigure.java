/*
 * Hacked together by Doug lea
 * Tue Feb 25 17:30:58 1997  Doug Lea  (dl at gee)
 *
 */

package CH.ifa.draw.contrib;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Vector;

import CH.ifa.draw.connector.ShortestDistanceConnector;
import CH.ifa.draw.figure.RectangleFigure;
import CH.ifa.draw.framework.Connector;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;

/**
 * A triangle with same dimensions as its enclosing rectangle,
 * and apex at any of 8 places
 */
public  class TriangleFigure extends RectangleFigure {

	private static final long serialVersionUID = -358681993869799745L;

static double[] rotations = {
    -Math.PI/2, -Math.PI/4,
    0.0, Math.PI/4,
    Math.PI/2, Math.PI * 3/4,
    Math.PI,  -Math.PI * 3/4
  };

  protected int fRotation = 0;

  public TriangleFigure() {
    super(new Point(0,0), new Point(0,0));
  }

  public TriangleFigure(Point origin, Point corner) {
    super(origin, corner);
  }

  @Override
public Point center() {
    return PolygonFigure.center(polygon());
  }

  public Point chop(Point p) {
    return PolygonFigure.chop(polygon(), p);
  }

  @Override
public Object clone() {
    TriangleFigure figure = (TriangleFigure) super.clone();
    figure.fRotation = fRotation;
    return figure;
  }


  @Override
public Insets connectionInsets() {
    Rectangle r = displayBox();
    switch(fRotation) {
    case 0:
      return new Insets(r.height, r.width/2, 0, r.width/2);
    case 1:
      return new Insets(0, r.width, r.height, 0);
    case 2:
      return new Insets(r.height/2, 0, r.height/2, r.width);
    case 3:
      return new Insets(r.height, r.width, 0, 0);
    case 4:
      return new Insets(0, r.width/2, r.height, r.width/2);
    case 5:
      return new Insets(r.height, 0, 0, r.width);
    case 6:
      return new Insets(r.height/2, r.width, r.height/2, 0);
    case 7:
      return new Insets(0, 0, r.height, r.width);
    default:
      return null;
    }
  }


  // MIW: added so that connects to Figure rather than displayBox. Insets ensure this.
  @Override
public Connector connectorAt(int x, int y) {
      return new ShortestDistanceConnector(this); // just for demo purposes
  }

  @Override
public boolean containsPoint(int x, int y) {
    return polygon().contains(x, y);
  }

  @Override
public void draw(Graphics g) {
    Polygon p = polygon();
    g.setColor(getFillColor());
    g.fillPolygon(p);
    g.setColor(getFrameColor());
    g.drawPolygon(p);
  }

  @Override
public Vector<Handle> handles() {
    Vector<Handle> h = super.handles();
    h.addElement(new TriangleRotationHandle(this));
    return h;
  }
  
  /** Return the polygon describing the triangle **/
  public Polygon polygon() {
    Rectangle r = displayBox();
    Polygon p = new Polygon();
    switch (fRotation) {
    case 0:
      p.addPoint(r.x+r.width/2, r.y);
      p.addPoint(r.x+r.width, r.y+r.height);
      p.addPoint(r.x, r.y+r.height);
      break;
    case 1:
      p.addPoint(r.x + r.width, r.y);
      p.addPoint(r.x+r.width, r.y+r.height);
      p.addPoint(r.x, r.y);
      break;
    case 2:
      p.addPoint(r.x + r.width, r.y+r.height/2);
      p.addPoint(r.x, r.y+r.height);
      p.addPoint(r.x, r.y);
      break;
    case 3:
      p.addPoint(r.x+r.width, r.y+r.height);
      p.addPoint(r.x, r.y+r.height);
      p.addPoint(r.x + r.width, r.y);
      break;
    case 4:
      p.addPoint(r.x+r.width/2, r.y+r.height);
      p.addPoint(r.x, r.y);
      p.addPoint(r.x + r.width, r.y);
      break;
    case 5:
      p.addPoint(r.x, r.y+r.height);
      p.addPoint(r.x, r.y);
      p.addPoint(r.x+r.width, r.y+r.height);
      break;
    case 6:
      p.addPoint(r.x, r.y+r.height/2);
      p.addPoint(r.x + r.width, r.y);
      p.addPoint(r.x+r.width, r.y+r.height);
      break;
    case 7:
      p.addPoint(r.x, r.y);
      p.addPoint(r.x + r.width, r.y);
      p.addPoint(r.x, r.y+r.height);
      break;
    }
    return p;
  }

  @Override
public void read(StorableInput dr) throws IOException {
  super.read(dr);
  fRotation = dr.readInt();
}
  

    //-- store / load ----------------------------------------------

    public void rotate(double angle) {
	    willChange();
	    //System.out.println("a:"+angle);
	    double dist = Double.MAX_VALUE;
	    int best = 0;
	    for (int i = 0; i < rotations.length; ++i) {
	      double d = Math.abs(angle - rotations[i]);
	      if (d < dist) {
	        dist = d;
	        best = i;
	      }
	    }
	    fRotation = best;
	    changed();
	  }

    @Override
	public void write(StorableOutput dw) {
      super.write(dw);
      dw.writeInt(fRotation);
    }
}
