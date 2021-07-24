import java.awt.*;

public class SpiderSectorView
{
	SpiderSectorModel model;

	private static final Color ZONE_STROKE_COLOR_UP   		= new Color(0, 153, 153, 255);
	private static final Color ZONE_STROKE_COLOR_OVER  	= new Color(0, 153, 153, 153);
//	private static final Color ZONE_STROKE_COLOR_UP   		= new Color(255, 153, 153, 255);
//	private static final Color ZONE_STROKE_COLOR_OVER  	= new Color(255, 153, 153, 153);
//	private static final Color ZONE_FILL_COLOR_UP     		= new Color(153, 153, 153);
//	private static final Color ZONE_FILL_COLOR_OVER     	= new Color(153, 153, 153);
	private static final Color SECTOR_STROKE_COLOR_UP		= new Color(153, 153, 153, 102);
	private static final Color SECTOR_STROKE_COLOR_OVER	= new Color(153, 153, 153, 201);
//	private static final Color SECTOR_FILL_COLOR_UP		= new Color(0, 102, 204);
//	private static final Color SECTOR_FILL_COLOR_OVER		= new Color(0, 102, 204);

	private static final float ZONE_STROKE_ALPHA_UP 	= 1.0f;
	private static final float ZONE_FILL_ALPHA_UP 		= 1.0f;
	private static final float STROKE_ALPHA_OVER 		= 1.0f;
	private static final float FILL_ALPHA_OVER 		= 1.0f;

	SpiderSectorView(SpiderSectorModel model) 
	{
		this.model = model;
	}
	
	public String toString() { return new String((model.getType() == SpiderSector.SPIDER_SECTOR ? "sector " : "zone ") + model.getID() + " hasFocus() = " + model.hasFocus()); }

	public void paint(Graphics g)
	{
//		System.out.println("paint " + this);
		Graphics2D g2d = (Graphics2D)g;
		if (model.getType() == SpiderSector.SPIDER_ZONE)
		{
			if (model.hasFocus())
			{
				g2d.setColor(ZONE_STROKE_COLOR_OVER);
//				g2d.setStroke(new BasicStroke(2.0f));
				g2d.draw(model.getPolygon());
			}
			else
			{
				g2d.setColor(ZONE_STROKE_COLOR_UP);
//				g2d.setStroke(new BasicStroke(1.0f));
				g2d.draw(model.getPolygon());
			}
		}
		else if (model.getType() == SpiderSector.SPIDER_SECTOR)
		{
			if (model.hasFocus())
			{
//				g2d.setColor(SECTOR_FILL_COLOR_OVER);
//				g2d.fill(model.getPolygon());
				g2d.setColor(SECTOR_STROKE_COLOR_OVER);
//				g2d.setStroke(new BasicStroke(1.0f));
				g2d.draw(model.getPolygon());
			}
			else
			{
				g2d.setColor(SECTOR_STROKE_COLOR_UP);
//				g2d.setStroke(new BasicStroke(1.0f));
				g2d.draw(model.getPolygon());
			}
		}
	}
}
