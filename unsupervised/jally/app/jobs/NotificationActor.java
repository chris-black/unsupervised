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
public class NotificationActor extends UntypedActor {
	  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	 
	  /**
	   * Receive a message with notification type; process based on message type
	   * schedule job
	   * collect / persist
	   * report 
	   * untyped with object interface seems to have its place here but need to clarify
	   */
	  public void onReceive(Object notificationType) throws Exception {
		  log.info("Received notificationType: {}", notificationType);
		  if (notificationType instanceof NotificationType) {
			  if (NotificationType.Email.equals(notificationType)) {
				  Dashboard dashboard = new Dashboard("Grays");
				  // response back to trigger
				  sender().tell(dashboard);
				  // do the work
				  new Notifier().byEmail(dashboard.generate());
			  } else if (NotificationType.Download.equals(notificationType)) {
				  // response back to trigger
				  sender().tell(new Dashboard("Grays").generate());
			  } else if (NotificationType.Query.equals(notificationType)) {
				  // do the work
				  new Collector().snapshot();
				  // response back to trigger
				  sender().tell(new Dashboard("Grays").generate());
			  } else {
				  unhandled(notificationType);
			  }
		  } else {
		    	unhandled(notificationType);
		  }
	  }
}
