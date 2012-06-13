package serialized;

public class HierarchicalRequirement implements Resultable {
	public String Name;
	public Release Release;
	public Project Project;
	public int PlanEstimate;
	public int TaskEstimateTotal;
	public int TaskRemainingTotal;
	public String ScheduleState;
	public String TaskStatus;
}
