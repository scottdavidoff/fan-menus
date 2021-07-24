
import java.awt.*;
import java.awt.event.*;

// debugging imports, remove to remove warning
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class FloatingComponentController implements MouseListener, MouseMotionListener, MouseWheelListener// , KeyListener
{
	FloatingComponentModel model;
	FloatingComponent owner;
	TelescopingCursor tCursor;
	ExperimentController exController;
	boolean menuMode, buttonMode, cursorMode, nextMode;

	FloatingMenuItem[] menuItems;
	FloatingMenu menuWithFocus;
	FloatingButton buttonWithFocus;
	
	int componentWithFocus, zoneWithFocus, sectorWithFocus, menuUpperBound, menuLowerBound, buttonLowerBound, menuItemWithFocus; //, cursorYAnchor, bridgeHeight;
	Point cursorProxy;
	private static final int NO_FOCUS	= -1;
	private static final int NEXT_BUTTON_FOCUS = 99;
	
	public FloatingComponentController(FloatingComponent owner, FloatingComponentModel model)
	{
		this.owner = owner;
		this.model = model;
		init();
	}

//	public void keyPressed(KeyEvent e) { }
//	public void keyReleased(KeyEvent e) { }
//	public void keyTyped(KeyEvent e)
//	{
//		System.out.println("typed");
//	}

	public void processSelection(MouseEvent e, int button)
	{
		if (model.getSpiderMode())
		{
			if (zoneWithFocus == FloatingComponentController.NO_FOCUS)
			{
				// escape event
				System.out.println("escape event");
				model.getMouseRecorder().recordEvent(e, EventRecord.ESCAPE_EVENT); // escape event click
			}
			else
			{
				model.setRecordingMode(false);
				if (zoneWithFocus == FloatingComponent.FLOATING_MENU)
				{
					model.getMouseRecorder().recordEvent(e, FloatingComponent.FLOATING_MENU, model.getFloatingMenuAt(sectorWithFocus).getChildren()[menuItemWithFocus].getLabel(), EventRecord.SELECTION_EVENT); // model.getFloatingMenuAt(sectorWithFocus).getLabel()
				}
				else if (zoneWithFocus == FloatingComponent.FLOATING_BUTTON)
				{
					model.getMouseRecorder().recordEvent(e, FloatingComponent.FLOATING_BUTTON, model.getFloatingButtonAt(sectorWithFocus).getLabel(), EventRecord.SELECTION_EVENT);
				}
				resetControls();
			}
		}
//		else if (menuMode)
//		{
//		}
//		else if (buttonMode)
//		{
//		}
		else if (nextMode)
		{
			if (exController.areMoreTrials())
			{
				model.setRecordingMode(true);
				model.getMouseRecorder().recordEvent(e, EventRecord.START_EVENT);
				model.setRecordingMode(true);
				model.getNextButton().setDisplayBottomLabels(true);
				model.setNextButtonMode(false);
				model.setTextPromptMode(true);
				if (exController.getTarget().getType() == Target.BUTTON)
				{
					model.getTextPrompt().setIcon(exController.getTarget().getIcon());
					model.getTextPrompt().hasIcon(true);
					model.getTextPrompt().setPrompt("Button: " + exController.getTarget().getText());
					
				}
				else
				{
					model.getTextPrompt().hasIcon(false);
					model.getTextPrompt().setPrompt("Menu: " + exController.getTarget().getParentName() + " > " + exController.getTarget().getText());
				}
				model.getNextButton().setLabel("");
//				model.setNextButtonMode(false);
			}
		}
		init();
	}
	
	public void resetControls()
	{
		exController.saveTrial(model.getMouseRecorder().getRecording());
		long tempTime = exController.getTime();
		model.getMouseRecorder().reset();
		exController.nextTrial();
		if (exController.isRestTime())
		{ ((ControlPanel)((ObjectSelectionStudy)model.getOwner()).getContentPane()).createRestDialog(); }
		if (exController.areMoreTrials())
		{
//			model.setRecordingMode(true);
			model.setNextButtonMode(true);
			model.getNextButton().setDisplayBottomLabels(true);
			model.getNextButton().setTrialsLeft(exController.getTrialNum());
			model.getNextButton().setTime(tempTime);
			model.setTextPromptMode(false);
			model.getTextPrompt().hasIcon(false);
			model.getTextPrompt().setPrompt("");
			model.getNextButton().setLabel("Click Here for Next Target");
		}
		if (exController.isConditionChange())
		{
			((ObjectSelectionStudy)model.getOwner()).getGlassPane().setVisible(false);
		}
		else 
		{
			// end experiment
		}
	}
	
	public void init()
	{
		model.setSpiderMode(false);
		model.setCursorMode(false);
		
		menuMode 	 	  = false;
		buttonMode 	 	  = false;
		nextMode			  = false;
		componentWithFocus = NO_FOCUS;
		zoneWithFocus 	  = NO_FOCUS;
		sectorWithFocus 	  = NO_FOCUS;
		menuItemWithFocus  = NO_FOCUS;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			processSelection(e, MouseEvent.BUTTON1);
		}
		else if (e.getButton() == MouseEvent.BUTTON2)
		{
			if (model.getSpiderMode())
			{ 
				processSelection(e, MouseEvent.BUTTON2);
			}	
			else
			{
				owner.instantiateSpiderComponents(e);
				model.setSpiderMode(true);
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			// TODO
			// what to do w/ mouse button 3?
		}
		model.getParent().repaint();
	}
	
	public void mouseMoved(MouseEvent e)
	{
//		Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
//		((ControlPanel)((ObjectSelectionStudy)model.getOwner()).getContentPane()).list();
//		System.out.println(focusOwner);
//		System.out.println(focusOwner.getLocation());
//		System.out.println("--");

//		if (model.isRecordingMode()) { model.getMouseRecorder().recordEvent(e, EventRecord.MOTION_EVENT); }
		if (menuMode)
		{
			boolean menuHasFocus = false;
			for (int k = 0; k < menuItems.length; k++)
			{
				if (menuItems[k].contains(e.getPoint()))
				{ 
					menuItems[k].setFocus(true);
					menuHasFocus = true;
				}
				else {  menuItems[k].setFocus(false); }
			}
			if (!menuHasFocus)
			{
				menuWithFocus.setIsShowing(false);
				for (int k = 0; k < menuItems.length; k++) { menuItems[k].setIsShowing(false); }
				menuMode = false;
				componentWithFocus = NO_FOCUS;
				focusCheck(e);
			}
		}
		else { focusCheck(e); }
		model.getParent().repaint();
	}
	
	public void focusCheck(MouseEvent e)
	{
		if (model.getNextButton().contains(e.getPoint()))
		{ 
			model.getNextButton().setFocus(true);
			nextMode = true;
		}
		else
		{
			model.getNextButton().setFocus(false);
			nextMode = false;
		}
		
		for (int i = 0; i < model.getFloatingComponents().size(); i++)
		{
			for (int j = 0; j < ((Object[])model.getFloatingComponents().get(i)).length; j++)
			{
				if (i == FloatingComponent.FLOATING_MENU)
				{
					if (((FloatingMenu[])model.getFloatingComponents().get(i))[j].contains(e.getPoint()))
					{
						menuWithFocus = ((FloatingMenu[])model.getFloatingComponents().get(i))[j];
						menuWithFocus.setIsShowing(true);
						menuItems = menuWithFocus.getChildren();
						for (int k = 0; k < menuItems.length; k++) { menuItems[k].setIsShowing(true); }
						componentWithFocus = j;
						menuMode = true;
					}
					else { ((FloatingMenu[])model.getFloatingComponents().get(i))[j].setIsShowing(false); }
				}
				else if (i == FloatingComponent.FLOATING_BUTTON)
				{
					buttonMode = false;
					if (((FloatingButton[])model.getFloatingComponents().get(i))[j].contains(e.getPoint()))
					{
						buttonWithFocus = ((FloatingButton[])model.getFloatingComponents().get(i))[j];
						buttonWithFocus.setFocus(true);
						componentWithFocus = j;
						buttonMode = true;
					}
					else { ((FloatingButton[])model.getFloatingComponents().get(i))[j].setFocus(false); }
				}
			}
		}
		
		if (model.getSpiderMode())
		{
			zoneWithFocus = NO_FOCUS;
			boolean zoneTieBreaker = false;
			SpiderSector[] zones = ((SpiderSector[])model.getSpiderMenu().getSpiderZones().get(0));
			for (int i = 0; i < zones.length; i++)
			{
				if (zones[i].contains(e.getPoint()) && zoneTieBreaker == false)
				{
					zones[i].setFocus(true);
					zoneWithFocus = i;
					zoneTieBreaker = true;
				}
				else { zones[i].setFocus(false); }
			}
			
			if (zoneWithFocus != NO_FOCUS)
			{
				boolean sectorTieBreaker = false;
				int oldSectorWithFocus = sectorWithFocus;
				for (int i = 0; i < model.getSpiderMenu().getSpiderSectors().size(); i++)
				{
					SpiderSector[] sectors = ((SpiderSector[])model.getSpiderMenu().getSpiderSectors().get(i));
					for (int j = 0; j < sectors.length; j++)
					{
						if (i == zoneWithFocus)
						{
							if (zoneWithFocus == FloatingComponent.FLOATING_MENU)
							{
								if (sectors[j].contains(e.getPoint()) && sectorTieBreaker == false)
								{
									sectorWithFocus = j;
									sectorTieBreaker = true;
									sectors[j].setFocus(true);
									model.getFloatingMenuAt(sectorWithFocus).setIsShowing(true);
									if (oldSectorWithFocus == sectorWithFocus) { adjustTelescopingCursor(e); }
									else { createTelescopingCursor(e); }
									model.setCursorMode(true);
								}
								else
								{
									sectors[j].setFocus(false);
									model.getFloatingMenuAt(j).setIsShowing(false);
								}
							}
							else if (zoneWithFocus == FloatingComponent.FLOATING_BUTTON)
							{
								model.setCursorMode(true);
								if (sectors[j].contains(e.getPoint()) && sectorTieBreaker == false)
								{
									sectorWithFocus = j;
									sectorTieBreaker = true;
									sectors[j].setFocus(true);
									model.getFloatingButtonAt(sectorWithFocus).setFocus(true);
									
									// ***
									if (oldSectorWithFocus == sectorWithFocus) { adjustTelescopingCursor(e); }
									else { createTelescopingCursor(e); }
									model.setCursorMode(true);
								}
								else
								{
									sectors[j].setFocus(false);
									model.getFloatingButtonAt(j).setFocus(false);
								}
							}
						}
						else { sectors[j].setFocus(false); }
					}
				}
			}
			else
			{ 
				model.setCursorMode(false);
				sectorWithFocus = NO_FOCUS;
			}
		}
	}
	
	public void createTelescopingCursor(MouseEvent e)
	{
		if (zoneWithFocus == FloatingComponent.FLOATING_MENU)
		{
			FloatingMenuItem[] menu = model.getFloatingMenuAt(sectorWithFocus).getChildren();
			FloatingMenuItem bottom = menu[menu.length - 1];
			menuUpperBound = bottom.getModel().getP2().y;
			menuLowerBound = menu[1].getLocation().y;
			model.setTelescopingCursor(new TelescopingCursor(e.getPoint(), bottom.getModel().getP2(), bottom.getModel().getP3()));
			model.getTelescopingCursor().setProxyUpperBound(menuUpperBound);
			model.getTelescopingCursor().setProxyLowerBound(menuLowerBound);
			checkProxyFocus();
		}
		else if (zoneWithFocus == FloatingComponent.FLOATING_BUTTON)
		{
			FloatingButton fb = model.getFloatingButtonAt(sectorWithFocus);
//			FloatingMenuItem bottom = menu[menu.length - 1];
//			menuUpperBound = fb.getModel().getP2().y;
			buttonLowerBound = fb.p2.y;
			model.setTelescopingCursor(new TelescopingCursor(e.getPoint(), fb.p2, fb.p3));
//			model.getTelescopingCursor().setProxyUpperBound(menuUpperBound);
			model.getTelescopingCursor().setProxyLowerBound(buttonLowerBound);
		}
	}
	
	public void adjustTelescopingCursor(MouseEvent e)
	{
		if (zoneWithFocus == FloatingComponent.FLOATING_MENU)
		{
			if (e.getPoint().y < menuUpperBound)
			{
				model.setSpiderMode(false);
				model.setCursorMode(false);
				menuWithFocus = model.getFloatingMenuAt(sectorWithFocus);
				menuItems = menuWithFocus.getChildren();
				menuMode = true;
			}
			else
			{
				model.getTelescopingCursor().updateTriangle(e.getPoint());
				checkProxyFocus();
			}
		}
		else if (zoneWithFocus == FloatingComponent.FLOATING_BUTTON)
		{
			model.getTelescopingCursor().updateVertex(e.getPoint());
		}
	}
	
	public void checkProxyFocus()
	{
		FloatingMenuItem[] menuItems = model.getFloatingMenuAt(sectorWithFocus).getChildren();
		Point proxyPoint = model.getTelescopingCursor().getProxyPoint();
		for (int i = 0; i < menuItems.length; i++) 
		{
			menuItems[i].setIsShowing(true);
			menuItems[i].setFocus(menuItems[i].contains(proxyPoint));
			if (menuItems[i].contains(proxyPoint)) { menuItemWithFocus = i; }
		}
	}
	
	public void setExperimentController(ExperimentController ec) { this.exController = ec; }
	
	public void mouseWheelMoved(MouseWheelEvent e) { }
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}