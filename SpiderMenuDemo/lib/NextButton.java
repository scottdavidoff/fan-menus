import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class NextButton extends JComponent
{
	private FloatingComponent controller;
	private int x, y, textX, textY;
	private String label, time, trialsLeft;
	boolean hasFocus;
//	private Rectangle r;
	private RoundRectangle2D.Double r;
	private Point p, pTime, pTrials;
	private boolean set, displayBottomLabels;
	
	private static final int BUTTON_TEXT_BORDER =  5;
	private static final int LEFT_LABEL_BORDER	 =	20;
	private static final int RIGHT_LABEL_BORDER =  30;

	private static final Color NEXT_STROKE_UP  	= new Color(0, 0, 0, 255);
	private static final Color NEXT_FILL_UP    	= new Color(255, 204, 51, 255);
	private static final Color NEXT_TEXT_UP    	= new Color(255, 255, 255, 255);
	
	private static final Color NEXT_FILL_OVER   	= new Color(0, 0, 0, 255);
	private static final Color NEXT_STROKE_OVER	= new Color(255, 153, 0, 255);
	private static final Color NEXT_TEXT_OVER   	= new Color(255, 255, 255, 255);
	
	private static final Color LABEL_TEXT			= new Color(0, 0, 0, 0);

	public NextButton(FloatingComponent controller)
	{
		this.controller = controller;
		x = 0;
		y = 0;
		textX = 0;
		textY = 0;
		label = "";
		time = "Time: ";
		trialsLeft = "Trials Left:";
		hasFocus = false;
//		r = new Rectangle();
		r = new RoundRectangle2D.Double();
		p = new Point();
		pTime = new Point();
		pTrials = new Point();
		set = false;
	}
	
	public void setX(int x) { this.x = x; }
	
	public int getX() { return x; }
	
	public void setY(int y) { this.y = y; }
	
	public int getY() { return y; }
	
	public void setLabel(String label) { this.label = label; }
	
	public String getLabel() { return label; }
	
	public void setLocation(Point p) { this.p = p; }
	
	public Point getLocation() { return p; }
	
//	public void setRectangle(Rectangle r) { this.r = r; }
//	
//	public Rectangle getRectangle() { return r; }

	public void setRectangle(RoundRectangle2D.Double r) { this.r = r; }
	
	public RoundRectangle2D.Double getRectangle() { return r; }

	public void setFocus(boolean hasFocus) { this.hasFocus = hasFocus; }
	
	public boolean hasFocus() { return hasFocus; }
 
	public void setButton(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		FontMetrics metrics = g2d.getFontMetrics();
		int textWidth = metrics.stringWidth(label);
		int textHeight = metrics.getAscent() + metrics.getDescent();
		int buttonWidth = textWidth + (2 * BUTTON_TEXT_BORDER);
		int buttonHeight = textHeight + (2 * BUTTON_TEXT_BORDER);
		x = controller.getModel().getUsableParentWidth() / 2 - (buttonWidth / 2);
		setLocation(new Point(x, y));
//		setRectangle(new Rectangle(p, new Dimension(buttonWidth, buttonHeight)));
		double arcWH = buttonHeight < buttonWidth ? buttonHeight * 0.5 :buttonWidth * 0.5 ;
		setRectangle(new RoundRectangle2D.Double((double)p.x, (double)p.y, (double)buttonWidth, (double)buttonHeight, arcWH, arcWH));
		textX = p.x + BUTTON_TEXT_BORDER;
		textY = p.y + buttonHeight - BUTTON_TEXT_BORDER;
		
		pTime = new Point(LEFT_LABEL_BORDER, textY);
		int trialsLeftWidth = metrics.stringWidth(trialsLeft);
		pTrials = new Point(controller.getModel().getUsableParentWidth() - trialsLeftWidth - RIGHT_LABEL_BORDER, textY);
		set = true;
	}
	
	public boolean contains(Point p) { return r.contains(p); }
	
	public void paint(Graphics g)
	{
		if (!set) { setButton(g); }
		Graphics2D g2d = (Graphics2D)g;
		if (hasFocus)
		{
			g2d.setPaint(NEXT_FILL_OVER);
			g2d.fill(r);
			g2d.setPaint(NEXT_STROKE_OVER);
			g2d.draw(r);
			g2d.setPaint(NEXT_TEXT_OVER);
			g2d.drawString(label, textX, textY);
		}
		else
		{
			g2d.setPaint(NEXT_FILL_UP);
			g2d.fill(r);
			g2d.setPaint(NEXT_STROKE_UP);
			g2d.draw(r);
			g2d.setPaint(NEXT_TEXT_UP);
			g2d.drawString(label, textX, textY);
		}
		if (displayBottomLabels)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawString(time, pTime.x, pTime.y);
			g2d.drawString(trialsLeft, pTrials.x, pTrials.y);
		}
	}
	public String getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = "Time: " + new Long(time).toString();
	}
	public String getTrialsLeft() {
		return trialsLeft;
	}
	public void setTrialsLeft(int trialsLeft) {
		this.trialsLeft = "Trials left: " + new Integer(trialsLeft).toString();
	}
	public void setDisplayBottomLabels(boolean displayBottomLabels) {
		this.displayBottomLabels = displayBottomLabels;
	}
}
