import java.awt.*;

public class FloatingMenuItemView
{
	FloatingMenuItemModel model;

	private static final int VERTICAL_BUFFER		= 5;
	private static final int HORIZONTAL_BUFFER		= 5;
	private static final int SHADOW_WIDTH			= 5;

	private static final Color STROKE_COLOR_UP   	= new Color(153, 153, 153, 255);
	private static final float STROKE_ALPHA_UP 	= 1.0f;
	private static final Color FILL_COLOR_UP     	= new Color(153, 153, 153, 255);
	private static final float FILL_ALPHA_UP 		= 1.0f;

	private static final Color STROKE_COLOR_OVER	= new Color(0, 102, 204, 255);
	private static final float STROKE_ALPHA_OVER 	= 1.0f;
	private static final Color FILL_COLOR_OVER   	= new Color(0, 102, 204, 255);
	private static final float FILL_ALPHA_OVER 	= 1.0f;

	private static final Color FONT_COLOR_UP		= new Color(0, 0, 0);
	private static final float FONT_ALPHA_UP 		= 1.0f;
	private static final Color FONT_COLOR_OVER		= new Color(255, 255, 255, 255);
	private static final float FONT_ALPHA_OVER 	= 1.0f;

	private Font font;

	public FloatingMenuItemView(FloatingMenuItemModel model) { this.model = model; }

	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if (model.isTopLevel())
		{
			if (model.isShowing())
			{
//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_OVER));
				g2d.setPaint(FILL_COLOR_OVER);
				g2d.fill(model.getRectangle());

//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_OVER));
				g2d.setPaint(STROKE_COLOR_OVER);
				g2d.draw(model.getRectangle());

//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_OVER));
				g2d.setPaint(FONT_COLOR_OVER);
			}
			else
			{
//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_UP));
				g2d.setPaint(FILL_COLOR_UP);
				g2d.fill(model.getRectangle());

//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_UP));
				g2d.setPaint(STROKE_COLOR_UP);
				g2d.draw(model.getRectangle());

//				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_UP));
				g2d.setPaint(FONT_COLOR_UP);
			}
			g2d.drawString(model.getLabel(), model.getP2().x + VERTICAL_BUFFER, model.getP2().y - HORIZONTAL_BUFFER);
		}
		else
		{
			if (model.isShowing())
			{
				if (model.hasFocus())
				{
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_OVER));
					g2d.setPaint(FILL_COLOR_OVER);
					g2d.fill(model.getRectangle());

//					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_OVER));
					g2d.setPaint(STROKE_COLOR_OVER);
					g2d.draw(model.getRectangle());

//					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_OVER));
					g2d.setPaint(FONT_COLOR_OVER);
				}
				else
				{
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FILL_ALPHA_UP));
					g2d.setPaint(FILL_COLOR_UP);
					g2d.fill(model.getRectangle());

//					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, STROKE_ALPHA_UP));
					g2d.setPaint(STROKE_COLOR_UP);
					g2d.draw(model.getRectangle());

//					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, FONT_ALPHA_UP));
					g2d.setPaint(FONT_COLOR_UP);
				}
				g2d.drawString(model.getLabel(), model.getP2().x + VERTICAL_BUFFER, model.getP2().y - HORIZONTAL_BUFFER);

				g2d.setPaint(new GradientPaint(model.getP4().x, model.getP4().y, new Color(0,0,0,153), model.getP4().x + SHADOW_WIDTH, model.getP4().y, new Color(0,0,0,0)));
				g2d.fill(new Rectangle(model.getP4(), new Dimension(SHADOW_WIDTH, model.getHeight())));

				g2d.setPaint(new GradientPaint(model.getP1().x, model.getP1().y, new Color(0,0,0,153), model.getP1().x - SHADOW_WIDTH, model.getP1().y, new Color(0,0,0,0)));
				g2d.fill(new Rectangle(new Point(model.getP1().x - SHADOW_WIDTH, model.getP1().y), new Dimension(SHADOW_WIDTH, model.getHeight())));

				if (model.isBottomLevel())
				{
					g2d.setPaint(new GradientPaint(model.getP2(), new Color(0,0,0,153), new Point(model.getP2().x, model.getP3().y + SHADOW_WIDTH), new Color(0,0,0,0)));
					g2d.fill(new Rectangle(model.getP2(), new Dimension(model.getWidth(), SHADOW_WIDTH)));
//					g2d.setPaint(new GradientPaint(model.getP3(), new Color(0,0,0,153), new Point(model.getP3().x + (int)(SHADOW_WIDTH / 1.4), model.getP4().y + (int)(SHADOW_WIDTH / 1.4)), new Color(0,0,0,0)));
//					g2d.fill(new Rectangle(model.getP3(), new Dimension(SHADOW_WIDTH, SHADOW_WIDTH)));
				}
			}
		}
	}
}
