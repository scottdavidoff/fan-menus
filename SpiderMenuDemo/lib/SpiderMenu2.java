import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class SpiderMenu2 extends JComponent // Container
{
	private static final int MIN_SECTOR_WIDTH 			= 30;
	private static final int MIN_SECTOR_HEIGHT			= 30;
	private static final int EDGE_BUFFER				= 20;
	private static final int VERTEX_ZONE_PADDING 		=  5;
	private static final int NEXT_BUTTON_MASK_PAD_X	= 100;
	private static final int NEXT_BUTTON_MASK_PAD_Y	= 10;
	
	private static final int ESCAPE_EVENT				=  1;
	private static final int MENU_ITEM_SELECTION_EVENT	=  2;
	private static final int BUTTON_SELECTION_EVENT	=  3;

	Vector accessibleComponents, sectors, zones, componentRectangles;
	FloatingMenu2[] 	 floatingMenus;
	FloatingButton2[] floatingButtons;
	ControlPanel controlPanel;
	Rectangle nextButtonMask;
	
	Point p1, p2, p3, p4;
	Point vertex;
	JFrame parent;
	
	boolean hasFocus, spiderMode, selectionMode, telescopeMode;

	int baseWidth, baseLeft, accessibleComponentBottomBoundary,
		frameWidth, frameHeight, parentHeight,
		sectorWithFocus, zoneWithFocus;
	
	long startTime;

	public SpiderMenu2(Point vertex, Component parent, Vector accessibleComponents, MouseEvent e, ControlPanel controlPanel)
	{
		super();
		setLayout(new BorderLayout());
		
		if (parent instanceof JFrame)
		{
			this.parent = (JFrame)parent;
			frameWidth  = parent.getWidth()  - ( ((JFrame)parent).getInsets().left + ((JFrame)parent).getInsets().right);
			frameHeight = parent.getHeight() - ( ((JFrame)parent).getInsets().top  + ((JFrame)parent).getInsets().bottom);
		}
		this.accessibleComponents = new Vector(accessibleComponents);
		this.vertex = vertex;
		this.controlPanel = controlPanel;
		
//		spiderMode 		= true;
		spiderMode 		= false;
		selectionMode 	= false;
		telescopeMode 	= false;
//		sectorWithFocus = SpiderMenu.N0_SECTOR_FOCUS;

		convertToSingleCoordSpace();

		calcBaseWidth();
		calcBaseLeft();
		calcZonePoints();
		calcSectorPoints();
		addFloatingComponents();
//		addNextButtonMask();

		checkZones(this.vertex);
		addListeners();
		repaint();
		
		startTime = e.getWhen();
	}

	public void convertToSingleCoordSpace()
	{
		Point glassPaneCoord;

		componentRectangles = new Vector();
		
		int componentWidth  = 0;
		int componentHeight = 0;
		int rowHeight 		= 0;

		for (int i = 0; i < accessibleComponents.size(); i++)
		{
			Rectangle[] temp = new Rectangle[((Component[])accessibleComponents.get(i)).length];

			for (int j = 0; j < ((Component[])accessibleComponents.get(i)).length; j++)
			{
				glassPaneCoord = new Point ( ((Component[])accessibleComponents.get(i))[j].getLocation().x,  ((Component[])accessibleComponents.get(i))[j].getLocation().y + rowHeight);
				componentWidth = ((Component[])accessibleComponents.get(i))[j].getWidth();
				componentHeight = ((Component[])accessibleComponents.get(i))[j].getHeight();

				temp[j] = new Rectangle(glassPaneCoord.x, glassPaneCoord.y, componentWidth, componentHeight);

//				debug
//				System.out.println("componentRectangle[" + i + "][" + j + "] = " + temp[j].toString());
			}
			componentRectangles.add(temp);
			rowHeight += componentHeight;
		}
	}

	public void calcBaseWidth()
	{
		int currentLongestIndex  = 0;
		int currentLongestLength = 0;
		for (int i = 0; i < accessibleComponents.size(); i++)
		{
			if ( ( (Object [])accessibleComponents.get(i) ).length > currentLongestLength )
			{
				currentLongestLength = ( (Object [])accessibleComponents.get(i) ).length;
				currentLongestIndex  = i;
			}
		}
		baseWidth = MIN_SECTOR_WIDTH * currentLongestLength;
	}
	
	public void calcBaseLeft()
	{
		if (vertex.x + (baseWidth / 2) > frameWidth) { baseLeft = (frameWidth - SpiderMenu2.EDGE_BUFFER) - baseWidth; }
		else
		{
			if (vertex.x - (baseWidth / 2) < 0) { baseLeft = SpiderMenu2.EDGE_BUFFER; }
			else { baseLeft = vertex.x - (baseWidth / 2); }
		}
	}
	
	public void calcZonePoints()
	{

		zones = new Vector();
		accessibleComponentBottomBoundary = (int) ((Rectangle[])componentRectangles.get(1))[0].getMaxY();

		for (int i = 0; i < accessibleComponents.size(); i++)
		{
			Rectangle[] tempZones = new Rectangle[((Rectangle[])componentRectangles.get(i)).length];
			tempZones = ((Rectangle[])componentRectangles.get(i));
			
			if (i == 0)
			{
				p1 = new Point(tempZones[0].x , accessibleComponentBottomBoundary);
				p2 = new Point(baseLeft, 			 vertex.y - (((accessibleComponents.size() - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
				p3 = new Point(baseLeft + baseWidth, vertex.y - (((accessibleComponents.size() - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
				p4 = new Point((int)tempZones[tempZones.length - 1].getMaxX(), accessibleComponentBottomBoundary); 
			}
			else
			{
				p1 = new Point(baseLeft, 			 vertex.y - (((accessibleComponents.size() - i) 		* MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
				p2 = new Point(baseLeft, 			 vertex.y - (((accessibleComponents.size() - i - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
				p3 = new Point(baseLeft + baseWidth, vertex.y - (((accessibleComponents.size() - i - 1) * MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
				p4 = new Point(baseLeft + baseWidth, vertex.y - (((accessibleComponents.size() - i) 		* MIN_SECTOR_HEIGHT) + VERTEX_ZONE_PADDING));
			}
			zones.add(new SpiderSector2(p1, p2, p3, p4, i, this));				
			((SpiderSector2)zones.get(i)).setType(SpiderSector2.ZONE);

// 			debug
//			System.out.println("zone[" + i + "] = " + tempZones[i]);
		}
	}

	public void calcSectorPoints()
	{
		sectors = new Vector();

		SpiderSector2[] tempSectorContainer;
		
		for (int i = 0; i < accessibleComponents.size(); i++)
		{
			tempSectorContainer = new SpiderSector2[((Rectangle[])componentRectangles.get(i)).length];
			
			Rectangle[] tempSectors = new Rectangle[((Rectangle[])componentRectangles.get(i)).length];
			tempSectors = ((Rectangle[])componentRectangles.get(i));

			double sectorWidth = baseWidth / tempSectorContainer.length ;
			
			for (int j = 0; j < tempSectorContainer.length ; j++)
			{
				p1 = new Point(tempSectors[j].x , (int) tempSectors[j].getMaxY());
				p2 = new Point( (int)(((SpiderSector2)zones.get(i)).p2.x + (j * sectorWidth)) , ((SpiderSector2)zones.get(i)).p2.y);
				p3 = new Point( (int)(((SpiderSector2)zones.get(i)).p2.x + ((j + 1) * sectorWidth)) , ((SpiderSector2)zones.get(i)).p2.y);
				p4 = new Point( (int)tempSectors[j].getMaxX(), (int) tempSectors[j].getMaxY()); 

				tempSectorContainer[j] = new SpiderSector2(p1, p2, p3, p4, i, this);
				tempSectorContainer[j].setType(SpiderSector2.SECTOR);

				// debug
//				System.out.println("sector[" + i + "][" + j + "] = " + tempSectorContainer[j]);
			}
			sectors.add(tempSectorContainer);
		}
	}

	public void buildSpiderMenu(MouseEvent e)
	{
		// if in spiderMode
		if (spiderMode)
		{
			// if not in selection mode, activate selection mode and hide sectors and zones
			if (!selectionMode)
			{
				// if the spider menu has focus, turn selectionMode on
				if (hasFocus)
				{
					if (zoneWithFocus == 1)
					{
						selectionMode = false;
						spiderMode 	  = false;
						hasFocus		  = false;

						processSelection(e, SpiderMenu2.BUTTON_SELECTION_EVENT);
					}
					else
					{
						selectionMode = true;
					}
					hideSectorsAndZones();
						
				}
				// otherwise, hide the spiderMenu
				else
				{
					selectionMode = false;
					spiderMode 	  = false;
					hasFocus		  = false;

					processSelection(e, SpiderMenu2.ESCAPE_EVENT);
					hideSectorsAndZones();
				}
			}
			// if in selection mode, turn off selectionMode and spiderMode
			else
			{
				selectionMode = false;
				spiderMode 	  = false;
				hasFocus		  = false;

				if (e.getButton() == MouseEvent.BUTTON2) { processSelection(e, SpiderMenu2.MENU_ITEM_SELECTION_EVENT); }
				else if (floatingMenus[sectorWithFocus].contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1)
					 { processSelection(e, SpiderMenu2.MENU_ITEM_SELECTION_EVENT); }
				else { processSelection(e, SpiderMenu2.ESCAPE_EVENT); }

				hideSectorsAndZones();
				resetFloatingComponentIndicies();
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON2)
		{
			spiderMode = true;
			
			vertex = e.getPoint();
			startTime = e.getWhen();
			
			spiderMode 		= true;
			selectionMode 	= false;
			telescopeMode 	= false;

			calcBaseLeft();
			calcZonePoints();
			calcSectorPoints();
			setFloatingButtonLocations();

			checkZones(vertex);
		}
			
		SpiderMenu2.this.repaint();
	}
	
	public void addListeners()
	{
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {}
			public void mouseMoved(MouseEvent e)
			{
				if (spiderMode)
				{
					if (selectionMode) { checkFloatingComponents(e.getPoint()); }
					else
					{
						checkZones(e.getPoint());
						if (hasFocus)
						{ 
							if (zoneWithFocus == 0) 	{ floatingMenus[sectorWithFocus].setLocation(e.getPoint()); }
						}
					}
					SpiderMenu2.this.repaint();
				}
			}
		});

		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e)
			{
				buildSpiderMenu(e);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				if (spiderMode && !selectionMode)
				{
					selectionMode = true;
					hideSectorsAndZones();
				}

				if (hasFocus || selectionMode)
				{
					if (zoneWithFocus == 0)
					{
						if 		(e.getWheelRotation() > 0) { floatingMenus[sectorWithFocus].incrementFocus(); }
						else if (e.getWheelRotation() < 0) { floatingMenus[sectorWithFocus].decrementFocus(); }
					}
				}
				SpiderMenu2.this.repaint();
			}
		});

		// TODO
		// add keystroke-based logic
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e)
			{
				System.out.println("key Typed");
				System.out.println("keyCode = " + e.getKeyCode());
				System.out.println("keyChar = " + e.getKeyChar());
				System.out.println("----------------------------");
			}
			
			public void keyPressed(KeyEvent e)
			{
				System.out.println("key Pressed");
				System.out.println("keyCode = " + e.getKeyCode());
				System.out.println("keyChar = " + e.getKeyChar());
				System.out.println("----------------------------");
			}
			public void keyReleased(KeyEvent e)
			{
				System.out.println("key Released");
				System.out.println("keyCode = " + e.getKeyCode());
				System.out.println("keyChar = " + e.getKeyChar());
				System.out.println("----------------------------");

				// 27 == 'ESC';
				if (e.getKeyCode() == 27)
				{
					selectionMode = false;
					spiderMode 	  = false;
					hasFocus 	  = false;

					hideSectorsAndZones();
				}
			}
		});
	}

	public void hideSectorsAndZones()
	{
		sectors.clear();
		zones.clear();
	}
	
	public void processSelection(MouseEvent e, int selectionType)
	{
		switch (selectionType)
		{
			case SpiderMenu2.ESCAPE_EVENT:
				System.out.println("escape sequence");
				break;
			case SpiderMenu2.MENU_ITEM_SELECTION_EVENT:
				System.out.println("selectedMenu: " + floatingMenus[sectorWithFocus].getChildAt(floatingMenus[sectorWithFocus].childWithFocus).getLabel());
				controlPanel.floatingSelection(FloatingComponent.FLOATING_MENU, e, floatingMenus[sectorWithFocus].getChildAt(floatingMenus[sectorWithFocus].childWithFocus).getLabel()); // Label()
				break;
			case SpiderMenu2.BUTTON_SELECTION_EVENT:
				System.out.println("selectedButton: " + floatingButtons[sectorWithFocus].original.getToolTipText());
				controlPanel.floatingSelection(FloatingComponent.FLOATING_BUTTON, e, floatingButtons[sectorWithFocus].original.getToolTipText());
			break;
		}
	}

	public void addFloatingComponents()
	{
		for (int i = 0; i < accessibleComponents.size(); i++)
		{
			if (i == 0)
			{
				Component[] components = new Component[((Component[])accessibleComponents.get(i)).length];
				components = ((Component[])accessibleComponents.get(i));

				floatingMenus = new FloatingMenu2[components.length];
				for (int j = 0; j < components.length; j++)
				{
					floatingMenus[j] = new FloatingMenu2((JMenu)components[j]);
					floatingMenus[j].setVisible(false);
					floatingMenus[j].setID(j);
				}
			}
			else if (i == 1)
			{
				Component[] components = new Component[((Component[])accessibleComponents.get(i)).length];
				components = ((Component[])accessibleComponents.get(i));
				
				floatingButtons = new FloatingButton2[components.length];
				for (int j = 0; j < components.length; j++)
				{
					floatingButtons[j] = new FloatingButton2((JButton)components[j]);
					floatingButtons[j].setVisible(false);
					floatingButtons[j].setID(j);
				}
				setFloatingButtonLocations();
			}

		}
	}
	
	public void setFloatingButtonLocations()
	{
		for (int j = 0; j < floatingButtons.length; j++)
		{
			int zoneMidX = ((SpiderSector2[])sectors.get(1))[j].p2.x + (SpiderMenu2.MIN_SECTOR_WIDTH  / 2);
			int zoneMidY = ((SpiderSector2[])sectors.get(1))[j].p2.y - (SpiderMenu2.MIN_SECTOR_HEIGHT / 2);
			int buttonX = zoneMidX - floatingButtons[j].getWidth()  / 2 ;
			int buttonY = zoneMidY - floatingButtons[j].getHeight() / 2 ;
		
			floatingButtons[j].setLocation(new Point(buttonX, buttonY));
		}
	}
	
	public void resetFloatingComponentIndicies()
	{
		for (int i = 0; i < floatingMenus.length; i++) { floatingMenus[i].resetChildrenFocus(); }
	}
	
	public void checkZones(Point p)
	{
		SpiderSector2[] activeZones = new SpiderSector2[zones.size()];
		for (int i = 0 ; i < zones.size(); i ++) { activeZones[i] = ((SpiderSector2)zones.get(i)); }

		boolean hadFocus = hasFocus;

		for (int i = 0; i < zones.size(); i++)
		{
			if (hitTest(p.x, parentHeight - p.y, activeZones[i].p1.x, parentHeight - activeZones[i].p1.y, activeZones[i].p2.x, parentHeight - activeZones[i].p2.y, activeZones[i].p4.x, parentHeight - activeZones[i].p4.y)
		    ||  hitTest(p.x, parentHeight - p.y, activeZones[i].p4.x, parentHeight - activeZones[i].p4.y, activeZones[i].p2.x, parentHeight - activeZones[i].p2.y, activeZones[i].p3.x, parentHeight - activeZones[i].p3.y))
			{
				hasFocus = true;
				((SpiderSector2)zones.get(zoneWithFocus)).setFocus(false);

				zoneWithFocus = i;
				((SpiderSector2)zones.get(zoneWithFocus)).setFocus(true);
				checkSectors(p);
				return;
			}
			((SpiderSector2)zones.get(zoneWithFocus)).setFocus(true);
		}
		
		for (int i = 0; i < zones.size(); i ++) { ((SpiderSector2)zones.get(i)).setFocus(false); }
		hasFocus = false;
	}
	
	public void checkSectors(Point p)
	{
		int sectorHadFocus = sectorWithFocus;
		
		SpiderSector2[] activeSectors = ((SpiderSector2[])sectors.get(zoneWithFocus));
		for (int i = 0; i < activeSectors.length; i++)
		{
			if (hitTest(p.x, parentHeight - p.y, activeSectors[i].p1.x, parentHeight - activeSectors[i].p1.y, activeSectors[i].p2.x, parentHeight - activeSectors[i].p2.y, activeSectors[i].p4.x, parentHeight - activeSectors[i].p4.y)
			||  hitTest(p.x, parentHeight - p.y, activeSectors[i].p4.x, parentHeight - activeSectors[i].p4.y, activeSectors[i].p2.x, parentHeight - activeSectors[i].p2.y, activeSectors[i].p3.x, parentHeight - activeSectors[i].p3.y))
			{
//				System.out.println("checkSectors() hitTest() == true:  sector " + i);
				sectorWithFocus = i;
				((SpiderSector2[])sectors.get(zoneWithFocus))[sectorWithFocus].setFocus(true);
				if (zoneWithFocus == 1) { floatingButtons[i].setFocus(true); }
			}
			else
			{
				((SpiderSector2[])sectors.get(zoneWithFocus))[i].setFocus(false);
				if (zoneWithFocus == 1) 	{ floatingButtons[i].setFocus(false); }
//				System.out.println("checkSectors() hitTest() == false:  sector " + i);
			}
		}
//		System.out.println("------------------------------");
	}

	public void checkFloatingComponents(Point p)
	{
		if (floatingMenus[sectorWithFocus].contains(p))
		{
			floatingMenus[sectorWithFocus].setFocus(true);
			for (int i = 0; i < floatingMenus[sectorWithFocus].getNumChildren(); i++)
			{
				if (floatingMenus[sectorWithFocus].getChildAt(i).contains(p))
				{
					floatingMenus[sectorWithFocus].getChildAt(i).setFocus(true);
					floatingMenus[sectorWithFocus].setChildWithFocus(i);
				}
//				else { floatingMenus[sectorWithFocus].getChildAt(i).setFocus(false); }
			}
		} else { floatingMenus[sectorWithFocus].setFocus(false);}
	}

	public boolean hitTest(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3)
	{
		float b0,
			  b1,
			  b2,
			  b3;

		b0 =  (x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1);
		b1 = ((x2 - x0) * (y3 - y0) - (x3 - x0) * (y2 - y0)) / b0;
		b2 = ((x3 - x0) * (y1 - y0) - (x1 - x0) * (y3 - y0)) / b0;
		b3 = ((x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0)) / b0;

		if ((b0 > 0) && (b1 > 0) && (b2 > 0) && (b3> 0)) { return true; }
		else 											 { return false; }
	}

	public Point calcPointOnLine(Point p1, Point p2, String unknownDimension, int knownDimension)
	{
		// y = mx + b;
		double m = ((frameHeight - p2.y) - (frameHeight - p1.y)) / (p2.x - p1.x);
		double b = (frameHeight - p1.y) - (m * p1.x);
		
		// solve for x
		if (unknownDimension.equalsIgnoreCase("x"))
		{
			// y = mx + b
			// mx = y - b
			// x = (y - b) / m
			int x = (int) (((frameHeight - knownDimension) - b) / m);
			return new Point(x, knownDimension);
		}

		// solve for y
		else if (unknownDimension.equalsIgnoreCase("y"))
		{ return new Point(); }
		else
		{
			System.out.println("calcPointOnLine() error");
			return new Point();
		}
	}
	
	public void addNextButtonMask()
	{
		nextButtonMask = new Rectangle(
			new Point(frameWidth/2 - controlPanel.bottomButton.getWidth()/2 - NEXT_BUTTON_MASK_PAD_X/2, FloatingComponent.NEXT_BUTTON_HEIGHT - NEXT_BUTTON_MASK_PAD_Y/2),
			new Dimension(controlPanel.bottomButton.getWidth() + NEXT_BUTTON_MASK_PAD_X, controlPanel.bottomButton.getHeight() + NEXT_BUTTON_MASK_PAD_Y)
			);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
//		g2d.setColor(Color.WHITE);
//		g2d.fill(nextButtonMask);
		
		if (spiderMode && hasFocus && !selectionMode)
		{
			for (int i = 0; i < ((SpiderSector2[])sectors.get(zoneWithFocus)).length; i++)
			{
				((SpiderSector2[])sectors.get(zoneWithFocus))[i].draw(g2d);
			}
		}

		if (spiderMode && !selectionMode)
		{
			for (int i = 0; i < zones.size(); i++) { ((SpiderSector2)zones.get(i)).draw(g2d); }
		}
		
		if (hasFocus || selectionMode)
		{
			if 		(zoneWithFocus == 0) 	{ floatingMenus[sectorWithFocus].draw(g2d);   }
			else if (zoneWithFocus == 1)
			{
				for (int i = 0; i < floatingButtons.length; i++) { floatingButtons[i].draw(g2d); }
			}
		}
	}
}
