/*
 * @(#)NothingApp.java 5.1
 *
 */

package CH.ifa.draw.samples.nothing;

import javax.swing.JPanel;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.contrib.PolygonTool;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.LineFigure;
import CH.ifa.draw.figure.RectangleFigure;
import CH.ifa.draw.figure.RoundRectangleFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.ElbowConnection;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;

public  class NothingApp extends DrawApplication {

	private static final long serialVersionUID = -8049499005884965432L;

	public static void main(String[] args) {
		DrawApplication window = new NothingApp();
		window.open();
    }

    NothingApp() {
        super("Nothing");
    }

    //-- main -----------------------------------------------------------

	@Override
	protected void createTools(JPanel palette) {
        super.createTools(palette);

        Tool tool = new TextTool(view(), new TextFigure());
        palette.add(createToolButton(IMAGES+"TEXT", "Text Tool", tool));

        tool = new CreationTool(view(), new RectangleFigure());
        palette.add(createToolButton(IMAGES+"RECT", "Rectangle Tool", tool));

        tool = new CreationTool(view(), new RoundRectangleFigure());
        palette.add(createToolButton(IMAGES+"RRECT", "Round Rectangle Tool", tool));

        tool = new CreationTool(view(), new EllipseFigure());
        palette.add(createToolButton(IMAGES+"ELLIPSE", "Ellipse Tool", tool));

        tool = new CreationTool(view(), new LineFigure());
        palette.add(createToolButton(IMAGES+"LINE", "Line Tool", tool));

        tool = new PolygonTool(view());
        palette.add(createToolButton(IMAGES+"POLYGON", "Polygon Tool", tool));

        tool = new ConnectionTool(view(), new LineConnection());
        palette.add(createToolButton(IMAGES+"CONN", "Connection Tool", tool));

        tool = new ConnectionTool(view(), new ElbowConnection());
        palette.add(createToolButton(IMAGES+"OCONN", "Elbow Connection Tool", tool));
    }
}
