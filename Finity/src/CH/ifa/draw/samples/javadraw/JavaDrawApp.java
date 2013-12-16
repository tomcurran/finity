/*
 * @(#)JavaDrawApp.java 5.1
 *
 */

package CH.ifa.draw.samples.javadraw;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.command.CommandMenu;
import CH.ifa.draw.command.InsertImageCommand;
import CH.ifa.draw.contrib.DiamondFigure;
import CH.ifa.draw.contrib.PolygonTool;
import CH.ifa.draw.contrib.TriangleFigure;
import CH.ifa.draw.figure.EllipseFigure;
import CH.ifa.draw.figure.LineFigure;
import CH.ifa.draw.figure.RectangleFigure;
import CH.ifa.draw.figure.RoundRectangleFigure;
import CH.ifa.draw.figure.TextFigure;
import CH.ifa.draw.figure.connection.ElbowConnection;
import CH.ifa.draw.figure.connection.LineConnection;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.tool.BorderTool;
import CH.ifa.draw.tool.ConnectedTextTool;
import CH.ifa.draw.tool.ConnectionTool;
import CH.ifa.draw.tool.CreationTool;
import CH.ifa.draw.tool.ScribbleTool;
import CH.ifa.draw.tool.TextTool;
import CH.ifa.draw.util.Animatable;

public class JavaDrawApp extends DrawApplication {

	private static final long serialVersionUID = -5585887440350195071L;

	public static void main(String[] args) {
		JavaDrawApp window = new JavaDrawApp();
		window.open();
	}

	private Animator fAnimator;
	private static String fgSampleImagesPath = "CH/ifa/draw/samples/javadraw/sampleimages/";
	private static String fgSampleImagesResourcePath = "/" + fgSampleImagesPath;

	JavaDrawApp() {
		super("JHotDraw");
	}

	// -- application life cycle --------------------------------------------

	protected JMenu createAnimationMenu() {
		JMenu menu = new JMenu("Animation");
		JMenuItem mi = new JMenuItem("Start Animation");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				startAnimation();
			}
		});
		menu.add(mi);

		mi = new JMenuItem("Stop Animation");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				endAnimation();
			}
		});
		menu.add(mi);
		return menu;
	}

	// -- DrawApplication overrides -----------------------------------------

	@Override
	protected Drawing createDrawing() {
		return new BouncingDrawing();
		// return new StandardDrawing();
	}

	protected JMenu createImagesMenu() {
		CommandMenu menu = new CommandMenu("Images");
		File imagesDirectory = new File(fgSampleImagesPath);
		try {
			String[] list = imagesDirectory.list();
			for (int i = 0; i < list.length; i++) {
				String name = list[i];
				String path = fgSampleImagesResourcePath + name;
				menu.add(new InsertImageCommand(name, path, view()));
			}
		} catch (Exception e) {
		}
		return menu;
	}

	@Override
	protected void createMenus(JMenuBar mb) {
		super.createMenus(mb);
		mb.add(createAnimationMenu());
		mb.add(createImagesMenu());
		mb.add(createWindowMenu());
	}

	@Override
	protected Tool createSelectionTool() {
		return new MySelectionTool(view());
	}

	@Override
	protected void createTools(JPanel palette) {
		super.createTools(palette);

		Tool tool = new TextTool(view(), new TextFigure());
		palette.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		tool = new ConnectedTextTool(view(), new TextFigure());
		palette.add(createToolButton(IMAGES + "ATEXT", "Connected Text Tool",
				tool));

		tool = new URLTool(view());
		palette.add(createToolButton(IMAGES + "URL", "URL Tool", tool));

		tool = new CreationTool(view(), new RectangleFigure());
		palette.add(createToolButton(IMAGES + "RECT", "Rectangle Tool", tool));

		tool = new CreationTool(view(), new RoundRectangleFigure());
		palette.add(createToolButton(IMAGES + "RRECT", "Round Rectangle Tool",
				tool));

		tool = new CreationTool(view(), new EllipseFigure());
		palette.add(createToolButton(IMAGES + "ELLIPSE", "Ellipse Tool", tool));

		tool = new CreationTool(view(), new TriangleFigure());
		palette.add(createToolButton(IMAGES + "TRIANGLE", "Triangle Tool", tool));

		tool = new CreationTool(view(), new DiamondFigure());
		palette.add(createToolButton(IMAGES + "DIAMOND", "Diamond Tool", tool));

		tool = new CreationTool(view(), new LineFigure());
		palette.add(createToolButton(IMAGES + "LINE", "Line Tool", tool));

		tool = new ConnectionTool(view(), new LineConnection());
		palette.add(createToolButton(IMAGES + "CONN", "Connection Tool", tool));

		tool = new ConnectionTool(view(), new ElbowConnection());
		palette.add(createToolButton(IMAGES + "OCONN", "Elbow Connection Tool",
				tool));

		tool = new ScribbleTool(view());
		palette.add(createToolButton(IMAGES + "SCRIBBL", "Scribble Tool", tool));

		tool = new PolygonTool(view());
		palette.add(createToolButton(IMAGES + "POLYGON", "Polygon Tool", tool));

		tool = new BorderTool(view());
		palette.add(createToolButton(IMAGES + "BORDDEC", "Border Tool", tool));
	}

	protected JMenu createWindowMenu() {
		JMenu menu = new JMenu("Window");
		JMenuItem mi = new JMenuItem("New Window");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				openView();
			}
		});
		menu.add(mi);
		return menu;
	}

	@Override
	public void destroy() {
		super.destroy();
		endAnimation();
	}

	// ---- animation support --------------------------------------------

	public void endAnimation() {
		if (fAnimator != null) {
			fAnimator.end();
			fAnimator = null;
		}
	}

	@Override
	public void open() {
		super.open();
	}

	public void openView() {
		JavaDrawApp window = new JavaDrawApp();
		window.open();
		window.setDrawing(drawing());
		window.setTitle("JHotDraw (View)");

	}

	// -- main -----------------------------------------------------------

	public void startAnimation() {
		if (drawing() instanceof Animatable && fAnimator == null) {
			fAnimator = new Animator((Animatable) drawing(), view());
			fAnimator.start();
		}
	}
}
