import java.util.concurrent.TimeUnit;

import jobs.NotificationType;
import jobs.RallyActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.util.Duration;
import play.*;
import play.libs.Akka;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
    Logger.info("Application has started");
	ActorRef ref = Akka.system().actorOf(new Props(RallyActor.class));
    Akka.system().scheduler().schedule(
        Duration.Zero(),
        Duration.create(1, TimeUnit.MINUTES),
        ref, 
        NotificationType.Query
    	);

  }  
  
  @Override
  public void onStop(Application app) {
    Logger.info("Application shutdown...");
  }  
    
}