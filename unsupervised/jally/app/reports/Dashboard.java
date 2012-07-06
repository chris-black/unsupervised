package reports;

import java.util.List;

import play.Logger;

import jobs.DashboardType;

import models.Iteration;
import models.Scale;
import models.Service;

import com.google.common.collect.Lists;

/**
 * object model representing a dashboard- consumable by generators
 * @author dlange
 *
 */
public class Dashboard {

	public DashboardType dashboardType;
	public List<String> reports;
	
	public Dashboard(DashboardType dashboardType) {
		this.dashboardType = dashboardType;
		this.reports = Lists.newArrayList();
	}

	/**
	 * Generate reports for current iteration, release
	 * 
	 * Main dashboard
	 * 	Scale portal/han
	 * 	%services implemented/transactions
	 * 	Service throughput
	 * 
	 * Rally dashboard
	 * 	prev sprint / current sprint
	 * 
	 * @return
	 */
	public Dashboard generate() {
		reports.clear();
		reports.addAll(generateReports());
		return this;
	}
	
	private List<String> generateReports() {
		if (DashboardType.Master.equals(dashboardType)) {
			return generateMaster();
		} else if (DashboardType.Scrum.equals(dashboardType)) {
			return generateBurndowns();
		}
		return generateBurndowns();
	}
	
	private List<String> generateMaster() {
		List<String> reports = Lists.newArrayList();
		// get scale data
		List<Scale> scaleData = Scale.all();
		reports.add(new ScaleReport("Portal", scaleData).generate().htmlReport);
		reports.add(new ScaleReport("HAN", scaleData).generate().htmlReport);
		List<Service> serviceData = Service.all();
		reports.add(new ServiceReport("Complete", serviceData).generate().htmlReport);
		reports.add(new ThroughputReport("Throughput", serviceData).generate().htmlReport);
		
		return reports;
	}

	private List<String> generateBurndowns() {
		List<String> burndowns = Lists.newArrayList();
		List<Iteration> iterations = Iteration.getTeamIterationsToday();
		Logger.info("num reports:"+iterations.size());
		for (Iteration iteration : iterations) {
			Logger.info("generateBurndown for "+iteration.team.name);
			burndowns.add(new BurndownReport(iteration).generate().htmlReport);
		}
		return burndowns;
	}
}
