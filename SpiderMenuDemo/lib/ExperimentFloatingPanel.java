import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class ExperimentFloatingPanel extends JPanel
{
	ClientController controller;
	ExperimentController experimentController;

	SampleApplicationMenuBar menuBar;
	SampleApplicationToolBar toolBar;
	
//	JPanel contentPane;
	FloatingComponent floatingComponent;
	Component glassPane;

	public ExperimentFloatingPanel(ClientController controller)
	{
		super();
		this.controller = controller;
		experimentController = controller.getController().getExperimentController();
		setLayout(new BorderLayout());
		
		menuBar = new SampleApplicationMenuBar();
		controller.getController().setJMenuBar(menuBar);

		toolBar = new SampleApplicationToolBar(this);
		add(toolBar, BorderLayout.NORTH);

		JPanel instructionPanel = new JPanel(new BorderLayout());
		instructionPanel.setBackground(Color.WHITE);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBackground(getBackground());
		textArea.append(experimentController.getExplorePrompt());
		textArea.setPreferredSize(new Dimension(300, 300));
		instructionPanel.add(textArea, BorderLayout.CENTER);
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(instructionPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton startButton = new JButton("OK");
		bottomPanel.add(startButton);
		add(bottomPanel, BorderLayout.SOUTH);
		validate();
		
//		JPanel gp = new JPanel();
//		gp.add(new JButton("hello"));
//		controller.getController().setGlassPane(gp);
//		gp.setOpaque(false);
//		controller.getController().getGlassPane().setVisible(true);
//		validate();

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component[] menuBarComponents = menuBar.getComponents();
				Component[] toolBarComponents = toolBar.getComponents();
				
				Vector accessibleComponents = new Vector();
				accessibleComponents.add(menuBarComponents);
				accessibleComponents.add(toolBarComponents);
				
				MouseEvent me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), 0, 0, 1, false, 1);
				floatingComponent = new FloatingComponent(ExperimentFloatingPanel.this.controller.getController(), accessibleComponents, me, experimentController);
//				floatingComponent.setExperimentController(experimentController);
				JPanel fcPanel = new JPanel(new BorderLayout());
				fcPanel.setOpaque(true);
				fcPanel.add(floatingComponent, BorderLayout.CENTER);
				fcPanel.setBackground(Color.WHITE);
				
//				ExperimentFloatingPanel.this.experimentController.generateTargets(accessibleComponents);

				ExperimentFloatingPanel.this.controller.getController().setGlassPane(fcPanel);
				glassPane = ExperimentFloatingPanel.this.controller.getController().getGlassPane();
				glassPane.setVisible(true);
				validate();
			}
		});

//		setVisible(true);
//		controller.getOwner().invalidate();
//
////		textArea.addMouseListener(new MouseListener() {
////			public void mouseClicked(MouseEvent e)
////			{
////				if (e.getButton() == MouseEvent.BUTTON2)
////				{
////					Component[] menuBarComponents = menuBar.getComponents();
////					Component[] toolBarComponents = toolBar.getComponents();
////					
////					Vector accessibleComponents = new Vector();
////					accessibleComponents.add(menuBarComponents);
////					accessibleComponents.add(toolBarComponents);
////					
////					spiderMenu = new FloatingComponent(ObjectSelectionStudy.this, accessibleComponents, e);
////					JPanel spiderPanel = new JPanel(new BorderLayout());
////					spiderPanel.setOpaque(true);
////					spiderPanel.add(spiderMenu, BorderLayout.CENTER);
////		
////					setGlassPane(spiderPanel);
////					glassPane = getGlassPane();
////					glassPane.setVisible(true);
////					validate();
////				}
////			}
////
////			public void mousePressed(MouseEvent e) {}
////			public void mouseReleased(MouseEvent e) {}
////			public void mouseEntered(MouseEvent e) {}
////			public void mouseExited(MouseEvent e) {}
////		});
	}
	
	public ClientController getController() { return controller; }
}
