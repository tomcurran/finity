package org.tomcurran.finity.handle;

import java.awt.Point;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.Locator;
import CH.ifa.draw.handle.LocatorHandle;
import CH.ifa.draw.util.Geom;

public class CircleResizeHandle extends LocatorHandle {

	private final Figure owner;

	public CircleResizeHandle(final Figure owner, final Locator locator) {
		super(owner, locator);
		this.owner = owner;
	}

	@Override
	public void invokeStep(final int x, final int y, final int anchorX, final int anchorY, final DrawingView view) {
		final Point centre = this.owner.center();
		double c = Geom.length(centre.x, centre.y, x, y) * 2;
		double a = Math.sqrt((c * c) / 2);
		int radius = (int) (a / 2);
		this.owner.displayBox(centre, new Point(centre.x + radius, centre.y + radius));
	}

}