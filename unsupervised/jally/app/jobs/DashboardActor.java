package jobs;

import reports.Dashboard;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
/**
 * Actor for collecting responses from a series of web service calls
 * @author dlange
 *
 */
public class DashboardActor extends UntypedActor {
	  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	 
	  /**
	   * Receive a message with notification type; process based on message type
	   * schedule job
	   * collect / persist
	   * report 
	   * untyped with object interface seems to have its place here but need to clarify
	   */
	  public void onReceive(Object dashboardType) throws Exception {
		  log.info("Received DashboardType: {}", dashboardType);
		  if (dashboardType instanceof DashboardType) {
			  // response back to trigger
			  sender().tell(new Dashboard((DashboardType)dashboardType).generate());
		  } else {
			  unhandled(dashboardType);
		  }
	  }
}
