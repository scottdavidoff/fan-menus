import java.awt.*;
import java.awt.geom.*;

import javax.swing.JComponent;

public class FloatingMenuItem2 extends JComponent
{
	Dimension d;
	String label;
	
	Point p1, p2, p3, p4;
	GeneralPath border;

	FloatingMenu2 parent;
	int ID;
	
	boolean isTopLevel, hasFocus, isBottomLevel;
	
	Font font;
	
	private static final int VERTICAL_BUFFER			= 5;
	private static final int HORIZONTAL_BUFFER		= 5;
	private static final int SHADOW_WIDTH			= 5;

	private static final Color STROKE_COLOR_UP   	= new Color(153, 153, 153);
	private static final float STROKE_ALPHA_UP 		= 0.5f;
	private static final Color FILL_COLOR_UP     	= new Color(153, 153, 153);
	private static final float FILL_ALPHA_UP 		= 0.5f;

	private static final Color STROKE_COLOR_OVER		= new Color(0, 102, 204);
	private static final float STROKE_ALPHA_OVER 	= 0.5f;
	private static final Color FILL_COLOR_OVER   	= new Color(0, 102, 204);
	private static final float FILL_ALPHA_OVER 		= 0.5f;

	private static final Color FONT_COLOR_UP			= new Color(0, 0, 0);
	private static final float FONT_ALPHA_UP 		= 1.0f;
	private static final Color FONT_COLOR_OVER		= new Color(255, 255, 255);
	private static final float FONT_ALPHA_OVER 		= 1.0f;
	
	public FloatingMenuItem2(Dimension d, String label, FloatingMenu2 parent)
	{
		this.d = d;
		this.label = label;
		this.parent = parent;

//		debug
//		System.out.println (this.label + ", (" + this.d + ")");
	}
	
	public void setLocation(Point p)
	{
		p1 = new Point(p.x, p.y);
		p2 = new Point(p.x, p.y + d.height);
		p3 = new Point(p.x + d.width, p.y + d.height);
		p4 = new Point(p.x + d.width, p.y);
		
		setPath();
	}
	
	public Point getLocation() { return p1; }

	public void setPath()
	{
		border = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		
		border.moveTo( (float) p1.getX(), (float) p1.getY() );
		border.lineTo( (float) p2.getX(), (float) p2.getY() );
		border.lineTo( (float) p3.getX(), (float) p3.getY() );
		border.lineTo( (float) p4.getX(), (float) p4.getY() );
		border.closePath();
	}
	
	public void setTopLevel(boolean isTopLevel)
	{
		this.isTopLevel = isTopLevel;
	}
	
	public boolean isTopLevel()
	{
		return isTopLevel;
	}
	
	public String getLabel() { return label; }
	
	public void setLabel(String label) { this.label = label; }
	
	public void setFocus(boolean hasFocus)
	{
		this.hasFocus = hasFocus;
	}
	
	public boolean getFocus()
	{
		return hasFocus;
	}
	
	public void setBottomLevel(boolean bottomLevel) { this.isBottomLevel = bottomLevel; }
	
	public boolean isBottomLevel() { return isBottomLevel; }
	
	public boolean contains(Point p)
	{
		if (p.x > p1.x && p.x < p4.x)
		{
			if (p.y > p1.y && p.y < p2.y) { return true; }
		}
		return false;
	}

	public void draw(Graphics2D g2d)
	{
		if (hasFocus || isTopLevel)
		{
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_OVER));
			g2d.setPaint(FILL_COLOR_OVER);
			g2d.fill(border);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_OVER));
			g2d.setPaint(STROKE_COLOR_OVER);
			g2d.draw(border);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_OVER));
			g2d.setPaint(FONT_COLOR_OVER);
		}
		else
		{
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_UP));
			g2d.setPaint(FILL_COLOR_UP);
			g2d.fill(border);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_UP));
			g2d.setPaint(STROKE_COLOR_UP);
			g2d.draw(border);

			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_UP));
			g2d.setPaint(FONT_COLOR_UP);
		}
		g2d.drawString(label, p2.x + FloatingMenuItem2.VERTICAL_BUFFER, p2.y - FloatingMenuItem2.HORIZONTAL_BUFFER);
		g2d.setPaint(new GradientPaint(p4.x, p4.y, new Color(0,0,0,153), p4.x + FloatingMenuItem2.SHADOW_WIDTH, p4.y, new Color(0,0,0,0)));
		g2d.fill(new Rectangle(p4, new Dimension(FloatingMenuItem2.SHADOW_WIDTH, d.height)));
		if (isBottomLevel())
		{
			g2d.setPaint(new GradientPaint(new Point(p2.x, p2.y), new Color(0,0,0,153), new Point(p2.x, p3.y + FloatingMenuItem2.SHADOW_WIDTH), new Color(0,0,0,0)));
			g2d.fill(new Rectangle(p2, new Dimension(d.width, FloatingMenuItem2.SHADOW_WIDTH)));
			g2d.setPaint(new GradientPaint(new Point(p3.x, p3.y), new Color(0,0,0,153), new Point(p3.x + (int)(FloatingMenuItem2.SHADOW_WIDTH / 1.4), p3.y + (int)(FloatingMenuItem2.SHADOW_WIDTH / 1.4)), new Color(0,0,0,0)));
			g2d.fill(new Rectangle(p3, new Dimension(FloatingMenuItem2.SHADOW_WIDTH, FloatingMenuItem2.SHADOW_WIDTH)));
		}
	}
}
