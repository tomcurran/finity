package org.tomcurran.finity.tool;

import org.tomcurran.finity.figure.connection.StartStateConnection;

import CH.ifa.draw.framework.ConnectionFigure;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.tool.ConnectionTool;

public class StartConnectionTool extends ConnectionTool {

	public StartConnectionTool(DrawingView view) {
		super(view, null);
	}

	@Override
	protected ConnectionFigure createConnection() {
		return new StartStateConnection(view());
	}

}