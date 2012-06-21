package jobs;

import reports.Dashboard;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
/**
 * Actor for performing various notifications
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
				  Dashboard dashboard = new Dashboard(DashboardType.Scrum);
				  String recipient = "bademail";
				  // response back to trigger
				  sender().tell(dashboard);
				  // do the work
				  new Notifier().byEmail(dashboard.generate(), recipient);
			  } else if (NotificationType.Download.equals(notificationType)) {
				  // response back to trigger
				  sender().tell(new Dashboard(DashboardType.Master).generate());
			  } else if (NotificationType.Query.equals(notificationType)) {
				  // response back to trigger
				  sender().tell("ok");
				  // do the work
				  new Collector().snapshot();
			  } else {
				  unhandled(notificationType);
			  }
		  } else {
		    	unhandled(notificationType);
		  }
	  }
}
