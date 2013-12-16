/*
 * @(#)NetApp.java 5.1
 *
 */

package CH.ifa.draw.samples.net;

import javax.swing.JPanel;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.TextTool;

public  class NetApp extends DrawApplication {

    /**
	 * 
	 */
	private static final long serialVersionUID = -97233389025989950L;

	public static void main(String[] args) {
		DrawApplication window = new NetApp();
		window.open();
    }

    NetApp() {
        super("Net");
    }

    //-- main -----------------------------------------------------------

	@Override
	protected void createTools(JPanel palette) {
        super.createTools(palette);

        Tool tool = new TextTool(view(), new NodeFigure());
        palette.add(createToolButton(IMAGES+"TEXT", "Text Tool", tool));

        tool = new CreationTool(view(), new NodeFigure());
        palette.add(createToolButton(IMAGES+"RECT", "Create Org Unit", tool));

        tool = new ConnectionTool(view(), new LineConnection());
        palette.add(createToolButton(IMAGES+"CONN", "Connection Tool", tool));
    }
}
