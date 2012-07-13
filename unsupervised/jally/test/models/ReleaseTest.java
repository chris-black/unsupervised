package models;

import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import jobs.Reconcile;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.libs.Yaml;
import play.test.FakeApplication;
import play.test.Helpers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.H2Platform;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;


public class ReleaseTest {
	
	public static FakeApplication app;
	public static DdlGenerator ddl;
	public static EbeanServer server;

	@BeforeClass
	public static void setup() {
		app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
		Helpers.start(app);

		server = Ebean.getServer("default");

		ServerConfig config = new ServerConfig();
		config.setDebugSql(true);

		ddl = new DdlGenerator((SpiEbeanServer) server, new H2Platform(), config);
	}

	@AfterClass
	public static void stopApp() {
		Helpers.stop(app);
	}

	@Before
	public void resetDb() throws IOException {

		// drop
		String dropScript = ddl.generateDropDdl();
		ddl.runScript(false, dropScript);

		// create
		String createScript = ddl.generateCreateDdl();
		ddl.runScript(false, createScript);

		// insert data
		Map<String, List<Object>> all = (Map<String, List<Object>>) Yaml.load("ftest.yml");
		Ebean.save(all.get("releases"));
		Ebean.save(all.get("teams"));
		Ebean.save(all.get("iterations"));
		Ebean.save(all.get("burndowns"));
	}


	
	    @Test
	    public void getByName() {
     	   Release release = Release.getByName("grays");
           assertThat(release).isNotNull();
	    }

	    @Test
	    public void reconcileRelease() {
     	   Release release = Release.getByName("grays");

     	   Release newRelease = new Release("grays++");
     	   Team newTeam = new Team("Red");
     	   newTeam.objId = "Red";
     	   Iteration newIteration = new Iteration("2012/06/17");
     	   newIteration.objId = "two";
     	   //'2012-06-07', 38, 30, 16, 1
     	   Burndown newBurndown = new Burndown();
     	   
     	   // use today- does not matter iteration end here
     	   Calendar rightNow = Calendar.getInstance();
  		   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
  		   String day = formatter.format(rightNow.getTime());
     	   newBurndown.day = day;
     	   newBurndown.hours = 21;
     	   newBurndown.ideal = 0;
     	   newBurndown.points = 6;
     	   
     	   newIteration.burndowns.add(newBurndown);
     	   newTeam.iterations.add(newIteration);
     	   newRelease.teams.add(newTeam);
     	   
     	   new Reconcile().reconcileModel(newRelease);
     	   
     	   // 2 existing plus this new one
     	   assertThat(Release.find.all().size()).isEqualTo(2);
	    }

	    @Test
	    public void reconcileTeam() {
     	   Release release = Release.getByName("grays");

     	   Release newRelease = new Release("grays");
     	   Team newTeam = new Team("Blue");
     	   newTeam.objId = "Blue";
     	   Iteration newIteration = new Iteration("2012/06/17");
     	   newIteration.objId = "two";
     	   //'2012-06-07', 38, 30, 16, 1
     	   Burndown newBurndown = new Burndown();
     	   
     	   // use today- does not matter iteration end here
     	   Calendar rightNow = Calendar.getInstance();
  		   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
  		   String day = formatter.format(rightNow.getTime());
     	   newBurndown.day = day;
     	   newBurndown.hours = 21;
     	   newBurndown.ideal = 0;
     	   newBurndown.points = 6;
     	   
     	   newIteration.burndowns.add(newBurndown);
     	   newTeam.iterations.add(newIteration);
     	   newRelease.teams.add(newTeam);

     	   new Reconcile().reconcileModel(newRelease);

     	   // 2 existing plus this new one
     	   assertThat(Team.find.all().size()).isEqualTo(2);
	    }

	    @Test
	    public void reconcileIteration() {
     	   Release release = Release.getByName("grays");

     	   Release newRelease = new Release("grays");
     	   Team newTeam = new Team("Red");
     	   newTeam.objId = "Red";
     	   Iteration newIteration = new Iteration("2012/07/02");
     	   newIteration.objId = "two";
     	   //'2012-06-07', 38, 30, 16, 1
     	   Burndown newBurndown = new Burndown();
     	   
     	   // use today- does not matter iteration end here
     	   Calendar rightNow = Calendar.getInstance();
  		   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
  		   String day = formatter.format(rightNow.getTime());
     	   newBurndown.day = day;
     	   newBurndown.hours = 21;
     	   newBurndown.ideal = 0;
     	   newBurndown.points = 6;
     	   
     	   newIteration.burndowns.add(newBurndown);
     	   newTeam.iterations.add(newIteration);
     	   newRelease.teams.add(newTeam);
     	   
     	   new Reconcile().reconcileModel(newRelease);
     	   // 2 existing plus this new one
     	   assertThat(Iteration.find.all().size()).isEqualTo(2);
	    }
	    @Test
	    public void reconcileBurndown() {
     	   Release release = Release.getByName("grays");

     	   Release newRelease = new Release("grays");
     	   Team newTeam = new Team("Red");
     	   newTeam.objId = "Red";
     	   Iteration newIteration = new Iteration("2012/06/17");
     	   newIteration.objId = "one";
     	   //'2012-06-07', 38, 30, 16, 1
     	   Burndown newBurndown = new Burndown();
     	   
     	   // use today- does not matter iteration end here
     	   Calendar rightNow = Calendar.getInstance();
  		   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
  		   String day = formatter.format(rightNow.getTime());
     	   newBurndown.day = day;
     	   newBurndown.hours = 21;
     	   newBurndown.ideal = 0;
     	   newBurndown.points = 6;
     	   
     	   newIteration.burndowns.add(newBurndown);
     	   newTeam.iterations.add(newIteration);
     	   newRelease.teams.add(newTeam);

     	   new Reconcile().reconcileModel(newRelease);
     	   // 2 existing plus this new one
     	   assertThat(Burndown.find.all().size()).isEqualTo(3);
	    }

}