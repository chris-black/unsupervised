package controllers;

import java.util.List;

import models.Burndown;
import models.Iteration;
import models.Scale;
import models.Service;
import reports.ScaleReport;
import reports.ServiceReport;
import jobs.NotificationActor;
import jobs.NotificationType;
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
 * Simple form processing to update Scale and Service tables
 */
public class Admin extends Controller {
    
    public static Result index() {
		List<Scale> scaleData = Scale.all();
		List<Service> serviceData = Service.all();
		List<Iteration> iterations = Iteration.getTeamIterations();
        return ok(admin.render((ScaleReport)(new ScaleReport("Portal", scaleData).generate()),(ServiceReport)(new ServiceReport("Services", serviceData).generate()),iterations));
    }

    public static Result showScale(Long id) {
    	Form<Scale> scaleForm = form(Scale.class);
		Scale scale = Scale.findById(id);
        return ok(editscale.render(id, scaleForm.fill(scale)));
    }
    
    public static Result updateScale(Long id) {
		Form<Scale> form = form(Scale.class).bindFromRequest();
		Scale updated = form.get();
		updated.update(id);
    	return redirect("/admin");
    }

    public static Result showService(Long id) {
    	Form<Service> serviceForm = form(Service.class);
    	Service service = Service.findById(id);
        return ok(editservice.render(id, serviceForm.fill(service)));
    }
    
    public static Result updateService(Long id) {
		Form<Service> form = form(Service.class).bindFromRequest();
		Service updated = form.get();
		updated.update(id);
    	return redirect("/admin");
    }
    
    public static Result showBurndown(Long id) {
    	Iteration iteration = Iteration.find.byId(id);
    	String name = iteration.team.name;
    	return ok(addburndown.render(iteration, form(Burndown.class)));
    }

    public static Result addBurndown(Long id) {
    	Form<Burndown> form = form(Burndown.class).bindFromRequest();
    	Iteration iteration = Iteration.find.byId(id);
    	Burndown added = form.get();
    	iteration.addBurndown(added);
    	return redirect("/admin");
    }
    
    public static Result refreshRally() {
		// set up Actor to do work
		ActorRef worker = Akka.system().actorOf(new Props(NotificationActor.class));
		Future<Object> f = Patterns.ask(worker, NotificationType.Query, new Timeout(Duration.parse("10 seconds")));
		// Play uses promise, Akka uses their own future
		Promise<Object> p = Akka.asPromise(f);
		// async return for promise of result
		// promise comes from Function using anonymous method
		return async(p.map(new Function<Object, Result>() {
			public Result apply(Object response) {
				return redirect("/admin");
			}
		}));
    }
}
