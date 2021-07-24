import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class QuestionPanel3 extends JPanel
{
	ClientController controller;

	String instructions = "Let's test the connection with your mouse";
	String question1 = "Click each of your mouse button(s)";

	JPanel button1Panel, button2Panel, button3Panel, scrollWheelPanel,
		   button1InstructionPanel, button2InstructionPanel, button3InstructionPanel, scrollWheelInstructionPanel;
	JLabel button1Label, button2Label, button3Label, scrollWheelLabel,
		   button1InstructionsLabel, button2InstructionsLabel, button3InstructionsLabel, scrollWheelInstructionsLabel;
	
	public QuestionPanel3(ClientController controller)
	{
		super();
		this.controller = controller;
		setLayout(new BorderLayout());

		JPanel labelPanel = new JPanel();
		JLabel instructionLabel = new JLabel(instructions);
		labelPanel.add(instructionLabel);
		labelPanel.setBackground(Color.BLUE);
		instructionLabel.setForeground(Color.WHITE);

		JPanel questionAndAnswerPanel = new JPanel();
		questionAndAnswerPanel.setLayout(new BorderLayout());
		JLabel questionLabel = new JLabel(question1);
		questionAndAnswerPanel.add(questionLabel, BorderLayout.NORTH);
		
		JPanel mouseButtonPanel = new JPanel();
		
		// TODO
		// change labels on buttons
		if (true) // controller.getNumMouseButtons() > 0
		{
			button1Panel = new JPanel();
			button1Panel.setPreferredSize(new Dimension(50, 50));
			button1Label = new JLabel("No");
			button1Label.setFont(new Font("SansSerif", Font.PLAIN, 24));
			button1Panel.add(button1Label);
			button1Panel.setBackground(Color.RED);
			button1Label.setForeground(Color.WHITE);
			
			button1InstructionPanel = new JPanel();
			button1InstructionPanel.setLayout(new BorderLayout());
			button1InstructionsLabel = new JLabel("Left"); // controller.getNumMouseButtons() == 1 ? "Button" : "Left"
			button1InstructionsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
			button1InstructionPanel.add(button1Panel, BorderLayout.NORTH);
			button1InstructionPanel.add(button1InstructionsLabel, BorderLayout.CENTER);
			mouseButtonPanel.add(button1InstructionPanel);
		}

		if (true) // controller.getNumMouseButtons() > 1
		{
			button2Panel = new JPanel();
			button2Panel.setPreferredSize(new Dimension(50, 50));
			button2Label = new JLabel("No");
			button2Label.setFont(new Font("SansSerif", Font.PLAIN, 24));
			button2Panel.add(button2Label);
			button2Panel.setBackground(Color.RED);
			button2Label.setForeground(Color.WHITE);
			mouseButtonPanel.add(button2Panel);

			button2InstructionPanel = new JPanel();
			button2InstructionPanel.setLayout(new BorderLayout());
			button2InstructionsLabel = new JLabel("Middle");
			button2InstructionsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
			button2InstructionPanel.add(button2Panel, BorderLayout.NORTH);
			button2InstructionPanel.add(button2InstructionsLabel, BorderLayout.CENTER);
			mouseButtonPanel.add(button2InstructionPanel);
		}

		if (true) // controller.getNumMouseButtons() > 2
		{
			button3Panel = new JPanel();
			button3Panel.setPreferredSize(new Dimension(50, 50));
			button3Label = new JLabel("No");
			button3Label.setFont(new Font("SansSerif", Font.PLAIN, 24));
			button3Panel.add(button3Label);
			button3Panel.setBackground(Color.RED);
			button3Label.setForeground(Color.WHITE);
			mouseButtonPanel.add(button3Panel);

			button3InstructionPanel = new JPanel();
			button3InstructionPanel.setLayout(new BorderLayout());
			button3InstructionsLabel = new JLabel("Right");
			button3InstructionsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
			button3InstructionPanel.add(button3Panel, BorderLayout.NORTH);
			button3InstructionPanel.add(button3InstructionsLabel, BorderLayout.CENTER);
			mouseButtonPanel.add(button3InstructionPanel);
		}

		if (true) // controller.hasScrollWheel()
		{
			scrollWheelPanel = new JPanel();
			scrollWheelPanel.setPreferredSize(new Dimension(50, 50));
			scrollWheelLabel = new JLabel("No");
			scrollWheelLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
			scrollWheelPanel.add(scrollWheelLabel);
			scrollWheelPanel.setBackground(Color.RED);
			scrollWheelLabel.setForeground(Color.WHITE);
			mouseButtonPanel.add(scrollWheelPanel);
			
			scrollWheelInstructionPanel = new JPanel();
			scrollWheelInstructionPanel.setLayout(new BorderLayout());
			scrollWheelInstructionsLabel = new JLabel("Wheel");
			scrollWheelInstructionsLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
			scrollWheelInstructionPanel.add(scrollWheelPanel, BorderLayout.NORTH);
			scrollWheelInstructionPanel.add(scrollWheelInstructionsLabel, BorderLayout.CENTER);
			mouseButtonPanel.add(scrollWheelInstructionPanel);
		}

		questionAndAnswerPanel.add(mouseButtonPanel, BorderLayout.CENTER);

		JPanel answerPanel = new JPanel();
		JButton startButton = new JButton("Start Experiment"); // change to show tutorial
//		JButton button2 = new JButton("2 buttons");
//		JButton button3 = new JButton("3 or more");
		answerPanel.add(startButton);
//		answerPanel.add(button2);
//		answerPanel.add(button3);

		this.add(labelPanel, BorderLayout.NORTH);
		this.add(questionAndAnswerPanel, BorderLayout.CENTER);
		this.add(answerPanel, BorderLayout.SOUTH);

		// TODO
		// don't check for buttons not chosen by user
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e)
			{ 
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					button1Label.setText("Yes");
					button1Panel.setBackground(Color.GREEN);
					button1Label.setForeground(Color.WHITE);
					validate();
				}
				if (e.getButton() == MouseEvent.BUTTON2)
				{
					button2Label.setText("Yes");
					button2Panel.setBackground(Color.GREEN);
					button2Label.setForeground(Color.WHITE);
					validate();
				}
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					button3Label.setText("Yes");
					button3Panel.setBackground(Color.GREEN);
					button3Label.setForeground(Color.WHITE);
					validate();
				}
			}

			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
			public void mouseEntered(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				scrollWheelLabel.setText("Yes");
				scrollWheelPanel.setBackground(Color.GREEN);
				scrollWheelLabel.setForeground(Color.WHITE);
				validate();
			}
		});
		
		startButton.addActionListener(controller);
//		button2.addActionListener(controller);
//		button3.addActionListener(controller);

	}
	
}
