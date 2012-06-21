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
    if (Release.find.all().size() > 0) {
    	ActorRef ref = Akka.system().actorOf(new Props(NotificationActor.class));
        Akka.system().scheduler().schedule(
                Duration.Zero(),
                Duration.create(24, TimeUnit.HOURS),
                ref, 
                NotificationType.Query
            	);
    }
  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}