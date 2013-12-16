/*
 * @(#)DrawApplication.java 5.1
 *
 */

package CH.ifa.draw.application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import CH.ifa.draw.command.AlignCommand;
import CH.ifa.draw.command.BringToFrontCommand;
import CH.ifa.draw.command.ChangeAttributeCommand;
import CH.ifa.draw.command.CommandMenu;
import CH.ifa.draw.command.CopyCommand;
import CH.ifa.draw.command.CutCommand;
import CH.ifa.draw.command.DeleteCommand;
import CH.ifa.draw.command.DuplicateCommand;
import CH.ifa.draw.command.GroupCommand;
import CH.ifa.draw.command.PasteCommand;
import CH.ifa.draw.command.SendToBackCommand;
import CH.ifa.draw.command.ToggleGridCommand;
import CH.ifa.draw.command.UngroupCommand;
import CH.ifa.draw.figure.PolyLineFigure;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.painter.BufferedUpdateStrategy;
import CH.ifa.draw.painter.SimpleUpdateStrategy;
import CH.ifa.draw.palette.PaletteButton;
import CH.ifa.draw.palette.PaletteLayout;
import CH.ifa.draw.palette.PaletteListener;
import CH.ifa.draw.palette.ToolButton;
import CH.ifa.draw.standard.StandardDrawing;
import CH.ifa.draw.standard.StandardDrawingView;
import CH.ifa.draw.storable.StorableInput;
import CH.ifa.draw.storable.StorableOutput;
import CH.ifa.draw.tool.SelectionTool;
import CH.ifa.draw.util.ColorMap;
import CH.ifa.draw.util.Iconkit;

/**
 * DrawApplication defines a standard presentation for
 * standalone drawing editors. The presentation is
 * customized in subclasses.
 * The application is started as follows:
 * <pre>
 * public static void main(String[] args) {
 *     MayDrawApp window = new MyDrawApp();
 *     window.open();
 * }
 * </pre>
 */

// MIW - tried to move all this from AWT to Swing

public  class DrawApplication
        extends JFrame		// MIW Swing
        implements DrawingEditor, PaletteListener {


	private static final long serialVersionUID = -6787930795718312998L;
	private Drawing             fDrawing;
    private Tool                fTool;
    @SuppressWarnings("unused")
	private Iconkit             fIconkit;

    private JTextField           fStatusLine;		// MIW Swing
    private StandardDrawingView fView;
    private ToolButton          fDefaultToolButton;
    private ToolButton          fSelectedToolButton;

    @SuppressWarnings("unused")
	private String              fDrawingFilename;
    static String               fgUntitled = "untitled";

    // the image resource path
    private static final String fgDrawPath = "/CH/ifa/draw/";
    public static final String IMAGES = fgDrawPath+"images/";

    /**
     * The index of the file menu in the menu bar. Numerical value used in JMenu.getMenu()
     */
    public static final int    FILE_MENU = 0;
    /**
     * The index of the edit menu in the menu bar.
     */
    public static final int    EDIT_MENU = 1;
    /**
     * The index of the alignment menu in the menu bar.
     */
    public static final int    ALIGNMENT_MENU = 2;
    /**
     * The index of the attributes menu in the menu bar.
     */
    public static final int    ATTRIBUTES_MENU = 3;

    /**
     * Constructs a drawing window with a default title.
     */
    public DrawApplication() {
        super("JHotDraw");
    }

    /**
     * Constructs a drawing window with the given title.
     */
    public DrawApplication(String title) {
        super(title);
    }

    /**
     * Registers the listeners for this window
     */
	protected void addListeners() {
	    addWindowListener(
            new WindowAdapter() {
                @Override
				public void windowClosing(WindowEvent event) {
                    exit();
                }
            }
        );
    }

    /**
     * Creates the alignment menu. Clients override this
     * method to add additional menu items.
     */
    protected JMenu createAlignmentMenu() {
		CommandMenu menu = new CommandMenu("Align");
		menu.add(new ToggleGridCommand("Toggle Snap to Grid", fView, new Point(4,4)));
		menu.addSeparator();
		menu.add(new AlignCommand("Lefts", fView, AlignCommand.Alignment.LEFTS));
		menu.add(new AlignCommand("Centers", fView, AlignCommand.Alignment.CENTERS));
		menu.add(new AlignCommand("Rights", fView, AlignCommand.Alignment.RIGHTS));
		menu.addSeparator();
		menu.add(new AlignCommand("Tops", fView, AlignCommand.Alignment.TOPS));
		menu.add(new AlignCommand("Middles", fView, AlignCommand.Alignment.MIDDLES));
		menu.add(new AlignCommand("Bottoms", fView, AlignCommand.Alignment.BOTTOMS));
		return menu;
	}

    /**
     * Creates the arrows menu.
     */
    protected JMenu createArrowMenu() {
        CommandMenu menu = new CommandMenu("Arrow");
        menu.add(new ChangeAttributeCommand("none",     "ArrowMode", new Integer(PolyLineFigure.ARROW_TIP_NONE),  fView));
        menu.add(new ChangeAttributeCommand("at Start", "ArrowMode", new Integer(PolyLineFigure.ARROW_TIP_START), fView));
        menu.add(new ChangeAttributeCommand("at End",   "ArrowMode", new Integer(PolyLineFigure.ARROW_TIP_END),   fView));
        menu.add(new ChangeAttributeCommand("at Both",  "ArrowMode", new Integer(PolyLineFigure.ARROW_TIP_BOTH),  fView));
        return menu;
    }

    /**
     * Creates the attributes menu and its submenus. Clients override this
     * method to add additional menu items.
     */
    protected JMenu createAttributesMenu() {
        JMenu menu = new JMenu("Attributes");
        menu.add(createColorMenu("Fill Color", "FillColor"));
        menu.add(createColorMenu("Pen Color", "FrameColor"));
        menu.add(createArrowMenu());
		menu.addSeparator();
        menu.add(createFontMenu());
        menu.add(createFontSizeMenu());
        menu.add(createFontStyleMenu());
        menu.add(createColorMenu("Text Color", "TextColor"));
        return menu;
    }

    /**
     * Creates the color menu.
     */
	protected JMenu createColorMenu(String title, String attribute) {
		CommandMenu menu = new CommandMenu(title);
		for (int i = 0; i < ColorMap.size(); i++)
			menu.add(new ChangeAttributeCommand(ColorMap.name(i), attribute,
					ColorMap.color(i), fView));
		return menu;
	}

    /**
     * Creates the contents component of the application
     * frame. By default the DrawingView is returned in
     * a JScrollPane. // MIW ScrollPane -> JScrollPane
     */
    protected JComponent createContents(StandardDrawingView view) {
/*      MIW -> Swing */
		JScrollPane sp = new JScrollPane(view);
        //Adjustable vadjust = sp.getVAdjustable();
        //Adjustable hadjust = sp.getHAdjustable(); 
    	//JScrollPane sp = new JScrollPane();*/
        //JScrollBar vadjust = sp.getVerticalScrollBar();
        //JScrollBar hadjust = sp.getHorizontalScrollBar();
        //hadjust.setUnitIncrement(16);
        //vadjust.setUnitIncrement(16);

       // sp.add(view);
        return sp;
    }

    /**
     * Creates the debug menu. Clients override this
     * method to add additional menu items.
     */
	protected JMenu createDebugMenu() {
		JMenu menu = new JMenu("Debug");

		JMenuItem mi = new JMenuItem("Simple Update");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fView.setDisplayUpdate(new SimpleUpdateStrategy());
			}
		});
		menu.add(mi);

		mi = new JMenuItem("Buffered Update");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				fView.setDisplayUpdate(new BufferedUpdateStrategy());
			}
		});
		menu.add(mi);
		return menu;
	}

    /**
     * Creates the drawing used in this application.
     * You need to override this method to use a Drawing
     * subclass in your application. By default a standard
     * Drawing is returned.
     */
    protected Drawing createDrawing() {
        return new StandardDrawing();
    }

    /**
     * Creates the drawing view used in this application.
     * You need to override this method to use a DrawingView
     * subclass in your application. By default a standard
     * DrawingView is returned.
     */
    protected StandardDrawingView createDrawingView() {
        Dimension d = getDrawingViewSize();
        return new StandardDrawingView(this, d.width, d.height);
    }

    /**
     * Creates the edit menu. Clients override this
     * method to add additional menu items.
     */
    protected JMenu createEditMenu() {
		CommandMenu menu = new CommandMenu("Edit");
		menu.add(new CutCommand("Cut", fView), 'x');
		menu.add(new CopyCommand("Copy", fView), 'c');
		menu.add(new PasteCommand("Paste", fView), 'v');
		menu.addSeparator();
		menu.add(new DuplicateCommand("Duplicate", fView), 'd');
		menu.add(new DeleteCommand("Delete", fView));
		menu.addSeparator();
		menu.add(new GroupCommand("Group", fView));
		menu.add(new UngroupCommand("Ungroup", fView));
		menu.addSeparator();
		menu.add(new SendToBackCommand("Send to Back", fView));
		menu.add(new BringToFrontCommand("Bring to Front", fView));
		return menu;
	}

    /**
     * Creates the file menu. Clients override this
     * method to add additional menu items.
     */
	protected JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		JMenuItem mi = new JMenuItem("New", 'n');
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				promptNew();
			}
		});
		menu.add(mi);

		mi = new JMenuItem("Open...", 'o');
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				promptOpen();
			}
		});
		menu.add(mi);

		mi = new JMenuItem("Save As...", 's');
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				promptSaveAs();
			}
		});
		menu.add(mi);

		mi = new JMenuItem("Save As Serialized...");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				promptSaveAsSerialized();
			}
		});
		menu.add(mi);
		menu.addSeparator();
		mi = new JMenuItem("Print...", 'p');
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				print();
			}
		});
		menu.add(mi);
		menu.addSeparator();
		mi = new JMenuItem("Exit");
		mi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exit();
			}
		});
		menu.add(mi);
		return menu;
	}

    /**
     * Creates the fonts menus. It installs all available fonts
     * supported by the toolkit implementation.
     */
    protected JMenu createFontMenu() {
        CommandMenu menu = new CommandMenu("Font");
        //String fonts[] = Toolkit.getDefaultToolkit().getFontList(); 
        // fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        // MIW: Just a few common fonts
        String fonts[] = {"Arial", "Courier New", "Helvetica", "Times New Roman"};
        for (int i = 0; i < fonts.length; i++)
            menu.add(new ChangeAttributeCommand(fonts[i], "FontName", fonts[i],  fView));
        return menu;
    }

    /**
     * Creates the font size menu.
     */
	protected JMenu createFontSizeMenu() {
		CommandMenu menu = new CommandMenu("Font Size");
		int sizes[] = { 9, 10, 12, 14, 18, 24, 36, 48, 72 };
		for (int i = 0; i < sizes.length; i++) {
			menu.add(new ChangeAttributeCommand(Integer.toString(sizes[i]),
					"FontSize", new Integer(sizes[i]), fView));
		}
		return menu;
	}

    /**
     * Creates the font style menu with entries (Plain, Italic, Bold).
     */
    protected JMenu createFontStyleMenu() {
        CommandMenu menu = new CommandMenu("Font Style");
        menu.add(new ChangeAttributeCommand("Plain", "FontStyle", new Integer(Font.PLAIN), fView));
        menu.add(new ChangeAttributeCommand("Italic","FontStyle", new Integer(Font.ITALIC),fView));
        menu.add(new ChangeAttributeCommand("Bold",  "FontStyle", new Integer(Font.BOLD),  fView));
        return menu;
    }

    /**
     * Creates the standard menus. Clients override this
     * method to add additional menus.
     */
    protected void createMenus(JMenuBar mb) {
		mb.add(createFileMenu());		
		mb.add(createEditMenu());
		mb.add(createAlignmentMenu());
		mb.add(createAttributesMenu());
		mb.add(createDebugMenu());
    }

    /**
     * Creates the selection tool used in this editor. Override to use
     * a custom selection tool.
     */
    protected Tool createSelectionTool() {
        return new SelectionTool(view());
    }

    /**
     * Creates the status line.
     */
    protected JTextField createStatusLine() {
        JTextField field = new JTextField("No Tool", 40);
        field.setEditable(false);
        return field;
    }

    /**
     * Creates a tool button with the given image, tool, and text
     */
    protected ToolButton createToolButton(String iconName, String toolName, Tool tool) {
        return new ToolButton(this, iconName, toolName, tool);
    }

    /**
     * Creates the tool palette.
     */
    protected JPanel createToolPalette() {
        JPanel palette = new JPanel();
        palette.setBackground(Color.lightGray);
        palette.setLayout(new PaletteLayout(2,new Point(2,2)));
        return palette;
    }

    /**
     * Creates the tools. By default only the selection tool is added.
     * Override this method to add additional tools.
     * Call the inherited method to include the selection tool.
     * @param palette the palette where the tools are added.
     */
    protected void createTools(JPanel palette) {
        Tool tool = createSelectionTool();

        fDefaultToolButton = createToolButton(IMAGES+"SEL", "Selection Tool", tool);
        palette.add(fDefaultToolButton);
    }

    /**
     * Gets the default size of the window.
     */
    protected Dimension defaultSize() {
        return new Dimension(430,406);
    }

    /**
     * Handles additional clean up operations. Override to destroy
     * or release drawing editor resources.
     */
    protected void destroy() {
    }

    /**
     * Gets the current drawing.
     * @see DrawingEditor
     */
    @Override
	public Drawing drawing() {
        return fDrawing;
    }

    /**
     * Exits the application. You should never override this method
     */
    public void exit() {
        destroy();
        setVisible(false);      	// hide the Frame
        dispose();   				// tell windowing system to free resources
		System.exit(0);
    }

    /**
     * Override to define the dimensions of the drawing view.
     */
    protected Dimension getDrawingViewSize() {
        return new Dimension(400, 400);
    }

    private String getSavePath(String title) {
        String path = null;
        FileDialog dialog = new FileDialog(this, title, FileDialog.SAVE);
        dialog.setVisible(true);
        String filename = dialog.getFile();
        if (filename != null) {
            filename = stripTrailingAsterisks(filename);
            String dirname = dialog.getDirectory();
            path = dirname + filename;
        }
        dialog.dispose();
        return path;
    }

    private String guessType(String file) {
        if (file.endsWith(".draw"))
            return "storable";
        if (file.endsWith(".ser"))
            return "serialized";
        return "unknown";
    }

    private void initDrawing() {
        fDrawing = createDrawing();
        fDrawingFilename = fgUntitled;
        fView.setDrawing(fDrawing);
        toolDone();
    }

    private void loadDrawing(String file) {
        toolDone();
        String type = guessType(file);
        if (type.equals("storable"))
            readFromStorableInput(file);
        else if (type.equals("serialized"))
            readFromObjectInput(file);
        else
            showStatus("Unknown file type");
    }

    /**
     * Opens the window and initializes its contents.
     * Clients usually only call but don't override it.
     */

    public void open() {
        fIconkit = new Iconkit(this);
        Container contentPane = this.getContentPane();		// MIW Swing
		contentPane.setLayout(new BorderLayout());

        fView = createDrawingView();
        Component contents = createContents(fView);
        contentPane.add("Center", contents);			// MIW Swing

        JPanel tools = createToolPalette();
        createTools(tools);
        contentPane.add("West", tools);

        fStatusLine = createStatusLine();
        contentPane.add("South", fStatusLine);

		JMenuBar mb = new JMenuBar();
		createMenus(mb);
		this.setJMenuBar(mb);

        initDrawing();
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        // MIW: At moment dimensions determined by DrawApplication.getDrawingViewSize()
        // Dimension d = defaultSize();
		//setSize(d.width, d.height); 

        addListeners();

    }

    /**
     * Handles when the mouse enters or leaves a palette button.
     * @see PaletteListener
     */
    @Override
	public void paletteUserOver(PaletteButton button, boolean inside) {
        ToolButton toolButton = (ToolButton) button;
        if (inside)
            showStatus(toolButton.name());
        else
            showStatus(fSelectedToolButton.name());
    }

    /**
     * Handles a user selection in the palette.
     * @see PaletteListener
     */
    @Override
	public void paletteUserSelected(PaletteButton button) {
        ToolButton toolButton = (ToolButton) button;
        setTool(toolButton.tool(), toolButton.name());
        setSelected(toolButton);
    }

    /**
     * Prints the drawing.
     */
    public void print() {
        fTool.deactivate();
        PrintJob printJob = getToolkit().getPrintJob(this, "Print Drawing", null);

        if (printJob != null) {
            Graphics pg = printJob.getGraphics();

            if (pg != null) {
                fView.printAll(pg);
                pg.dispose(); // flush page
            }
            printJob.end();
        }
        fTool.activate();
    }

    /**
     * Resets the drawing to a new empty drawing.
     */
    public void promptNew() {
        initDrawing();
    }

    /**
     * Shows a file dialog and opens a drawing.
     */
    public void promptOpen() {
        FileDialog dialog = new FileDialog(this, "Open File...", FileDialog.LOAD);
        dialog.setVisible(true);
        String filename = dialog.getFile();
        if (filename != null) {
            filename = stripTrailingAsterisks(filename);
            String dirname = dialog.getDirectory();
            loadDrawing(dirname + filename);
        }
        dialog.dispose();
    }

    /**
     * Shows a file dialog and saves drawing.
     */
    public void promptSaveAs() {
        toolDone();
        String path = getSavePath("Save File...");
        if (path != null) {
            if (!path.endsWith(".draw"))
                path += ".draw";
            saveAsStorableOutput(path);
        }
    }

    /**
     * Shows a file dialog and saves drawing.
     */
    public void promptSaveAsSerialized() {
        toolDone();
        String path = getSavePath("Save File...");
        if (path != null) {
            if (!path.endsWith(".ser"))
                path += ".ser";
            saveAsObjectOutput(path);
        }
    }

    private void readFromObjectInput(String file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            ObjectInput input = new ObjectInputStream(stream);
            fDrawing.release();
            fDrawing = (Drawing)input.readObject();
            fView.setDrawing(fDrawing);
        } catch (IOException e) {
            initDrawing();
            showStatus("Error: " + e);
        } catch (ClassNotFoundException e) {
            initDrawing();
            showStatus("Class not found: " + e);
        }
    }

    private void readFromStorableInput(String file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            StorableInput input = new StorableInput(stream);
            fDrawing.release();
            fDrawing = (Drawing)input.readStorable();
            fView.setDrawing(fDrawing);
        } catch (IOException e) {
            initDrawing();
            showStatus("Error: " + e);
        }
    }

    private void saveAsObjectOutput(String file) {
        // TBD: should write a MIME header
        try {
            FileOutputStream stream = new FileOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(stream);
            output.writeObject(fDrawing);
            output.close();
        } catch (IOException e) {
            showStatus(e.toString());
        }
    }

    private void saveAsStorableOutput(String file) {
        // TBD: should write a MIME header
        try {
            FileOutputStream stream = new FileOutputStream(file);
            StorableOutput output = new StorableOutput(stream);
            output.writeStorable(fDrawing);
            output.close();
        } catch (IOException e) {
            showStatus(e.toString());
        }
    }

    /**
     * Handles a change of the current selection. Updates all
     * menu items that are selection sensitive.
     * @see DrawingEditor
     */
    @Override
	public void selectionChanged(DrawingView view) {
        JMenuBar mb = getJMenuBar();
        CommandMenu editMenu = (CommandMenu)mb.getMenu(EDIT_MENU);
        editMenu.checkEnabled();
        CommandMenu alignmentMenu = (CommandMenu)mb.getMenu(ALIGNMENT_MENU);
        alignmentMenu.checkEnabled();
    }

    /**
     * Sets the drawing to be edited.
     */
    public void setDrawing(Drawing drawing) {
        fView.setDrawing(drawing);
        fDrawing = drawing;
    }

    private void setSelected(ToolButton button) {
        if (fSelectedToolButton != null)
            fSelectedToolButton.reset();
        fSelectedToolButton = button;
        if (fSelectedToolButton != null)
            fSelectedToolButton.select();
    }

    private void setTool(Tool t, String name) {
        if (fTool != null)
            fTool.deactivate();
        fTool = t;
        if (fTool != null) {
            fStatusLine.setText(name);
            fTool.activate();
        }
    }

    /**
     * Shows a status message.
     * @see DrawingEditor
     */
    @Override
	public void showStatus(String string) {
        fStatusLine.setText(string);
    }

    private String stripTrailingAsterisks(String filename) {
        // workaround for bug on NT
        if (filename.endsWith("*.*"))
            return filename.substring(0, filename.length() - 4);
        else
            return filename;
    }

    /**
     * Gets the current tool.
     * @see DrawingEditor
     */
    @Override
	public Tool tool() {
        return fTool;
    }

    /**
     * Sets the default tool of the editor.
     * @see DrawingEditor
     */
    @Override
	public void toolDone() {
        if (fDefaultToolButton != null) {
            setTool(fDefaultToolButton.tool(), fDefaultToolButton.name());
            setSelected(fDefaultToolButton);
        }
    }

    /**
     * Gets the current drawing view.
     * @see DrawingEditor
     */
    @Override
	public DrawingView view() {
        return fView;
    }
}
