package jobs;

import java.util.ArrayList;
import java.util.List;


import play.Logger;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS.Response;

public class Collector {
	// state
	public boolean collected = false;
	public boolean generated = false;
	
	// model
	String htmlReport;
	
	public Collector() {
	}

	public Collector collect() {
		String url="https://rally1.rallydev.com/slm/webservice/1.31/hierarchicalrequirement.js?query=((Project.Name%20=%20%22Paper%20Reports%22)%20and%20(Iteration.Name%20=%202012-03-05))&fetch=true&start=1&pagesize=100";

		  final Promise<Response> remoteCall1 = WS.url((String)url).setAuth("user", "passwd",  com.ning.http.client.Realm.AuthScheme.BASIC).get();
		  final Promise<Response> remoteCall2 = WS.url((String)url).setAuth("user", "passwd",  com.ning.http.client.Realm.AuthScheme.BASIC).get();
		  
		  Promise<List<Response>> httpResponses = remoteCall1.flatMap(new Function<Response, Promise<List<Response>>>() {
			public Promise<List<Response>> apply(final Response a) throws Throwable {
				return remoteCall2.map(new Function<Response,List<Response>>() {
			        public List<Response> apply(Response response) {
				          List<Response> results = new ArrayList<Response>();
				          results.add(a);
				          results.add(response);
				          return results;
			        }
			      }
			    );
			}
		  });
		  List<Response> rs = httpResponses.get();
		  for (Response r : rs) {
			  Logger.info(r.getBody());
		  }
		  // set model parameters
		  collected = true;
		
		return this;
	}
	
}
