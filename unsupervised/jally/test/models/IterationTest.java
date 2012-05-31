package models;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.List;

import org.junit.Test;


public class IterationTest {

	    @Test
	    public void getTeamIterations() {
	        running(fakeApplication(), new Runnable() {
	           public void run() {
	        	   List<Iteration> iterations = Iteration.getTeamIterations();
	               assertThat(iterations).isNotNull();
	           }
	        });
	    }
}