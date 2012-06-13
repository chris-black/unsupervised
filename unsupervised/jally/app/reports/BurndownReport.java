package reports;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import models.Burndown;
import models.Iteration;

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
	public int hoursMax;
	public int pointsMax;
	public List<Burndown> data;
	
	public BurndownReport(Iteration iteration) {
		this.project = iteration.team.name;
		this.iteration = iteration.name;
		this.hoursMax = iteration.totalHours;
		this.pointsMax = iteration.totalPoints;
		this.data = backfill(iteration);
	}

	
	public Report generate() {
		htmlReport = views.html.burndownreport.render(this).body();
		generated = true;
		return this;
	}
	
	private List<Burndown> backfill(Iteration iteration) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<Burndown> filled = Lists.newArrayList();
		for (String day : iteration.workdays()) {
			try {
				Date dayTime = formatter.parse(day);
				boolean added = false;
				for (Burndown burndown : iteration.burndowns) {
					Date burndownTime = formatter.parse(burndown.day);
					if (burndownTime.after(dayTime)) {
						filled.add(new Burndown(day, iteration));
						added = true;
					} else if (burndownTime.equals(dayTime)) {
						filled.add(burndown);
						added = true;
					}
				}
				if (!added) {
					filled.add(new Burndown(day, iteration));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// fill in ideal based on total hrs
		int topline = iteration.totalHours;
		int delta = topline / filled.size();
		for (Burndown burndown : filled) {
			burndown.ideal = (topline > 0)? topline : 0;
			topline -= delta;
		}
		return filled;
	}
}
