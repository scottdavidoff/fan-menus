import javax.swing.*;

public class SampleApplicationMenuBar extends JMenuBar
{

	// TODO
	// change menu contents:  be sure to add listeners to new menus via addListeners()
	ControlPanel controller;
	JMenuItem newItem, openItem, closeItem, saveItem, saveAsItem, printItem,
			  undoItem, redoItem, cutItem, copyItem, pasteItem, selectAllItem, findItem, replaceItem, gotoItem,
			  zoomItem, normalItem, fullScreenItem, aboutItem, downloadUpdatesItem;
	
	public SampleApplicationMenuBar()
	{
		super();
		instantiateMenuItems(false);		
	}

	public SampleApplicationMenuBar(ControlPanel controller)
	{
		super();
		this.controller = controller;
		instantiateMenuItems(true);
	}
	
	public void instantiateMenuItems(boolean hasListener)
	{
		// file menu
		JMenu fileMenu = new JMenu("File");
		
		newItem = new JMenuItem("New");
		openItem = new JMenuItem("Open");
		closeItem = new JMenuItem("Close");
		saveItem = new JMenuItem("Save");
//		saveAsItem = new JMenuItem("Save As...");
		printItem = new JMenuItem("Print");
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(closeItem);
//		fileMenu.addSeparator();
		fileMenu.add(saveItem);
//		fileMenu.add(saveAsItem);
		fileMenu.add(printItem);
		
		// edit menu
		JMenu editMenu = new JMenu("Edit");

		cutItem = new JMenuItem("Cut");
		copyItem = new JMenuItem("Copy");
		pasteItem = new JMenuItem("Paste");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");

		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(undoItem);
		editMenu.add(redoItem);
//		editMenu.addSeparator();
//		editMenu.addSeparator();

		// view menu
		JMenu viewMenu = new JMenu("View");

		zoomItem = new JMenuItem("Zoom");
		normalItem = new JMenuItem("Normal");
		fullScreenItem = new JMenuItem("Full Screen");

		viewMenu.add(normalItem);
		viewMenu.add(fullScreenItem);
		viewMenu.add(zoomItem);

		// toolsmenu
		JMenu toolsMenu = new JMenu("Tools");
		
		findItem = new JMenuItem("Find");
		replaceItem = new JMenuItem("Replace");
		gotoItem = new JMenuItem("Goto");

		toolsMenu.add(findItem);
		toolsMenu.add(replaceItem);
		toolsMenu.add(gotoItem);

//		// actions menu
//		JMenu actionsMenu = new JMenu("Actions");
//
//		test7 = new JMenuItem("test7");
//		test8 = new JMenuItem("test8");
//
//		actionsMenu.add(test7);
//		actionsMenu.add(test8);
//
		// help menu
		JMenu helpMenu = new JMenu("Help");

		aboutItem = new JMenuItem("About This Study");
		downloadUpdatesItem= new JMenuItem("Download Updates");

		helpMenu.add(aboutItem);
		helpMenu.add(downloadUpdatesItem);

		add(fileMenu);
		add(editMenu);
		add(viewMenu);
//		add(favoritesMenu);
		add(toolsMenu);
//		add(actionsMenu);
		add(helpMenu);
		
		if (hasListener) { addListeners(); }
	}
	
	public void addListeners()
	{
		newItem.addActionListener(controller);
		openItem.addActionListener(controller);
		closeItem.addActionListener(controller);
		saveItem.addActionListener(controller);
		printItem.addActionListener(controller);
		
		cutItem.addActionListener(controller);
		copyItem.addActionListener(controller);
		pasteItem.addActionListener(controller);
		undoItem.addActionListener(controller);
		redoItem.addActionListener(controller);
		
		normalItem.addActionListener(controller);
		zoomItem.addActionListener(controller);
		fullScreenItem.addActionListener(controller);
		
		findItem.addActionListener(controller);
		replaceItem.addActionListener(controller);
		gotoItem.addActionListener(controller);
		
		aboutItem.addActionListener(controller);
		downloadUpdatesItem.addActionListener(controller);
	}
}
