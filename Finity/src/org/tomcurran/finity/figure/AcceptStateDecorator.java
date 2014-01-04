package org.tomcurran.finity.figure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import CH.ifa.draw.figure.DecoratorFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;

public class AcceptStateDecorator extends DecoratorFigure {

	private static final long serialVersionUID = -3088154268824811771L;

	private static final int XMARGIN = 4;
	private static final int YMARGIN = 4;

	private Color colour;

	public AcceptStateDecorator(Figure figure, Color colour) {
		super(figure);
		this.colour = colour;
	}

	@Override
	public Rectangle displayBox() {
		Rectangle r = fComponent.displayBox();
		r.grow(XMARGIN, YMARGIN);
		return r;
	}

	@Override
	public void draw(Graphics g) {
		Rectangle r = displayBox();
		super.draw(g);
		g.setColor(colour);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	@Override
	public void figureInvalidated(FigureChangeEvent e) {
		Rectangle rect = e.getInvalidatedRectangle();
		rect.grow(XMARGIN, YMARGIN);
		super.figureInvalidated(new FigureChangeEvent(e.getFigure(), rect));
	}

}
