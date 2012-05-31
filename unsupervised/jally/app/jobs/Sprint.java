package jobs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Burndown;
import models.Scrum;

import play.Logger;

public class Sprint {

	public void addData(String release) {
		/**
		List<Scrum> scrums = Scrum.findByRelease(release);
		for (Scrum scrum : scrums) {
			Burndown burndown = Burndown.findLatestByProjectIteration(scrum.name);
			if (isSprintDay(burndown.iteration.name, burndown.day)) {
				// get daily burndown for scrum
				Burndown todayBurndown = queryBurndown(scrum, burndown);
				Logger.info("Sprint:addData collected burndown for day:"+todayBurndown.day);
				todayBurndown.save();
			}
		}
		*/
	}
	
	private Burndown queryBurndown(Scrum scrum, Burndown burndown) {
		// get burndown for today
		Burndown today = new Burndown();
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		today.day = formatter.format(rightNow.getTime());
		return today;
	}
	
	public boolean isSprintDay(String iteration, String lastDay) {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date lastDate = formatter.parse(lastDay);
			Calendar given = Calendar.getInstance();
			given.setTime(lastDate);
			if (!isSameDay(rightNow, given)) {
				Logger.info("Not same day");
				if (given.getTime().before(rightNow.getTime())) {
					Logger.info("Last day before today");
					Date iterDate = formatter.parse(iteration);
					Calendar iter = Calendar.getInstance();
					iter.setTime(iterDate);
					if (iter.getTime().before(rightNow.getTime())) {
						Logger.info("Iter before today");
						iter.add(Calendar.DAY_OF_YEAR, 14);
						if (iter.getTime().after(rightNow.getTime())) {
							// check for workday
							if (rightNow.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && rightNow.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
								return true;
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
