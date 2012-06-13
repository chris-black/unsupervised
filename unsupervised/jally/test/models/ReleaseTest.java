package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import org.junit.Test;


public class ReleaseTest {
	    @Test
	    public void findById() {
	        running(fakeApplication(), new Runnable() {
	           public void run() {
	        	   Release release = Release.find.byId(1l);
	               assertThat(release).isNotNull();
	           }
	        });
	    }

	    @Test
	    public void reconcile() {
	        running(fakeApplication(), new Runnable() {
		           public void run() {
		        	   Release release = Release.find.byId(1l);
		        	   int initial = Burndown.find.all().size();
		        	   Release newRelease = new Release("Grays");
		        	   Team newTeam = new Team("Entergize");
		        	   Iteration newIteration = new Iteration("2012-06-08");
		        	   //'2012-06-07', 38, 30, 16, 1
		        	   Burndown newBurndown = new Burndown();
		        	   newBurndown.day = "2012-06-04";
		        	   newBurndown.hours = 21;
		        	   newBurndown.ideal = 0;
		        	   newBurndown.points = 6;
		        	   newIteration.burndowns.add(newBurndown);
		        	   newTeam.iterations.add(newIteration);
		        	   newRelease.teams.add(newTeam);
		        	   release.merge(newRelease);
		        	   assertThat(Burndown.find.all().size()).isEqualTo(initial+1);
		           }
		        });
	    }
}