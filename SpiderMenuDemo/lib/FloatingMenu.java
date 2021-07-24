import java.awt.*;

import javax.swing.*;

public class FloatingMenu extends JComponent
{
	FloatingMenuModel model;
	FloatingMenuView view;

	public FloatingMenu(JMenu original, FloatingComponent parent)
	{
		model = new FloatingMenuModel(this);
		view = new FloatingMenuView(model);
		model.setView(view);
		model.setOriginal(original);
//		model.setParent(parent);
		generateFloatingMenu();
	}

	public void generateFloatingMenu()
	{
		Component[] menuItems = model.getOriginal().getMenuComponents();
		FloatingMenuItem[] children = new FloatingMenuItem[menuItems.length + 1];
		Rectangle[] floatingMenuItemRectangles = new Rectangle[menuItems.length + 1];
		
		int preferredWidth 	= 0;
		int preferredHeight 	= 0;
		int currentWidest 	= 0;
		int currentTallest 	= 0;
		
		for (int i = 0; i < menuItems.length; i++)
		{
			preferredWidth  = menuItems[i].getPreferredSize().width;
			preferredHeight = menuItems[i].getPreferredSize().height;
			if (preferredWidth  > currentWidest ) {currentWidest  = preferredWidth;}
			if (preferredHeight > currentTallest) {currentTallest = preferredHeight;}
		}

		model.setFloatingMenuItemSize(new Dimension(currentWidest, currentTallest));
		String label;
		for (int i = 0; i < menuItems.length + 1; i++)
		{
			if (i == 0)
			{
				model.setTriggerSize(new Dimension(model.getOriginal().getSize()));
				children[i] = new FloatingMenuItem(this);
				children[i].setRectangle(new Rectangle(new Point(0, 0), model.getTriggerSize()));
				children[i].setLabel(model.getOriginal().getText());
				children[i].setTopLevel(true);
				children[i].setBottomLevel(false);
				children[i].setIsShowing(false);
			}
			else if (false)
			{
//				 is a menu separator
//				 include separator component spacer
			}
			else
			{
				children[i] = new FloatingMenuItem(this);
				children[i].setRectangle(new Rectangle(new Point(0, 0), model.getFloatingMenuItemSize()));
				children[i].setLabel(((JMenuItem)menuItems[i - 1]).getText());
				children[i].setTopLevel(false);
				children[i].setBottomLevel(i == menuItems.length ? true : false);
				children[i].setIsShowing(false);
			}
		}
		model.setChildren(children);
		model.setChildWithFocusIndex(0);	
	}

//	public FloatingMenuItem getChildAt(int childIndex) { return floatingMenuItems[childIndex];}
//	
//	public void resetChildrenFocus()
//	{
//		childWithFocus = 0;
//		for (int i = 0; i < floatingMenuItems.length; i++) { floatingMenuItems[i].setFocus(false); }
//	}
//	
//	public int getNumChildren() { return floatingMenuItems.length; }
//	
//	public void setChildWithFocus(int childWithFocus)
//	{
//		floatingMenuItems[this.childWithFocus].setFocus(false);
//		this.childWithFocus = childWithFocus;
//		floatingMenuItems[this.childWithFocus].setFocus(true);
//	}
//	
//	public int getChildWithFocus() { return childWithFocus; }
//	
//	public void incrementFocus()
//	{
//		if (childWithFocus < floatingMenuItems.length - 1)
//		{
//			floatingMenuItems[childWithFocus].setFocus(false);
//			childWithFocus ++;
//			floatingMenuItems[childWithFocus].setFocus(true);
//		}
//	}
//	
//	public void decrementFocus()
//	{
//		if (childWithFocus > 1) 
//		{
//			floatingMenuItems[childWithFocus].setFocus(false);
//			childWithFocus --;
//			floatingMenuItems[childWithFocus].setFocus(true);
//		}
//	}
	
	public void setRectangle(Rectangle r) { model.setBounds(r); }
	
	public Rectangle getRectangle() { return model.getBounds(); }
	
	public void setLocation(Point p)
	{
		model.setLocation(p);
		model.setTriggerLocation(p);
		setChildrenLocation(p);
	}

	private void setChildrenLocation(Point p)
	{
		FloatingMenuItem[] children = model.getChildren();
		for (int i = 0; i < children.length; i++) { children[i].setLocation(new Point(p.x, p.y + (i * model.getFloatingMenuItemSize().height))); }
	}
	
	public Point getLocation() { return model.getLocation(); }

	public boolean contains(Point p)
	{
		if (model.isShowing())
		{
			if (isPointInTrigger(p) || isPointInMenu(p)) 	{ return true; }
			else 										{ return false; }
		}
		else
		{
			if (isPointInTrigger(p)) 	{ return true; }
			else 					{ return false; }
		}
	}
	
	private boolean isPointInTrigger(Point p)
	{
		if (p.x > model.getLocation().x && p.x < model.getTriggerBounds().getMaxX())
		{
			if (p.y > model.getLocation().y && p.y < model.getTriggerBounds().getMaxY()) { return true; }
		}
		return false;
	}
	
	private boolean isPointInMenu(Point p)
	{
		if (p.x > model.getLocation().x && p.x < model.getBounds().getMaxX())
		{
			if (p.y > model.getLocation().y && p.y < model.getBounds().getMaxY()) { return true; }
		}
		return false;
	}

	public void setSize(Dimension d) { model.setSize(d); }
	
	public Dimension getSize() { return model.getSize(); }
	
	public void setFloatingMenuItemSize(Dimension size) { model.setFloatingMenuItemSize(size); }
	
	public Dimension getFloatingMenuItemSize() { return model.getFloatingMenuItemSize(); }
	
	public void setLabel(String label) { model.setLabel(label); }
	
	public String getLabel() { return model.getLabel(); }
	
//	public void setParent(FloatingComponent parent) { model.setParent(parent); }
	
//	public FloatingComponent getParent() { return model.getParent(); }
	
	public void setID(int id) { model.setID(id); }
	
	public int getID() { return model.getID(); }
	
	public void setChildWithFocusIndex(int childWithFocus) { model.setChildWithFocusIndex(childWithFocus); }
	
	public int findChildWithFocus(Point p)
	{
		for (int i = 0; i < model.getChildren().length; i++)
		{
			if (model.getChildren()[i].contains(p))
			{ 
				setChildWithFocusIndex(i);
				return getChildWithFocusIndex();
			}
		}
		return -1;
	}
	
	public int getChildWithFocusIndex() { return model.getChildWithFocusIndex(); }
	
	public void setChildWithFocus(FloatingMenuItem child) { model.setChildWithFocus(child); }
	
	public FloatingMenuItem getChildWithFocus() { return model.getChildWithFocus(); }

	public void setChildren (FloatingMenuItem[] children) { model.setChildren(children); }
	
	public FloatingMenuItem[] getChildren() { return model.getChildren(); }
	
	public void setFocus(boolean hasFocus) { model.setFocus(hasFocus); }
	
	public boolean hasFocus() { return model.hasFocus(); }
	
	public void setIsShowing(boolean isShowing)
	{
		model.setIsShowing(isShowing);
		for (int i = 0; i < model.getChildren().length; i++) { model.getChildren()[i].setIsShowing(isShowing); }
	}
	
	public boolean isShowing() { return model.isShowing(); }

	public void setModel(FloatingMenuModel model) { this.model = model; }
	
	public FloatingMenuModel getModel() { return model; }
	
	public String toString()
	{
		return new String("Floating Menu ," + model.getLocation() + ", rectangle = " + model.getBounds() + ", hasFocus() = " + model.hasFocus());
	}
	
	public void paint(Graphics g)
	{
		model.getView().paint(g);
	}
}