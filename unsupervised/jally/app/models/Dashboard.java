package models;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * object model representing a dashboard- consumable by generators
 * @author dlange
 *
 */
public class Dashboard {

	public String release;
	public List<String> reports;
	
	public Dashboard(String release) {
		this.release = release;
		this.reports = Lists.newArrayList();
	}

	/**
	 * Generate reports for current iteration, release
	 * 
	 * @return
	 */
	public Dashboard generate() {
		reports.clear();
		reports.add(new BurndownReport("Enterprise", "2012-05-21").generate().htmlReport);
		reports.add(new BurndownReport("Energize", "2012-05-21").generate().htmlReport);
		reports.add(new BurndownReport("HER", "2012-05-21").generate().htmlReport);
		return this;
	}
}
