import java.awt.event.*;
import java.util.*;

public class MouseRecorder
{
	Vector events;

	int interval;
	long startTime, endTime;
	
	public MouseRecorder() { events = new Vector(); }
	
	public void recordEvent(MouseEvent e, int eventType)
	{
		switch(eventType)
		{
			case EventRecord.MOTION_EVENT:
				events.add(new EventRecord(e.getWhen(), e.getPoint(), EventRecord.MOTION_EVENT));
				break;
			case EventRecord.START_EVENT:
				events.add(new EventRecord(e.getWhen(), e.getPoint(), EventRecord.START_EVENT));
				break;
			case EventRecord.ESCAPE_EVENT:
				EventRecord er = new EventRecord(e.getWhen(), e.getPoint(), EventRecord.SELECTION_EVENT);
				er.setButtonPressed(e.getButton());
				events.add(er);
				break;
		}
	}

	public void recordEvent(MouseEvent e, int targetType, String label, int eventType)
	{
		EventRecord er = new EventRecord(e.getWhen(), e.getPoint(), EventRecord.SELECTION_EVENT);
		er.setButtonPressed(e.getButton());
		er.setTargetType(targetType);
		er.setLabel(label);
		events.add(er);
	}
	
	public void reset()
	{ 
		events.clear();
	}

	public Vector getRecording() { return events; }
}
