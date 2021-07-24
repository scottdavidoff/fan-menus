import java.awt.*;
import javax.swing.*;

public class QuestionPanel2 extends JPanel
{
	ClientController controller;

	String instructions = "Let's test the connection with your mouse";
	String question1 = "Does your mouse have a scroll wheel?";

	public QuestionPanel2(ClientController controller)
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
		JPanel questionLabelPanel = new JPanel();
		questionLabelPanel.add(questionLabel);
		questionAndAnswerPanel.add(questionLabelPanel, BorderLayout.NORTH);

		JPanel answerPanel = new JPanel();
		JButton button1 = new JButton("Yes");
		JButton button2 = new JButton("No");
		answerPanel.add(button1);
		answerPanel.add(button2);
		questionAndAnswerPanel.add(answerPanel, BorderLayout.CENTER);

		this.add(labelPanel, BorderLayout.NORTH);
		this.add(questionAndAnswerPanel, BorderLayout.CENTER);
		
		button1.addActionListener(controller);
		button2.addActionListener(controller);
	}
	
}
