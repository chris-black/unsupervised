package controllers;

import reports.Dashboard;
import jobs.DashboardType;
import jobs.NotificationType;
import jobs.RallyActor;
import play.Logger;
import play.data.Form;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;

/**
 * scheduling daily sync / email
* release/project - collect
* report on data persist
settings tab 
* create release
* create iteration
autodetect iteration
email on demand
 * @author dlange
 *
 */
public class Landing extends Controller {
	/**
	 * Simple class to wrap in a form. Scala enum binding problems? use string and convert for now
	 */
	public static class Notify {
		public String notifier;
	} 
    
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
    
    private static Result drawDashboard(DashboardType notificationType) {
		// set up Actor to do work
		ActorRef worker = Akka.system().actorOf(new Props(RallyActor.class));
		Future<Object> f = Patterns.ask(worker, notificationType, new Timeout(Duration.parse("10 seconds")));
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
    
    public static Result admin() {
        return ok(admin.render(form(Notify.class)));
    }

    /**
     * form post trigger to kick off the work
     * 
     * @return
     */
	public static Result trigger() {
		Form<Notify> form = form(Notify.class).bindFromRequest();
		Logger.info("notifier:"+form.get().notifier);
		if (form.hasErrors()) {
			return badRequest(admin.render(form));
		} else {
			// set up Actor to do work
			ActorRef worker = Akka.system().actorOf(new Props(RallyActor.class));
			Future<Object> f = Patterns.ask(worker, NotificationType.valueOf(form.get().notifier), new Timeout(Duration.parse("10 seconds")));
			// Play uses promise, Akka uses their own future
			Promise<Object> p = Akka.asPromise(f);
			// async return for promise of result
			// promise comes from Function using anonymous method
			return async(p.map(new Function<Object, Result>() {
				public Result apply(Object response) {
					if (response instanceof Dashboard) {
						return ok(views.html.dashboard.render((Dashboard)response));
					}
					return redirect(routes.Landing.admin());
				}
			}));
		}
	}

}
