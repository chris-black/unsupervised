package reports;

import java.util.List;

import models.Iteration;
import play.Logger;
import com.google.common.collect.Lists;

/**
 * object model representing a dashboard- consumable by generators
 * @author dlange
 *
 */
public class Dashboard {

	public List<String> reports;
	
	public Dashboard(String release) {
		this.reports = Lists.newArrayList();
	}

	/**
	 * Generate reports for current iteration, release
	 * 
	 * @return
	 */
	public Dashboard generate() {
		reports.clear();
		reports.addAll(generateBurndowns());
		return this;
	}
	
	private List<String> generateBurndowns() {
		List<String> burndowns = Lists.newArrayList();
		List<Iteration> iterations = Iteration.getTeamIterations();
		for (Iteration iteration : iterations) {
			burndowns.add(new BurndownReport(iteration).generate().htmlReport);
		}
		return burndowns;
	}
}
