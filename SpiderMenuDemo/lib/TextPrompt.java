import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class TextPrompt extends JComponent implements KeyListener
{
	private FloatingComponentModel model;
	private Point p;
	private String prompt;
	private int parentWidth, parentHeight, height, iconX, iconY;
	private boolean set, hasIcon;
	private Icon icon;
	
	private static final Color TEXT_COLOR = new Color(51, 51, 51, 204);
	private static final int BUTTON_TEXT_GAP 	= 15;
	private static final int ICON_TEXT_GAP 	= 10;
	
	public TextPrompt(FloatingComponent controller)
	{
		model = controller.getModel();
		parentWidth = model.getUsableParentWidth();
		parentHeight = model.getUsableParentHeight();
//		height = model.getNextButton().getY() - BUTTON_TEXT_GAP;
		height = model.getNextButton().getY() + BUTTON_TEXT_GAP;
		set = true;
		addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void keyTyped(KeyEvent arg0) {
		System.out.println("TextPrompt typed");
	}

	public void calcLocation(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		FontMetrics metrics = g2d.getFontMetrics();
		int width = metrics.stringWidth(prompt);
		p = new Point(parentWidth / 2 - width / 2, height) ;
		set = true;
		if (hasIcon)
		{
			iconX = p.x - icon.getIconWidth() - ICON_TEXT_GAP;
			iconY = p.y - icon.getIconHeight();
		}
	}
	
	public Point getLocation() {
		return p;
	}
	public void setLocation(Point p) {
		this.p = p;
	}
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
		set = false;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
		hasIcon = true;
		set = false;
	}
	public void hasIcon(boolean hasIcon) {
		this.hasIcon = hasIcon;
	}
	public void paint(Graphics g)
	{
		if (!set) { calcLocation(g); }
		Graphics2D g2d = (Graphics2D)g;
		if  (hasIcon) { icon.paintIcon(this, g2d, iconX, iconY); }
		g2d.setColor(TEXT_COLOR);
		g2d.drawString(prompt, p.x, p.y);
	}
}
