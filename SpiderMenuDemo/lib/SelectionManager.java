import java.util.Vector;

public class SelectionManager
{
	FloatingComponentModel model;

	boolean hasFocus;
	int componentWithFocus, subComponentWithFocus, selectedType;
	
	private static final int NO_FOCUS	= -1;
	
	public SelectionManager(FloatingComponentModel model)
	{
		this.model = model;
		hasFocus = false;
		componentWithFocus			= NO_FOCUS;
		subComponentWithFocus 		= NO_FOCUS;
		selectedType 				= NO_FOCUS;
	}
	
	public boolean isNewSelection(int selected)
	{
		if (selected == componentWithFocus) { return false; }
		else { return true; }
	}
	
	public void processSelection(int selectedType, int newSelection)
	{
		if (selectedType == FloatingComponent.FLOATING_MENU)
		{
			// if no component was selected or the same component is selected
			if (componentWithFocus == NO_FOCUS) { componentWithFocus = newSelection; }
			
			if (componentWithFocus == newSelection)
			{
				// don't reset any components
				System.out.println("no focus change");
			}
			else
			{
				System.out.println("SelectionManager.processSelection(" + newSelection + ");");
				// deselect the old component
				((FloatingMenu[])model.getFloatingComponents().get(0))[componentWithFocus].setIsShowing(false);
				// select the new component
				componentWithFocus = newSelection;
				((FloatingMenu[])model.getFloatingComponents().get(0))[newSelection].setIsShowing(true);
			}
			
		}
		
	}
	
	public void registerComponents(Vector components)
	{
		
	}

}
