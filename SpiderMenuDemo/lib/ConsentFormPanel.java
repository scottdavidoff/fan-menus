import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConsentFormPanel extends JPanel
{

	ClientController controller;
	boolean agree;
	
	JButton agreeButton;
	JLabel agreeLabel;
	
	public ConsentFormPanel(ClientController controller)
	{
		super(new BorderLayout());
		this.controller = controller;
		agree = false;

		JPanel welcomePanel = new JPanel(new BorderLayout());
		JLabel welcomeLabel = new JLabel(welcomeText, SwingConstants.CENTER);
//		welcomeLabel.setAutoscrolls(true);
		welcomeLabel.setPreferredSize(new Dimension(550, 25));
		welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
		welcomePanel.setBackground(Color.BLUE);
		welcomeLabel.setForeground(Color.WHITE);
		JPanel welcomeSpacerPanel = new JPanel();
		welcomeSpacerPanel.setPreferredSize(new Dimension(300, 15));
		welcomePanel.add(welcomeSpacerPanel, BorderLayout.SOUTH);
		this.add(welcomePanel, BorderLayout.NORTH);
		
        //Create a text area.
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(550, 650));
        scrollPane.setBorder(
        		BorderFactory.createCompoundBorder(
        		BorderFactory.createCompoundBorder(
        		BorderFactory.createTitledBorder("Please read this consent form, and click \"OK\""),
			BorderFactory.createEmptyBorder(5,5,5,5)),
			scrollPane.getBorder()));
		this.add(scrollPane, BorderLayout.CENTER);
		
		JPanel westSpacerPanel = new JPanel();
		JPanel eastSpacerPanel = new JPanel();
		westSpacerPanel.setPreferredSize(new Dimension(35, 200));
		eastSpacerPanel.setPreferredSize(new Dimension(35, 200));
		this.add(westSpacerPanel, BorderLayout.WEST);
		this.add(eastSpacerPanel, BorderLayout.EAST);
		
		JCheckBox checkBox = new JCheckBox();
		agreeLabel = new JLabel(agreeText);
//		agreeLabel.setEnabled(false);
		agreeButton = new JButton("OK");
		agreeButton.setEnabled(false);
		JPanel bottomSpacerPanel = new JPanel();
		bottomSpacerPanel.setPreferredSize(new Dimension(300, 15));
		JPanel bottomSpacerPanel2 = new JPanel();
		bottomSpacerPanel.setPreferredSize(new Dimension(300, 15));

//		JPanel checkBoxPanel = new JPanel();
//		JPanel buttonPanel = new JPanel();

		JPanel bottomPanel = new JPanel(new BorderLayout());
		JPanel bottomButtonPanel = new JPanel();
		bottomButtonPanel.add(checkBox);
		bottomButtonPanel.add(agreeButton);
		bottomButtonPanel.add(agreeLabel);
		bottomButtonPanel.add(agreeButton);
		bottomPanel.add(bottomSpacerPanel, BorderLayout.NORTH);
		bottomPanel.add(bottomButtonPanel, BorderLayout.CENTER);
		bottomPanel.add(bottomSpacerPanel2, BorderLayout.SOUTH);
		this.add(bottomPanel, BorderLayout.SOUTH);

		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				agree = !agree;
				ConsentFormPanel.this.agreeButton.setEnabled(agree);
//				ConsentFormPanel.this.agreeLabel.setEnabled(agree);
			}
		});

		agreeButton.addActionListener(controller);
	}
	
	String welcomeText =
		"Welcome to the \"Computer Mouse Object Selection Study\"";
	
	String content = 
			"This study has been approved by the Carnegie Mellon Institutional Review Board (IRB) for the use of human subjects\n\n" +
			"Project\n" +
			"Computer Mouse Object Selection Effectiveness Study\n\n" +
			"Principal Investigator\n" +
			"Professor Scott Hudson\n\n" +
			"Procedures\n" +
			"The computer will ask you to select a series of familiar objects on a computer screenusing a mouse.  During the experiment, the computer will record the motion of the computer mouse and the objects selected.\n\n" +
			"Risks\n" +
			"You may get tired from performing this experiment.  If you experience tiredness in any way, feel free to stop.  We foresee no other risks associated with participation.\n\n" +
			"Costs\n" +
			"There will be no cost to you if you participate in this research.\n\n" +
			"Benefits\n" +
			"There may be no personal benefit to you but the knowledge received may be of value to the public at large.\n\n" +
			"Rights\n" +
			"Your participation is voluntary. You can withdraw your consent and discontinue participation in the study at any time without affecting your relationship with Carnegie Mellon University. New information developed during the course of the study which might affect the understandings in this consent and willingness to continue to participate will be brought to your attention. Your participation in this study may be ended by the principal investigator or thesponsor if they feel it is best in the interest of safety. No guarantees have been made as to the results of your participation in the study.\n\n" +
			"Confidentiality\n" +
			"We will use the information you supply to develop methods to more efficiently select objects on a computer screen.  " +
			"I understand that the following procedures will be used to maintain my anonymity in analysis and publication/presentation of any results: " +
			"(1) Each participant will be assigned a number, names will not be recorded. " +
			"(2) The researchers will save thedata file by participant number, not by name.  " +
			"(3) Only members of the research group will view the files in detail.  " +
			"(4) The data files and files will be stored in asecured location by Professor Hudson.  " +
			"(5) Only authorized researchers will have access to these files.\n\n" +
			"Contact Information\n" +
			"If you have any questions about this study, you should feel free to ask them now of the researcher, or any time throughout the study by contacting:\n" +
			"\tProfessor Scott Hudson\n" +
			"\tHuman Computer Interaction Institute\n" +
			"\tCarnegie Mellon University\n" +
			"\tPittsburgh, PA 15213 - 3891\n" +
			"\t+1 412.268.2429\n" +
			"\tscott.hudson@cs.cmu.edu\n\n" +
			"You may report any objections to the study, either orally or in writing to:\n" +
			"\tIRB Chair, CMU Compliance Officer\n" +
			"\tRegulatory Compliance Administration\n" +
			"\tCarnegie Mellon University\n" +
			"\tUTDC Building, Room 312\n" +
			"\tCarnegie Mellon University\n" +
			"\tPittsburgh, PA 15213 - 3891\n" +
			"\t+1 412.268.4727\n" +
			"\tirb-review@andrew.cmu.edu\n\n" +
			"For any questions pertaining to your rights as a research subject, you may contact the Regulatory Compliance Administration office above.";

	String agreeText = "I agree to participate in this research study.";

}
