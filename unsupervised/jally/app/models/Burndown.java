package models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;


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

}

