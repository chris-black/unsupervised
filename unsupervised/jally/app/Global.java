import java.util.concurrent.TimeUnit;

import models.Release;

import jobs.NotificationType;
import jobs.NotificationActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Duration;
import play.*;
import play.libs.Akka;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
	ActorRef ref = Akka.system().actorOf(new Props(NotificationActor.class));
    Akka.system().scheduler().schedule(
            Duration.create(1, TimeUnit.MINUTES),
            Duration.create(2, TimeUnit.HOURS),
            ref, 
            NotificationType.Query
        	);
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}