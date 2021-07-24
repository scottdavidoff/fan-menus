import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class SpiderMenu extends JComponent
{
	SpiderMenuModel model;
	SpiderMenuView view;

	public static final int MIN_SECTOR_WIDTH 			= 35;
	public static final int MIN_SECTOR_HEIGHT			= 35;
	public static final int EDGE_BUFFER				= 20;
	public static final int VERTEX_ZONE_PADDING 		=  5;

	public SpiderMenu(FloatingComponent parent, MouseEvent e)
	{
		model = new SpiderMenuModel(this);
		view = new SpiderMenuView(model);
		model.setView(view);
		model.setParent(parent);
		model.setSourceEvent(e);
		
		model.setFloatingComponents(parent.getModel().getFloatingComponents());
		instantiateSpiderSectors();
		
		FloatingLayoutManager layoutManager = new FloatingLayoutManager(model.getParent(), this);
		layoutManager.doSpiderLayout();
	}
	
	public void instantiateSpiderSectors()
	{
		// create zones
		Vector floatingComponents = model.getFloatingComponents();
		Vector spiderZones = new Vector();
		Vector spiderSectors = new Vector();

		SpiderSector[] zones = new SpiderSector[floatingComponents.size()];
		for (int i = 0; i < floatingComponents.size(); i++)
		{
			zones[i] = new SpiderSector(this);
			zones[i].setType(SpiderSector.SPIDER_ZONE);
			zones[i].setFocus(false);
			zones[i].setID(i);
		}
		spiderZones.add(zones);
		model.setSpiderZones(spiderZones);

		// create sectors
		for (int i = 0; i < floatingComponents.size(); i++)
		{
			SpiderSector[] sectors = new SpiderSector[((Object[])floatingComponents.get(i)).length];
			for (int j = 0; j < ((Object[])floatingComponents.get(i)).length; j++)
			{
				sectors[j] = new SpiderSector(this);
				sectors[j].setType(SpiderSector.SPIDER_SECTOR);
				sectors[j].setFocus(false);
				sectors[j].setID(j);
			}
			spiderSectors.add(sectors);
		}
		model.setSpiderSectors(spiderSectors);
	}
	
	public void setFocus(boolean hasFocus) { model.setFocus(hasFocus); }
	
	public boolean hasFocus() { return model.hasFocus(); }
	
	public void setIsShowing(boolean isShowing) { model.setIsShowing(isShowing); }
	
	public boolean isShowing() { return model.isShowing(); }

	public void setLocation(Point p) { model.setLocation(p);	 }
	
	public Point getLocation() { return model.getLocation(); }
	
	public SpiderMenuModel getModel() { return model; }
	
	public Vector getSpiderZones() { return model.getSpiderZones(); }
	
	public Vector getSpiderSectors() { return model.getSpiderSectors(); }
	
	public void paint(Graphics g)
	{
		model.getView().paint(g);
	}

}
