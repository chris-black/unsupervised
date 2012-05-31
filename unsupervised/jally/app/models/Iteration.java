package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.google.common.collect.Lists;

import play.db.ebean.*;
import play.data.validation.*;


/**
 * Iteration entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Iteration extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;

    public Date iterationStart;
    
    public Date iterationEnd;
    
    @ManyToOne
    public Team team;
    
    @OneToMany(cascade=CascadeType.ALL)
    public List<Burndown> burndowns;
    
    /**
     * Business methods
     */


    /**
     * Generic query helper for entity Company with id Long
     */
    public static Model.Finder<Long,Iteration> find = new Model.Finder<Long,Iteration>(Long.class, Iteration.class);

    /**
     * Static query methods
     */

    /**
     * Answer back matching Iterations
     * @return
     */
    public static List<Iteration> getTeamIterations() {
    	Calendar rightNow = Calendar.getInstance();
    	return find.where().gt("iterationStart", rightNow.getTime()).le("iterationEnd", rightNow.getTime()).findList();
    }
}

