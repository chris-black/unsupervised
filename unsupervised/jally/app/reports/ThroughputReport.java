package reports;

import java.util.List;

import models.Service;



/**
 * object model representing a scale report- consumable by generators
 * scale: ideal (#portal/month) current (#portal/month)
 * 
 * @author dlange
 *
 */
public class ThroughputReport extends Report {
	
	public String title;
	public List<Service> data;
	
	public ThroughputReport(String title, List<Service> serviceData) {
		this.title = title;
		this.data = serviceData;
	}

	
	public Report generate() {
		htmlReport = views.html.serviceperf.render(this).body();
		generated = true;
		return this;
	}
	
}
