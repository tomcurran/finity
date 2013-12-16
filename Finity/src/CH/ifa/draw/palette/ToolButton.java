/*
 * @(#)ToolButton.java 5.1
 *
 */

package CH.ifa.draw.palette;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.util.HJDError;
import CH.ifa.draw.util.Iconkit;

/**
 * A PaletteButton that is associated with a tool.
 * @see Tool
 */
public class ToolButton extends PaletteButton {

	private static final long serialVersionUID = -2154298479501447177L;
	private String          fName;
    private Tool            fTool;
    private PaletteIcon     fIcon;

    public ToolButton(PaletteListener listener, String iconName, String name, Tool tool) {
        super(listener);
        // use a Mediatracker to ensure that all the images are initially loaded
        Iconkit kit = Iconkit.instance();
        if (kit == null)
            throw new HJDError("Iconkit instance isn't set");

        Image im[] = new Image[3];
        im[0] = kit.loadImageResource(iconName+"1.gif");
        im[1] = kit.loadImageResource(iconName+"2.gif");
        im[2] = kit.loadImageResource(iconName+"3.gif");

        MediaTracker tracker = new MediaTracker(this);
        for (int i = 0; i < 3; i++) {
            tracker.addImage(im[i], i);
        }
        try {
            tracker.waitForAll();
        } catch (Exception e) {  }

        fIcon = new PaletteIcon(new Dimension(24,24), im[0], im[1], im[2]);
        fTool = tool;
        fName = name;
    }

    public Object attributeValue() {
        return tool();
    }

    @Override
	public Dimension getMinimumSize() {
        return new Dimension(fIcon.getWidth(), fIcon.getHeight());
    }

    @Override
	public Dimension getPreferredSize() {
        return new Dimension(fIcon.getWidth(), fIcon.getHeight());
    }

    @Override
	public String name() {
        return fName;
    }

    @Override
	public void paintBackground(Graphics g) { }

    @Override
	public void paintNormal(Graphics g) {
        if (fIcon.normal() != null)
            g.drawImage(fIcon.normal(), 0, 0, this);
    }

    @Override
	public void paintPressed(Graphics g) {
        if (fIcon.pressed() != null)
            g.drawImage(fIcon.pressed(), 0, 0, this);
    }

    @Override
	public void paintSelected(Graphics g) {
        if (fIcon.selected() != null)
            g.drawImage(fIcon.selected(), 0, 0, this);
    }

    public Tool tool() {
        return fTool;
    }

}

