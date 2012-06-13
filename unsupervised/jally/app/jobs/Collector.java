package jobs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import models.Burndown;
import models.Iteration;
import models.Release;
import models.Team;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import serialized.ResultFromJson;
import serialized.ResultFromJsonStory;

/**
 * 
 * https://rally1.rallydev.com/slm/webservice/1.34/iteration.js?query=((StartDate%20%3C%20%222012-05-19%22)%20AND%20(EndDate%20%3E%3D%20%222012-05-19%22))&fetch=true&start=1&pagesize=50
 * from
 * https://rally1.rallydev.com/slm/doc/webservice/
 * @author dlange
 *
 */
public class Collector {
	// some queries
	private String iterationBase = "https://rally1.rallydev.com/slm/webservice/1.34/iteration.js";
	private String storyBase = "https://rally1.rallydev.com/slm/webservice/1.34/hierarchicalrequirement.js";

	
	public void snapshot() throws JsonParseException, JsonMappingException, IOException {
		new Reconcile().reconcileModel(queryData());
	}

	private String today() {
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(rightNow.getTime());
	}
	
	private boolean srcAfter(String src, String dst) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date srcDate;
		try {
			srcDate = formatter.parse(src);
			Date dstDate = formatter.parse(dst);
			if (srcDate.after(dstDate)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private Map<String,String> queryParams(String query) {
		Map<String,String> params = Maps.newHashMap();
		params.put("query", query);
		params.put("fetch", "true");
		params.put("pageSize", "100");
		return params;
	}
	
	/**
	 * get list of serializedIterations which overlap today
	 * if there are multiple iterations per team, pick the latest iteration
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	private List<serialized.Iteration> queryOverlap() throws JsonParseException, JsonMappingException, IOException {
		List<serialized.Iteration> iterations = Lists.newArrayList();
		ObjectMapper mapper = new ObjectMapper();
		mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		Map<String,serialized.Iteration> teams = Maps.newHashMap();
		ResultFromJson results = mapper.readValue(singleQuery(iterationBase, queryParams("((StartDate < \""+today()+"\") AND (EndDate > \""+today()+"\"))")), ResultFromJson.class);
		for (serialized.Iteration srcIter : results.QueryResult.Results) {
			if (teams.containsKey(srcIter.Project._ref)) {
				// keep later iteration (multiple iterations per team)
				serialized.Iteration existing = teams.get(srcIter.Project._ref);
				if (srcAfter(srcIter.EndDate, existing.EndDate)) {
					iterations.remove(existing);
					iterations.add(srcIter);
					teams.put(srcIter.Project._ref, srcIter);
				} 
			} else {
				iterations.add(srcIter);
				teams.put(srcIter.Project._ref, srcIter);
			}
		}
		return iterations;
	}

	/**
	 * using iterations as anchor, find stories per iteration then use stories to identify release / team
	 * iteration also has team 

	 * @param serializedIterations
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private List<Iteration> populateIterations(List<serialized.Iteration> serializedIterations) throws JsonParseException, JsonMappingException, IOException {
		Release defaultRelease = null;
		List<Iteration> iterations = Lists.newArrayList();
		for (serialized.Iteration serializedIteration : serializedIterations) {
			String iterationName = "(Iteration.ObjectID = \""+serializedIteration.ObjectID+"\")";
			ObjectMapper mapper = new ObjectMapper();
			mapper.getDeserializationConfig().disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
			// TODO use multipleQuery
			ResultFromJsonStory results = mapper.readValue(singleQuery(storyBase, queryParams(iterationName)), ResultFromJsonStory.class);
			
			// sum up points hrs etc
			Iteration iteration = new Iteration(serializedIteration);
			Burndown burndown = new Burndown();
			burndown.day = today();
			burndown.connect(iteration);
			for (serialized.HierarchicalRequirement story : results.QueryResult.Results) {
				iteration.totalPoints += story.PlanEstimate;
				iteration.totalHours += story.TaskEstimateTotal;
				if (StringUtils.equalsIgnoreCase(story.ScheduleState, "Accepted")) {
					burndown.points += story.PlanEstimate;
				}
				burndown.hours += story.TaskRemainingTotal;
			}
			// hook in story release to iteraton's team
			Release iterationRelease = inflateParent(results, iteration);
			if (null != iterationRelease) {
				defaultRelease = iterationRelease;
			}
			// add to return list
			iterations.add(iteration);
			Logger.info("query burndown:"+burndown.day+" "+burndown.hours+" "+burndown.points+" for itr:"+iteration.objId);
		}
		// fill in gaps on release
		for (Iteration iteration : iterations) {
			if (null == iteration.team.release) {
				defaultRelease.teams.add(iteration.team);
				iteration.team.release = defaultRelease;
			}
		}
		return iterations;
	}
	
	public Release queryData() throws JsonParseException, JsonMappingException, IOException {
		return mergeParents(populateIterations(queryOverlap()));
	}
	
	/**
	 * Given a list of Iterations with burndown collections, identify common parent team/release structure
	 * get unique teams, make sure releases are consistent
	 * @param iterations
	 * @return
	 */
	protected Release mergeParents(List<Iteration> iterations) {
		Release parent = null;
		Map<String,Release> releases = Maps.newHashMap();
		for (Iteration iteration : iterations) {
			if (null != iteration.team.release) {
				if (!releases.containsKey(iteration.team.release.name)) {
					releases.put(iteration.team.release.name, iteration.team.release);
				}
			}
		}
		// TODO deal with multiple correctly
		if (releases.size() > 0) {
			parent = releases.values().iterator().next();
			parent.teams.clear();
			for (Iteration iteration : iterations) {
				if (iteration.reportable()) {
					parent.addTeam(iteration.team);
				}
			}
		}
		return parent;
	}
	
	protected Release storyRelease(ResultFromJsonStory results) {
		for (serialized.HierarchicalRequirement story : results.QueryResult.Results) {
			if (null != story.Release && !StringUtils.isBlank(story.Release._refObjectName) && !StringUtils.equalsIgnoreCase("null", (story.Release._refObjectName))) {
				return new Release(story.Release);
			} 
		}
		return null;
	}
	
	protected Release inflateParent(ResultFromJsonStory results, Iteration iteration) {
		Release release = null;
		if (null != iteration.team) {
			release = storyRelease(results);
			if (null != release ) {
				release.teams.add(iteration.team);
				iteration.team.release = release;
			}
		}
		return release;
	}
	
	protected String getUser() {
		return Play.application().configuration().getString("rally.user");
	}
	protected String getPassword() {
		return Play.application().configuration().getString("rally.password");
	}
	
	protected String singleQuery(String query, Map<String,String> params) {
		WSRequestHolder req = WS.url((String)query);
		// add params
		for (String key : params.keySet()) {
			req.setQueryParameter(key, params.get(key));
		}
		final Promise<Response> remoteCall = req.setAuth(getUser(), getPassword(), com.ning.http.client.Realm.AuthScheme.BASIC).get();
		return remoteCall.get().getBody();
	}
	
	protected List<String> multipleQuery(List<String> urls) {
		List<String> bodies = Lists.newArrayList();
		final Promise<Response> remoteCall1 = WS.url(urls.get(0)).setAuth(getUser(), getPassword(),  com.ning.http.client.Realm.AuthScheme.BASIC).get();
		final Promise<Response> remoteCall2 = WS.url(urls.get(1)).setAuth(getUser(), getPassword(),  com.ning.http.client.Realm.AuthScheme.BASIC).get();
		  
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
			bodies.add(r.getBody());
			Logger.info(r.getBody());
		}
		return bodies;
	}
}
