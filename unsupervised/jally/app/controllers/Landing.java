package controllers;

import reports.Dashboard;
import jobs.DashboardType;
import jobs.DashboardActor;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

/**
 * @author dlange
 *
 */
public class Landing extends Controller {
    
	/**
	 * Draw the master dashboard page
	 * @return
	 */
    public static Result dashboard() {
    	return drawDashboard(DashboardType.Master);
    }

    /**
     * Draw the scrum dashboard
     * @return
     */
    public static Result scrum() {
    	return drawDashboard(DashboardType.Scrum);
    }

    /**
     * Generate Result for dashboard view based on dashboard type
     * @param dashboardType
     * @return
     */
    private static Result drawDashboard(DashboardType dashboardType) {
		// set up Actor to do work
		ActorRef worker = Akka.system().actorOf(new Props(DashboardActor.class));
		Future<Object> f = Patterns.ask(worker, dashboardType, new Timeout(Duration.parse("10 seconds")));
		// Play uses promise, Akka uses their own future
		Promise<Object> p = Akka.asPromise(f);
		// async return for promise of result
		// promise comes from Function using anonymous method
		return async(p.map(new Function<Object, Result>() {
			public Result apply(Object response) {
				if (response instanceof Dashboard) {
					return ok(views.html.dashboard.render((Dashboard)response));
				}
				return redirect(routes.Landing.dashboard());
			}
		}));
    }

}
