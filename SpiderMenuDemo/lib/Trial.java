import java.util.*;

public class Trial
{
	long startTime, endTime, distance;
	int iterationForThisTarget;
	Target target;
	Vector events;
	boolean isCorrect;
	
	public Trial(Target target)
	{
		this.target = target;
	}

	public Trial(Target target, int iterationsPerTarget)
	{
		this(target);
		this.iterationForThisTarget = iterationsPerTarget;
	}

	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public Vector getEvents() {
		return events;
	}
	public void setEvents(Vector events) {
		this.events = events;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public Target getTarget() {
		return target;
	}
	public void setTarget(Target target) {
		this.target = target;
	}
	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public boolean wasCorrect() {
		return isCorrect;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	public int getIterationForThisTarget() {
		return iterationForThisTarget;
	}
	public void setIterationForThisTarget(int iterationForThisTarget) {
		this.iterationForThisTarget = iterationForThisTarget;
	}
	public String toString() { return new String((target.getType() == Target.BUTTON ? "button: " : "menu: ") + target.getText()); }
}
