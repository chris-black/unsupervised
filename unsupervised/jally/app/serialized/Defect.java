package serialized;

public class Defect implements Resultable {
	public String Name;
	public Release Release;
	public Project Project;
	public int PlanEstimate;
	public int TaskEstimateTotal;
	public int TaskRemainingTotal;
	public String ScheduleState;
	public String TaskStatus;
}
