import java.awt.*;
import java.awt.geom.GeneralPath;

import javax.swing.*;

public class FloatingButton extends JComponent
{
	FloatingComponentView controller;
	JButton original;
	
	Icon buttonImage;
	
	int id, width, height, imageX, imageY;
	String label;
	boolean hasFocus;
//	JComponent owner;
	
	Point p1, p2, p3, p4;
	GeneralPath border;
	
	private static final Color STROKE_COLOR_UP   	= new Color(204, 204, 204, 255);
	private static final float STROKE_ALPHA_UP 	= 0.5f;
	private static final Color FILL_COLOR_UP     	= new Color(153, 153, 153, 255);
	private static final float FILL_ALPHA_UP 		= 0.5f;

	private static final Color STROKE_COLOR_OVER	= new Color(255, 153, 0, 255);
	private static final float STROKE_ALPHA_OVER 	= 0.75f;
	private static final Color FILL_COLOR_OVER   	= new Color(255, 204, 51, 255);
	private static final float FILL_ALPHA_OVER 	= 0.5f;

	private static final float BUTTON_ALPHA_UP 	= 0.4f;
	private static final float BUTTON_ALPHA_OVER	= 0.8f;
	
	private static final int	   SHADOW_WIDTH		= 5;

	public FloatingButton(JButton original)
	{
		this.original = original;
		buttonImage = original.getIcon();
		width  = this.original.getWidth();
		height = this.original.getHeight();
//		label  = this.original.getText();
		label  = this.original.getToolTipText();

		hasFocus = false;
	}
	
	public boolean contains(Point p)
	{
		if (p.x > p1.x && p.x < p4.x)
		{
			if (p.y > p1.y && p.y < p2.y)
			{ 
				return true;
			}
		}
		return false;
	}
	
	public void setID(int id) { this.id = id; }
	
	public int getID() { return id; }
	
	public void setWidth(int width) { this.width = width; }
	
	public int getWidth() { return width; }

	public void setHeight(int height)	{ this.height = height; }
	
	public int getHeight() { return height; }
	
	public void setLabel(String label) { this.label = label; }
	
	public String getLabel() 	{ return label; }

	public boolean hasFocus() { return hasFocus; }
	
	public void setFocus(boolean focusState) { this.hasFocus = focusState; } 

	public void setLocation(Point p)
	{
		border = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		
		border.moveTo( (float) p.getX(), (float) p.getY() );
		border.lineTo( (float) p.getX(), (float) p.getY() + height );
		border.lineTo( (float) p.getX() + width, (float) p.getY() + height );
		border.lineTo( (float) p.getX() + width, (float) p.getY() );
		border.closePath();
		
		imageX = p.x + ((width  - buttonImage.getIconWidth())  / 2);
		imageY = p.y + ((height - buttonImage.getIconHeight()) / 2);
		
		p1 = p;
		p2 = new Point(p.x, p.y + height);
		p3 = new Point(p.x + width, p.y + height);
		p4 = new Point(p.x + width, p.y);
	}
	
	public Point getLocation() { return p1; }
	
//	public void setOwner(JComponent owner) { this.owner = owner; }
	
	public String toString()
	{
		return new String("Floating Button (" + getLocation() + ", " + width + ", " + height + ") hasFocus() = " + hasFocus);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if (hasFocus)
		{
//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_OVER));
			g2d.setPaint(FILL_COLOR_OVER);
			g2d.fill(border);

//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_OVER));
			g2d.setPaint(STROKE_COLOR_OVER);
			g2d.draw(border);
			
//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BUTTON_ALPHA_OVER));
			buttonImage.paintIcon(this, g2d, imageX, imageY);

			g2d.setPaint(new GradientPaint(p4.x, p4.y, new Color(0,0,0,153), p4.x + FloatingButton.SHADOW_WIDTH, p4.y, new Color(0,0,0,0)));
			g2d.fill(new Rectangle (p4, new Dimension(FloatingButton.SHADOW_WIDTH, height)));
			g2d.setPaint(new GradientPaint(new Point(p2.x, p2.y), new Color(0,0,0,153), new Point(p2.x, p3.y + FloatingButton.SHADOW_WIDTH), new Color(0,0,0,0)));
			g2d.fill(new Rectangle(p2, new Dimension(width, FloatingButton.SHADOW_WIDTH)));
			g2d.setPaint(new GradientPaint(new Point(p3.x, p3.y), new Color(0,0,0,153), new Point(p3.x + (int)(FloatingButton.SHADOW_WIDTH / 1.4), p3.y + (int)(FloatingButton.SHADOW_WIDTH / 1.4)), new Color(0,0,0,0)));
			g2d.fill(new Rectangle(p3, new Dimension(FloatingButton.SHADOW_WIDTH, FloatingButton.SHADOW_WIDTH)));
		}
		else
		{
//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_UP));
			g2d.setPaint(FILL_COLOR_UP);
			g2d.fill(border);

//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_UP));
			g2d.setPaint(STROKE_COLOR_UP);
			g2d.draw(border);

//			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, BUTTON_ALPHA_UP));
			buttonImage.paintIcon(this, g2d, imageX, imageY);
		}
	}
}
