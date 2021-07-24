import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SpiderMenuModel
{
	private SpiderMenu controller;
	private SpiderMenuView view;
	private FloatingComponent parent;
	
	private Vector floatingComponents, spiderZones, spiderSectors;
	private boolean isShowing, hasFocus;
	private Point p;
	private MouseEvent sourceEvent;

	public SpiderMenuModel(SpiderMenu controller)
	{
		this.controller = controller;
		isShowing = false;
	}

	public void setController(SpiderMenu controller) { this.controller = controller; }
	
	public SpiderMenu getController() { return controller; }
	
	public void setView(SpiderMenuView view) { this.view = view; }
	
	public SpiderMenuView getView() { return view; }
	
	public void setParent(FloatingComponent parent) { this.parent = parent; }
	
	public FloatingComponent getParent() { return parent; }
	
	public void setSpiderZones(Vector spiderZones) { this.spiderZones = spiderZones; }
	
	public Vector getSpiderZones() { return spiderZones; }
	
	public void setSpiderSectors(Vector spiderSectors) { this.spiderSectors = spiderSectors; }
	
	public Vector getSpiderSectors() { return spiderSectors; }
	
	public void setFloatingComponents(Vector floatingComponents) { this.floatingComponents = floatingComponents; }
	
	public Vector getFloatingComponents() { return floatingComponents; }

	public void setLocation(Point p) { this.p = p; }
	
	public Point getLocation() { return p; }
	
	public void setIsShowing(boolean isShowing) { this.isShowing = isShowing; }
	
	public boolean isShowing() { return isShowing; }

	public void setFocus(boolean hasFocus) { this.hasFocus = hasFocus; }
	
	public boolean hasFocus() { return hasFocus; }
	
	public void setSourceEvent(MouseEvent e) { this.sourceEvent = e; }
	
	public MouseEvent getSourceEvent() { return sourceEvent; }
}