import java.awt.*;
import java.util.*;
import javax.swing.*;

public class FloatingLayoutManager
{
	FloatingComponentModel model;
	SpiderMenuModel spiderMenuModel;
	
	public FloatingLayoutManager(FloatingComponent client)
	{
		model = client.getModel();
	}

	public FloatingLayoutManager(FloatingComponent parent, SpiderMenu client)
	{
		model = parent.getModel();
		spiderMenuModel = client.getModel();
	}
	
	public void doLayout()
	{
		calcUsableArea();
		unifyCoordinateSpace();
		calcFloatingComponentSize();
		calcFloatingComponentLocation();
		setFloatingComponentLocation(model.getLocation());
	}
	
	public void doSpiderLayout()
	{
		// temp fix to height to add buffer b/t vertex and spider menu bottom bound
		Point p = new Point(spiderMenuModel.getSourceEvent().getPoint().x, spiderMenuModel.getSourceEvent().getPoint().y - 30);
		
		calcSpiderComponentLocation(p);
		setSpiderSectorLocations(model.getSpiderMenuLocation());
	}
	
	private void calcUsableArea()
	{
		if (model.getOwner() instanceof JFrame)
		{
			JFrame parent = (JFrame)model.getOwner();
			int frameWidth  = parent.getWidth()  - ( parent.getInsets().left + parent.getInsets().right);
			int frameHeight = parent.getHeight() - ( parent.getInsets().top  + parent.getInsets().bottom);
			model.setUsableParentArea(new Dimension(frameWidth, frameHeight));
		}
	}
	
	private void unifyCoordinateSpace()
	{
		Point newLocation;
		Vector floatingComponentBounds = new Vector();
		Vector staticComponents = model.getStaticComponents();
		int componentWidth  = 0;
		int componentHeight = 0;
		int rowHeight 		= 0;
		for (int i = 0; i < staticComponents.size(); i++)
		{
			Component[] originalComponent = ((Component[])staticComponents.get(i));
			Rectangle[] newBounds		  = new Rectangle[((Component[])staticComponents.get(i)).length];
			for (int j = 0; j < originalComponent.length; j++)
			{
				newLocation = new Point ( originalComponent[j].getLocation().x,  originalComponent[j].getLocation().y + rowHeight);
				componentWidth = originalComponent[j].getWidth();
				componentHeight = originalComponent[j].getHeight();
				newBounds[j] = new Rectangle(newLocation.x, newLocation.y, componentWidth, componentHeight);
			}
			floatingComponentBounds.add(newBounds);
			rowHeight += originalComponent[0].getParent().getHeight();
		}
		model.setComponentBounds(floatingComponentBounds);
	}
	
	public void calcFloatingComponentSize()
	{
		int width 		= 0;
		int height 		= 0;
		int rowWidth 	= 0;
		int rowHeight 	= 0;
		Vector rectangles = model.getComponentBounds();	
		for (int i = 0; i < rectangles.size(); i++)
		{
			Rectangle[] components = ((Rectangle[])rectangles.get(i));
			for (int j = 0; j < components.length; j++)
			{
				rowWidth += components[j].width;
				rowHeight = (rowHeight > components[j].height ? rowHeight : components[j].height);
			}
			width  = (width  > rowWidth  ? width  : rowWidth);
			rowWidth = 0;
			height += rowHeight;
		}
		height += (rectangles.size() > 1 ? FloatingComponent.ZONE_GAP : 0) ;
		model.setWidth(width);
		model.setHeight(height);
	}
	
	public void calcFloatingComponentLocation()
	{
		model.setLocation(new Point(0, 0));
		model.setFloatingComponentBG(new Rectangle(model.getLocation(), new Dimension(model.getWidth(), model.getHeight())));
	}

	public void calcSpiderComponentLocation(Point sourceEvent)
	{
		int locationX, locationY;
		int width = model.getWidth();
		int height = model.getHeight();
		int parentWidth = model.getUsableParentWidth();
		int parentHeight = model.getUsableParentHeight();
		if ((sourceEvent.x + (width / 2)) > (parentWidth - FloatingComponent.EDGE_BUFFER)) { locationX = parentWidth - FloatingComponent.EDGE_BUFFER - width; }
		else
		{
			if ((sourceEvent.x - (width / 2)) < FloatingComponent.EDGE_BUFFER) { locationX = FloatingComponent.EDGE_BUFFER; }
			else { locationX = sourceEvent.x - (width / 2); }
		}
		
		//                            \/ this needs to be 0 for screen w/o persistent menu/toolbars
		if (sourceEvent.y > (height + FloatingComponent.TOP_BUFFER)) { locationY = sourceEvent.y; }
		else
		{
			if ((sourceEvent.y + height) > (parentHeight - FloatingComponent.EDGE_BUFFER)) { locationY = parentHeight - height - FloatingComponent.EDGE_BUFFER; }
			else { locationY = height + FloatingComponent.TOP_BUFFER; } //  + (height / 2)
			//                  /\ this also needs to be 0 for screen w/o persistent menu/toolbars
		}
		model.setSpiderMenuLocation(new Point(locationX, locationY));
		System.out.println(model.getSpiderMenuLocation());
	}
	
	public void setFloatingComponentLocation(Point p)
	{
		Vector rectangles = model.getComponentBounds();
		Vector floatingComponents = model.getFloatingComponents();
		for (int i = 0; i < rectangles.size(); i++)
		{
			Rectangle[] components = ((Rectangle[])rectangles.get(i));
			for (int j = 0; j < components.length; j++)
			{ 
				components[j].translate(p.x, p.y);
				if (i == 0) { ((FloatingMenu[])floatingComponents.get(i))[j].setLocation(components[j].getLocation()); }
				else { ((FloatingButton[])floatingComponents.get(i))[j].setLocation(components[j].getLocation()); }
			}
			rectangles.set(i, components);
		}
	}
	
	public void setSpiderSectorLocations(Point vertex)
	{
		int[] x = new int[4];
		int[] y = new int[4];
		int floatingWidgetMaxX;

		FloatingButton leftFloatingButton  = model.getFloatingButtonAt(0);
		FloatingButton rightFloatingButton = model.getFloatingButtonAt(((FloatingButton[])model.getFloatingComponents().get(1)).length - 1);
		FloatingMenu leftFloatingMenu  = model.getFloatingMenuAt(0);
		FloatingMenu rightFloatingMenu = model.getFloatingMenuAt(((FloatingMenu[])model.getFloatingComponents().get(0)).length - 1);

		int menuZoneMaxX = (int) rightFloatingMenu.getModel().getTriggerBounds().getMaxX();
		int buttonZoneMaxX = rightFloatingButton.p3.x;
		floatingWidgetMaxX = menuZoneMaxX > buttonZoneMaxX ? menuZoneMaxX : buttonZoneMaxX;

		// set zones
		for (int i = 0; i < (spiderMenuModel.getSpiderZones().size()); i++)
		{
			SpiderSector[] zones = ((SpiderSector[])spiderMenuModel.getSpiderZones().get(i));
			for (int j = 0; j < zones.length; j++)
			{
				if (j == 0)
				{
//					FloatingMenu leftFloatingMenu  = model.getFloatingMenuAt(0);
//					FloatingMenu rightFloatingMenu = model.getFloatingMenuAt(((FloatingMenu[])model.getFloatingComponents().get(0)).length - 1);
					x[0] = (int) leftFloatingMenu.getModel().getTriggerBounds().getX();
					y[0] = (int) leftFloatingMenu.getModel().getTriggerBounds().getMaxY();
					x[1] = model.getSpiderMenuLocation().x;
					y[1] = model.getSpiderMenuLocation().y;
					x[3] = floatingWidgetMaxX;
					y[3] = y[0];
					x[2] = x[1] + x[3];
					y[2] = y[1];
				}
				if (j == 1)
				{
//					FloatingButton leftFloatingButton  = model.getFloatingButtonAt(0);
//					FloatingButton rightFloatingButton = model.getFloatingButtonAt(((FloatingButton[])model.getFloatingComponents().get(1)).length - 1);
					x[0] = x[1];
					y[0] = y[1];
					x[3] = x[2];
					y[3] = y[2];
					x[1] = x[0];
					y[1] = y[0] + SpiderMenu.MIN_SECTOR_HEIGHT;
					x[2] = x[3];
					y[2] = y[1];
				}
				zones[j].setP1(new Point(x[0], y[0]));
				zones[j].setP2(new Point(x[1], y[1]));
				zones[j].setP3(new Point(x[2], y[2]));
				zones[j].setP4(new Point(x[3], y[3]));
				zones[j].setPolygon(new Polygon(x, y, x.length));
			}
		}
		
		// set sectors
		for (int i = 0; i < (spiderMenuModel.getSpiderSectors().size()); i++)
		{
			SpiderSector[] sectors = ((SpiderSector[])spiderMenuModel.getSpiderSectors().get(i));
			int sectorWidth = (i == 0 ? floatingWidgetMaxX / sectors.length : floatingWidgetMaxX / sectors.length);
			for (int j = 0; j < sectors.length; j++)
			{
				if (i == 0)
				{
					FloatingMenu floatingMenu  = model.getFloatingMenuAt(j);
					x[0] = (int) floatingMenu.getModel().getTriggerBounds().getX();
					y[0] = (int) floatingMenu.getModel().getTriggerBounds().getMaxY();
					x[1] = model.getSpiderMenuLocation().x + (j * sectorWidth);
					y[1] = model.getSpiderMenuLocation().y;
					x[3] = (j == sectors.length - 1) ? floatingWidgetMaxX : (int) floatingMenu.getModel().getTriggerBounds().getMaxX();
					y[3] = y[0];
					x[2] = x[1] + sectorWidth;
					y[2] = y[1];
				}
				if (i == 1)
				{
					FloatingButton floatingButton  = model.getFloatingButtonAt(j);
					x[0] = floatingButton.p2.x;
					y[0] = floatingButton.p2.y;
					x[1] = model.getSpiderMenuLocation().x + (j * sectorWidth);
					y[1] = model.getSpiderMenuLocation().y + SpiderMenu.MIN_SECTOR_HEIGHT;
					x[3] = floatingButton.p3.x;
					y[3] = y[0];
					x[2] = x[1] + ( x[3] - x[0]);
					y[2] = y[1];
				}
				sectors[j].setP1(new Point(x[0], y[0]));
				sectors[j].setP2(new Point(x[1], y[1]));
				sectors[j].setP3(new Point(x[2], y[2]));
				sectors[j].setP4(new Point(x[3], y[3]));
				sectors[j].setPolygon(new Polygon(x, y, x.length));
			}
		}
		
//		for (int i = 0; i < model.getFloatingComponents().size(); i++)
//		{
//			Rectangle[] tempZones = new Rectangle[((Rectangle[])model.getComponentBounds().get(i)).length];
//			tempZones = ((Rectangle[])model.getComponentBounds().get(i));
//			
//			// set zones
//			if (i == 0)
//			{
//				p1 = new Point(tempZones[0].x , accessibleComponentBottomBoundary);
//				p2 = new Point(baseLeft, vertex.y - (((model.getFloatingComponents().size() - 1) * SpiderMenu.MIN_SECTOR_HEIGHT) + SpiderMenu.VERTEX_ZONE_PADDING));
//				p3 = new Point(baseLeft + model.getWidth(), vertex.y - (((model.getFloatingComponents().size() - 1) * SpiderMenu.MIN_SECTOR_HEIGHT) + SpiderMenu.VERTEX_ZONE_PADDING));
//				p4 = new Point((int)tempZones[tempZones.length - 1].getMaxX(), accessibleComponentBottomBoundary); 
//				
//				int[] x = { p1.x, p2.x, p3.x, p4.x };
//				int[] y = { p1.y, p2.y, p3.y, p4.y };
//				Polygon p = new Polygon(x, y, x.length);
//				((SpiderSector[])spiderMenuModel.getSpiderZones().get(i))[i].setPolygon(p);
//			}
			// set sectors
//			else
//			{
//				p1 = new Point(baseLeft, 			 vertex.y - (((accessibleComponents.size() - i) 		* MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
//				p2 = new Point(baseLeft, 			 vertex.y - (((accessibleComponents.size() - i - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
//				p3 = new Point(baseLeft + baseWidth, vertex.y - (((accessibleComponents.size() - i - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
//				p4 = new Point(baseLeft + baseWidth, vertex.y - (((accessibleComponents.size() - i) 		* MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
//			}

//			zones.add(new SpiderSector(p1, p2, p3, p4, i, this));				

//			((SpiderSector)zones.get(i)).setType(SpiderSector.ZONE);

	}
}