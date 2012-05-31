package models;
import java.util.List;

import play.Logger;


/**
 * object model representing a burndown report- consumable by generators
 * 
 * @author dlange
 *
 */
public class BurndownReport extends Report {
	
	public String project;
	public String iteration;
	public List<Burndown> data;
	
	public BurndownReport(String project, String iteration) {
		this.project = project;
		this.iteration = iteration;
//		this.data = Burndown.findByProjectIteration(project, iteration);
	}

	public Report generate() {
//		data = Burndown.findByProjectIteration(project, iteration);
		htmlReport = views.html.burndownreport.render(this).body();
		generated = true;
		Logger.info(htmlReport);
		return this;
	}
}
