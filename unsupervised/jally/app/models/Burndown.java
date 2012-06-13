package models;

import java.util.Map;

import javax.persistence.*;

import com.google.common.collect.Maps;

import play.db.ebean.*;


/**
 * Burndown entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Burndown extends Model {

    @Id
    public Long id;
    
    public String day;
    
    public int hours;
    
    public int ideal;
    
    public int points;
    
    @ManyToOne
    public Iteration iteration;
    
    public Burndown() {
    }
    
    public Burndown(String day, Iteration iteration) {
    	this.day = day;
    	this.hours = 0;
    	this.ideal = 0;
    	this.points = 0;
    	this.iteration = iteration;
    }
    
    /**
     * hook up this burndown to an iteration 
     */
    public void connect(Iteration parent) {
    	parent.burndowns.add(this);
    	this.iteration = parent;
    }
    
    /**
     * Generic query helper for entity Company with id Long
     */
    public static Model.Finder<Long,Burndown> find = new Model.Finder<Long,Burndown>(Long.class, Burndown.class);

    /**
     * Static query methods
     */
    
    
    /**
     * Answer back the latest day burndown for a given project/iteration
     * 
     * @param project
     * @param iteration
     * @return
     */
    public static Burndown findLatestByProjectIteration(String project) {
    	return null;
//        return find.where().eq("project", project).orderBy("day desc").findList().get(0);
    }
    
    /**
     * Go through all burndowns looking for matching iteration
     * 
     * @param project
     * @return
     */
    public static Burndown findCurrentByProjectIteration(String project) {
    	return null;
    }

    /**
     * Answer back burndown of given day for an iteration
     * 
     * @param release
     * @return
     */
    public static Burndown getByDay(String day, Iteration iteration) {
		Map<String,Object> constraints = Maps.newHashMap();
		constraints.put("day", day);
		constraints.put("iteration_id", iteration.id);
		return find.where().allEq(constraints).findUnique();
    }
}

