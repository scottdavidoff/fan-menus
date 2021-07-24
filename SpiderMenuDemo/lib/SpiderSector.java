import java.awt.*;

import javax.swing.*;

public class SpiderSector extends JComponent
{
	SpiderMenu owner;
	SpiderSectorModel model;
	public static final int SPIDER_ZONE   = 0;
	public static final int SPIDER_SECTOR = 1;
	
	public SpiderSector(SpiderMenu owner)
	{
		this.owner = owner;
		model = new SpiderSectorModel(this);

		SpiderSectorView view = new SpiderSectorView(model);
		model.setView(view);
	}

	public Polygon getPolygon() { return model.getPolygon(); }

	public void setPolygon(Polygon p) { model.setPolygon(p); }

	public Point getLocation() { return model.getLocation(); }

	public void setLocation(Point p) { model.setLocation(p); }
	
	public boolean contains(Point p) { return model.getPolygon().contains(p); }
	
	public void setType(int type) { model.setType(type); }
	
	public int getType() { return model.getType(); }
	
	public void setFocus(boolean hasFocus) { model.setFocus(hasFocus); }
	
	public boolean hasFocus() { return model.hasFocus(); }
	
	public void setID(int ID) { model.setID(ID); }
	
	public int getID() { return model.getID(); }
	
	public Point getP1() {
		return model.getP1();
	}
	public void setP1(Point p1) {
		model.setP1(p1);
	}
	public Point getP2() {
		return model.getP2();
	}
	public void setP2(Point p2) {
		model.setP1(p2);
	}
	public Point getP3() {
		return model.getP3();
	}
	public void setP3(Point p3) {
		model.setP1(p3);
	}
	public Point getP4() {
		return model.getP4();
	}
	public void setP4(Point p4) {
		model.setP1(p4);
	}
	
	public void paint(Graphics g) { model.getView().paint(g); }
}
