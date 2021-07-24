import java.awt.*;

public class SpiderSectorModel
{
	SpiderSector controller;
	SpiderSectorView view;
	Point location;
	Polygon p;
	Point p1, p2, p3, p4;
	int type, ID;
	boolean hasFocus;
	
	public SpiderSectorModel(SpiderSector controller)
	{
		this.controller = controller;
	}

	public SpiderSector getController() { return controller; }

	public void setController(SpiderSector controller) { this.controller = controller; }

	public void setView(SpiderSectorView view) { this.view = view; }
	
	public SpiderSectorView getView() { return view; }

	public Polygon getPolygon() { return p; }

	public void setPolygon(Polygon p) { this.p = p; }

	public Point getLocation() { return location; }

	public void setLocation(Point p) { this.location = p; }
	
	public int getType() { return type; }
	
	public void setType(int type) { this.type = type; }
	
	public int getID() { return ID; }
	
	public void setID(int ID) { this.ID = ID; }
	
	public boolean hasFocus() { return hasFocus; }
	
	public void setFocus(boolean hasFocus) { this.hasFocus = hasFocus; }

	public Point getP1() {
		return p1;
	}
	public void setP1(Point p1) {
		this.p1 = p1;
	}
	public Point getP2() {
		return p2;
	}
	public void setP2(Point p2) {
		this.p2 = p2;
	}
	public Point getP3() {
		return p3;
	}
	public void setP3(Point p3) {
		this.p3 = p3;
	}
	public Point getP4() {
		return p4;
	}
	public void setP4(Point p4) {
		this.p4 = p4;
	}
}
