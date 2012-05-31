package models;

/**
 * object model representing a report- consumable by generators
 * burndown uses project/iteration
 * burnup uses release
 * @author dlange
 *
 */
public abstract class Report {
	// state
	public boolean collected = false;
	public boolean generated = false;
	
	// model
	String htmlReport;
	
	public Report() {
	}

	
	/**
	 * Generate html report
	 * 
	 * @return
	 */
	public abstract Report generate();
}
