import java.awt.event.*;
import javax.swing.*;

public class ClientController implements ActionListener
{
	ObjectSelectionStudy controller;
	ExperimentController experimentController;
	ExperimentDialog experimentDialog;
	
	public static final int CONSENT_FORM_PANEL = 0;
	public static final int QUESTION_PANEL		= 1; 
	public static final int CONTROL_PANEL   	= 2;
	public static final int TELESCOPING_PANEL  = 3;
	public static final int FLOATING_PANEL		= 4;
	public static final int MAX_PANELS		= 4;

	int currentPanel, maxPanels;
	String[] surveyResults;

	public ClientController(ObjectSelectionStudy controller)
	{
		this.controller = controller;
		experimentController = controller.getExperimentController();
		currentPanel = 0;
	}

	public void start()
	{
		controller.setContentPane(new ConsentFormPanel(this));
		controller.validate();
	}

	public void actionPerformed(ActionEvent e)
	{
		switch(currentPanel)
		{
		case CONSENT_FORM_PANEL:
			if (((JButton)e.getSource()).getText().equals("OK") )
			{
				controller.setContentPane(new QuestionPanel(this));
			}
			break;
		case QUESTION_PANEL:
			if (((JButton)e.getSource()).getText().equals("Start Study") )
			{
				experimentController.setSurvey(surveyResults);
				controller.setContentPane(new ControlPanel(this));
			}
			break;
		}
		controller.validate();
		currentPanel++;
	}
	
	public void setSurvey(String[] results) { this.surveyResults = results; }
	
	public String[] getSurveyResults() { return surveyResults; }
	
	public ObjectSelectionStudy getController() { return controller; }
	
	public void setController(ObjectSelectionStudy owner) { this.controller = owner; }	
}
