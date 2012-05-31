package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import org.junit.Test;


public class BurndownTest {
	    @Test
	    public void findById() {
	        running(fakeApplication(), new Runnable() {
	           public void run() {
	        	   Burndown report = Burndown.find.byId(1l);
	               assertThat(report).isNotNull();
	               assertThat(report.hours).isEqualTo(120);
	           }
	        });
	    }
}