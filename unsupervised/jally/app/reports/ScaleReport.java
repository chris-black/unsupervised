package reports;

import java.util.List;
import models.Scale;


/**
 * object model representing a scale report- consumable by generators
 * scale: ideal (#portal/month) current (#portal/month)
 * 
 * @author dlange
 *
 */
public class ScaleReport extends Report {
	
	public String title;
	public List<Scale> data;
	
	
	public ScaleReport(String title, List<Scale> data) {
		this.title = title;
		this.data = data;
	}

	
	public Report generate() {
		htmlReport = views.html.scalereport.render(this).body();
		generated = true;
		return this;
	}
	
}
