import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class SampleApplication extends JFrame
{
	SampleApplicationMenuBar menuBar;
	SampleApplicationToolBar toolBar;
	
	JPanel contentPane;
	
	FloatingComponent spiderMenu;
	Component glassPane;

	public SampleApplication()
	{
		super("Sample Application");
		
		contentPane = new JPanel(new BorderLayout());
		setContentPane(contentPane);

		menuBar = new SampleApplicationMenuBar();
		setJMenuBar(menuBar);

//		toolBar = new SampleApplicationToolBar();
//		contentPane.add(toolBar, BorderLayout.NORTH);

		JTextArea textArea = new JTextArea();
		textArea.setRows(100);
		textArea.setEditable(false);
		contentPane.add(textArea, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		JButton startButton = new JButton("start experiment");
		bottomPanel.add(startButton);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		
//		startButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Component[] menuBarComponents = menuBar.getComponents();
//				Component[] toolBarComponents = toolBar.getComponents();
//				
//				Vector accessibleComponents = new Vector();
//				accessibleComponents.add(menuBarComponents);
//				accessibleComponents.add(toolBarComponents);
//				
//				MouseEvent me = new MouseEvent((Component)e.getSource(),  e.getID(),  e.getWhen(),  e.getModifiers(), 0, 0, 1, false, 1);
//				spiderMenu = new FloatingComponent(SampleApplication.this, accessibleComponents, me);
//				JPanel spiderPanel = new JPanel(new BorderLayout());
//				spiderPanel.setOpaque(true);
//				spiderPanel.add(spiderMenu, BorderLayout.CENTER);
//				spiderPanel.setBackground(Color.WHITE);
//	
//				setGlassPane(spiderPanel);
//				glassPane = getGlassPane();
//				glassPane.setVisible(true);
//			}
//		});
//
//		textArea.addMouseListener(new MouseListener() {
//			public void mouseClicked(MouseEvent e)
//			{
//				if (e.getButton() == MouseEvent.BUTTON2)
//				{
//					Component[] menuBarComponents = menuBar.getComponents();
//					Component[] toolBarComponents = toolBar.getComponents();
//					
//					Vector accessibleComponents = new Vector();
//					accessibleComponents.add(menuBarComponents);
//					accessibleComponents.add(toolBarComponents);
//					
//					spiderMenu = new FloatingComponent(SampleApplication.this, accessibleComponents, e);
//					JPanel spiderPanel = new JPanel(new BorderLayout());
//					spiderPanel.setOpaque(true);
//					spiderPanel.add(spiderMenu, BorderLayout.CENTER);
//		
//					setGlassPane(spiderPanel);
//					glassPane = getGlassPane();
//					glassPane.setVisible(true);
//				}
//			}
//
//			public void mousePressed(MouseEvent e) {}
//			public void mouseReleased(MouseEvent e) {}
//			public void mouseEntered(MouseEvent e) {}
//			public void mouseExited(MouseEvent e) {}
//		});
		
		setLocation(0, 0);
		setSize(600, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		}

		public static void main(String[] args)
		{
			SampleApplication sampleApplication = new SampleApplication();
		}

}
