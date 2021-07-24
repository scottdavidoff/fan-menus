import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ControlPanel extends JPanel implements ActionListener, KeyListener
{
	public void keyPressed(KeyEvent e)
	{
//        int modifiers = e.getModifiersEx();
//        String modString = "modifiers = " + modifiers;
//        String tmpString = KeyEvent.getModifiersExText(modifiers);
//        if (tmpString.length() > 0) {
//            modString += " (" + tmpString + ")";
//        } else {
//            modString += " (no modifiers)";
//        }
//        int keyCode = e.getKeyCode();
//        String keyString = "key code = " + keyCode
//                    + " ("
//                    + KeyEvent.getKeyText(keyCode)
//                    + ")";
//        System.out.println(modString);
//        System.out.println(keyString);
		
        if (e.getKeyCode() == 18)
        { 
    		switch (experimentController.getCondition().getType())
			{
				case Condition.FLOATING:
					Point proxyPoint = SwingUtilities.convertPoint(bottomPanel, targetLabel.getLocation(), this);
					MouseEvent me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), proxyPoint.x, proxyPoint.y, 1, false, MouseEvent.BUTTON2);
					spiderMenu.buildSpiderMenu(me);
					break;
				case Condition.TELESCOPING:
					System.out.println("key");
					break;
			}
        }
	}
	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e)    { }
	
	ClientController controller;
	ExperimentController experimentController;
	MouseRecorder mr;

	SampleApplicationMenuBar menuBar;
	SampleApplicationToolBar toolBar;
	
	FloatingComponent floatingComponent;
	SpiderMenu2 spiderMenu;
	Component glassPane;
	
	JPanel bottomPanel, labelPanel, buttonPanel, buttonRowPanel, spacerPanel, centerPanel;
	JButton bottomButton;
	JTextArea textArea;
	JLabel targetLabel, trialsLeftLabel, timeLabel;
	Icon targetIcon;
	int sectionCounter;
	
	boolean targetsGenerated, firstControlTrial, firstFloatingTrial;

	public ControlPanel(ClientController controller)
	{
		super();
		this.setFocusable(true);
		this.addKeyListener(this);
		requestFocusInWindow();
		this.controller = controller;
		experimentController = controller.getController().getExperimentController();
		setLayout(new BorderLayout());
		
		menuBar = new SampleApplicationMenuBar(this);
		controller.getController().setJMenuBar(menuBar);

		toolBar = new SampleApplicationToolBar(this);
		add(toolBar, BorderLayout.NORTH);

//		JPanel instructionPanel = new JPanel(new BorderLayout());
//		instructionPanel.setBackground(Color.WHITE);
		
		centerPanel = new JPanel();
//		textArea = new JTextArea();
//		textArea.setLineWrap(true);
//		textArea.setWrapStyleWord(true);
//		textArea.setBackground(getBackground());
//		textArea.setPreferredSize(new Dimension(300, 300));
//		textArea.setEditable(false);
//		textArea.setAlignmentX(RIGHT_ALIGNMENT);
//		instructionPanel.add(textArea, BorderLayout.CENTER);
//		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(centerPanel, BorderLayout.CENTER);
		
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.addKeyListener(this);
		labelPanel = new JPanel();
		targetLabel = new JLabel();
		labelPanel.add(targetLabel);

		buttonPanel = new JPanel();
		bottomButton = new JButton("Click here to Start Next Section");
		buttonPanel.add(bottomButton);

		buttonRowPanel = new JPanel(new BorderLayout());

		trialsLeftLabel = new JLabel();
		timeLabel = new JLabel();
		
		buttonRowPanel.add(trialsLeftLabel, BorderLayout.EAST);
		buttonRowPanel.add(timeLabel, BorderLayout.WEST);
		buttonRowPanel.add(buttonPanel, BorderLayout.CENTER);
		
		bottomPanel.add(labelPanel, BorderLayout.NORTH);
		bottomPanel.add(buttonRowPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		validate();
		
		targetsGenerated = false;
		firstControlTrial = true;
		firstFloatingTrial = true;
		sectionCounter = 0;

		ExperimentDialog dialog = new ExperimentDialog(controller.getController(), "General Instructions", experimentController.getExplorePrompt());

		bottomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Component[] menuBarComponents, toolBarComponents;
				Vector accessibleComponents = new Vector();
				JPanel fcPanel;
				MouseEvent me;
				
				menuBarComponents = menuBar.getComponents();
				toolBarComponents = toolBar.getComponents();
				
				accessibleComponents.clear();
				accessibleComponents.add(menuBarComponents);
				accessibleComponents.add(toolBarComponents);
				
				if (!targetsGenerated)
				{ 
					ControlPanel.this.experimentController.generateTargets(accessibleComponents);
					targetsGenerated = true;
				}
				
				if (experimentController.areMoreConditions())
				{
					switch (experimentController.getCondition().getType())
					{
						case Condition.CONTROL:
							if (experimentController.areMoreTrials())
							{
								if (firstControlTrial)
								{
									sectionCounter++;
									mr = new MouseRecorder();
									firstControlTrial = false;
									bottomButton.setText("Click here for Next Target");					
									targetLabel.setText("");
									targetLabel.setIcon(null);
									
									String dialogTitle = "Section " + sectionCounter + ":  Regular Menus";
									createDialog(ControlPanel.this.controller.getController(), dialogTitle, experimentController.getDialogContent("control"));
								}
								else
								{
//									bottomButton.setText("                          ");
									trialsLeftLabel.setVisible(false);
									timeLabel.setVisible(false);
									bottomButton.setVisible(false);
									if (experimentController.getTarget().getType() == Target.BUTTON)
									{
										targetLabel.setIcon(experimentController.getTarget().getIcon());
										targetLabel.setText("Button: " + experimentController.getTarget().getText());
										mr.recordEvent(new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JButton)e.getSource()).getLocation().x, ((JButton)e.getSource()).getLocation().y, 1, false, 1), EventRecord.START_EVENT);
										
									}
									else
									{
										targetLabel.setText("Menu: " + experimentController.getTarget().getParentName() + " > " + experimentController.getTarget().getText());
										mr.recordEvent(new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JButton)e.getSource()).getLocation().x, ((JButton)e.getSource()).getLocation().y, 1, false, 1), EventRecord.START_EVENT);
									}
								}
							}
							else if (experimentController.isConditionChange())
							{
								trialsLeftLabel.setVisible(false);
								timeLabel.setVisible(false);
								targetLabel.setText("Section Complete");
								bottomButton.setVisible(true);
								bottomButton.setText("Click here to Start Next Section");
								targetLabel.setIcon(null);
							}
							validate();
							break;

						case Condition.FLOATING:
							if (experimentController.areMoreTrials())
							{
								if (firstFloatingTrial)
								{
									sectionCounter++;
									mr = new MouseRecorder();
									firstFloatingTrial = false;
									bottomButton.setText("Click here for Next Target");
									
									trialsLeftLabel.setVisible(false);
									timeLabel.setVisible(false);
									targetLabel.setText("");
									targetLabel.setIcon(null);
//									textArea.setText("Trials: " + new Integer(experimentController.getTrialNum()).toString());

									String dialogTitle = "Section " + sectionCounter + ":  Floating Menus";
									createDialog(ControlPanel.this.controller.getController(), dialogTitle, experimentController.getDialogContent("floating"));
									
									me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), 0, 0, 1, false, 1);
									spiderMenu = new SpiderMenu2(me.getPoint(), ControlPanel.this.controller.getController(), accessibleComponents, me, ControlPanel.this);
									fcPanel = new JPanel(new BorderLayout());
									fcPanel.setOpaque(false);
									fcPanel.add(spiderMenu, BorderLayout.CENTER);
									fcPanel.setBackground(Color.WHITE);					
				
									ControlPanel.this.controller.getController().setGlassPane(fcPanel);
									glassPane = ControlPanel.this.controller.getController().getGlassPane();
								}
								else
								{
									glassPane.setVisible(true);
//									bottomButton.setText("                          ");
									bottomButton.setVisible(false);
									trialsLeftLabel.setVisible(false);
									timeLabel.setVisible(false);
									if (experimentController.getTarget().getType() == Target.BUTTON)
									{
										targetLabel.setIcon(experimentController.getTarget().getIcon());
										targetLabel.setText("Button: " + experimentController.getTarget().getText());
										mr.recordEvent(new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JButton)e.getSource()).getLocation().x, ((JButton)e.getSource()).getLocation().y, 1, false, 1), EventRecord.START_EVENT);
									}
									else
									{
										targetLabel.setText("Menu: " + experimentController.getTarget().getParentName() + " > " + experimentController.getTarget().getText());
										mr.recordEvent(new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JButton)e.getSource()).getLocation().x, ((JButton)e.getSource()).getLocation().y, 1, false, 1), EventRecord.START_EVENT);
									}
								}
							}
							else if (experimentController.isConditionChange())
							{
								targetLabel.setText("Section Complete");
								bottomButton.setVisible(true);
								bottomButton.setText("Click here to Start Next Section");
								targetLabel.setIcon(null);
							}
							validate();
							break;
							
						case Condition.TELESCOPING:
							sectionCounter++;
							String dialogTitle = "Section " + sectionCounter + ": Telescoping Menus";
							createDialog(ControlPanel.this.controller.getController(), dialogTitle, experimentController.getDialogContent("telescoping"));

							me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), 0, 0, 1, false, 1);
							floatingComponent = new FloatingComponent(ControlPanel.this.controller.getController(), accessibleComponents, me, experimentController);
							fcPanel = new JPanel(new BorderLayout());
							fcPanel.setOpaque(true);
							fcPanel.add(floatingComponent, BorderLayout.CENTER);
							fcPanel.setBackground(Color.WHITE);

							ControlPanel.this.controller.getController().setGlassPane(fcPanel);
							glassPane = ControlPanel.this.controller.getController().getGlassPane();
							glassPane.setVisible(true);
							validate();
							break;
					}
				}
				else
				{
					String dialogTitle = "We're Finished";
					createDialog(ControlPanel.this.controller.getController(), dialogTitle, "Thanks for helping.\n\nWe'll email you if you won the gift certificate");

//					textArea.setText("");
					bottomButton.setVisible(false);						
					targetLabel.setText("");
					targetLabel.setIcon(null);
					trialsLeftLabel.setVisible(false);
					timeLabel.setVisible(false);
					
//					experimentController.parseAndWriteExperiment();
				}
			}
		});
	}
	
	public ClientController getController() { return controller; }
	
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("test");
		if (e.getSource() instanceof JMenuItem)
		{
			MouseEvent me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JMenuItem)e.getSource()).getLocation().x, ((JMenuItem)e.getSource()).getLocation().y, 1, false, 1);
			mr.recordEvent(me, FloatingComponent.FLOATING_MENU, ((JMenuItem)e.getSource()).getText(), EventRecord.SELECTION_EVENT);
		}
		else if (e.getSource() instanceof JButton)
		{
			MouseEvent me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), ((JButton)e.getSource()).getLocation().x, ((JButton)e.getSource()).getLocation().y, 1, false, 1);
			mr.recordEvent(me, FloatingComponent.FLOATING_BUTTON, ((JButton)e.getSource()).getToolTipText(), EventRecord.SELECTION_EVENT);
		}
		experimentController.saveTrial(mr.getRecording());
		long tempTime = experimentController.getTime();
		mr.reset();
		experimentController.nextTrial();

		if (experimentController.isRestTime())
		{ 
			createRestDialog();
			targetLabel.setText("");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here for Next Target");
			
			trialsLeftLabel.setVisible(true);
			timeLabel.setVisible(true);
			trialsLeftLabel.setText("Trials Left : " + new Integer(experimentController.getTrialNum()).toString() + "  ");
			timeLabel.setText("  Time :" + new Long(tempTime).toString());
		}
		else if (experimentController.areMoreTrials()) // experimentController.areMoreConditions()
		{
			targetLabel.setText("");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here for Next Target");

			trialsLeftLabel.setVisible(true);
			timeLabel.setVisible(true);
			trialsLeftLabel.setText("Trials Left : " + new Integer(experimentController.getTrialNum()).toString() + "  ");
			timeLabel.setText("  Time :" + new Long(tempTime).toString());
		}
		else if (experimentController.isConditionChange())
		{
//			System.out.println("actionPerformed.areMoreConditions()");
			targetLabel.setText("Section Complete");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here to Start Next Section");
			targetLabel.setIcon(null);
			trialsLeftLabel.setVisible(false);
			timeLabel.setVisible(false);
//			textArea.setText("");
//			createDialog(ControlPanel.this.controller.getController(), experimentController.getDialogTitle(sectionCounter), experimentController.getDialogContent(sectionCounter));
		}
		else if (!experimentController.areMoreConditions())
		{
			String dialogTitle = "We're Finished";
			createDialog(ControlPanel.this.controller.getController(), dialogTitle, "Thanks for helping.\n\nWe'll email you if you won the gift certificate");
//			textArea.setText("");
			bottomButton.setVisible(false);						
			trialsLeftLabel.setVisible(false);
			timeLabel.setVisible(false);
//			experimentController.parseAndWriteExperiment();
		}
		targetLabel.setIcon(null);
		validate();
	}
	
	public void floatingSelection(int typeSelected, MouseEvent e, String label)
	{
		if (typeSelected == FloatingComponent.FLOATING_MENU) 
		{
			mr.recordEvent(e, FloatingComponent.FLOATING_MENU, label, EventRecord.SELECTION_EVENT);
		}
		else
		{
			mr.recordEvent(e, FloatingComponent.FLOATING_BUTTON, label, EventRecord.SELECTION_EVENT);
		}

		experimentController.saveTrial(mr.getRecording());
		long tempTime = experimentController.getTime();
		mr.reset();
		experimentController.nextTrial();
		
		if (experimentController.isRestTime())
		{ 
			createRestDialog();
			targetLabel.setText("");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here for Next Target");

			trialsLeftLabel.setVisible(true);
			timeLabel.setVisible(true);
			trialsLeftLabel.setText("Trials Left : " + new Integer(experimentController.getTrialNum()).toString() + "  ");
			timeLabel.setText("  Time :" + new Long(tempTime).toString());
		}
		else if (experimentController.areMoreTrials())
		{
			targetLabel.setText("");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here for Next Target");				

			trialsLeftLabel.setVisible(true);
			timeLabel.setVisible(true);
			trialsLeftLabel.setText("Trials Left : " + new Integer(experimentController.getTrialNum()).toString() + "  ");
			timeLabel.setText("  Time :" + new Long(tempTime).toString());
		}
		else if (experimentController.isConditionChange())
		{
			targetLabel.setText("Section Complete");
			bottomButton.setVisible(true);
			bottomButton.setText("Click here to start Next Section");			
			targetLabel.setIcon(null);
//			textArea.setText("");
		}
		else if (!experimentController.areMoreConditions())
		{
			String dialogTitle = "We're Finished";
			createDialog(ControlPanel.this.controller.getController(), dialogTitle, "Thanks for helping.\n\nWe'll email you if you won the gift certificate");
//			textArea.setText("");
			targetLabel.setText("");
			bottomButton.setVisible(false);		
			trialsLeftLabel.setVisible(false);
			timeLabel.setVisible(false);
//			experimentController.parseAndWriteExperiment();
		}
		glassPane.setVisible(false);
		targetLabel.setIcon(null);
		validate();
	}

	public void createRestDialog()
	{
		ExperimentDialog dialog = new ExperimentDialog(controller.getController(), "Take a Break", "Click OK to continue");
	}
	
	public void createDialog(JFrame parent, String title, String content)
	{
		ExperimentDialog dialog = new ExperimentDialog(parent, title, content);
	}
}
