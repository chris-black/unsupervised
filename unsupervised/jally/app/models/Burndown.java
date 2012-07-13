package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

	private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");

    public Burndown() {
    	this.day = today();
    }
    
	private String today() {
		Calendar rightNow = Calendar.getInstance();
		return formatter.format(rightNow.getTime());
	}

    public Burndown(String day, Iteration iteration) {
    	this.day = day;
    	this.hours = 0;
    	this.ideal = 0;
    	this.points = 0;
    	this.iteration = iteration;
    }
    

    public String toString() {
    	return ((this.id != null)?this.id:"x")+":"+this.day+":"+this.hours+":"+this.points;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getIdeal() {
		return ideal;
	}

	public void setIdeal(int ideal) {
		this.ideal = ideal;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	/**
     * hook up this burndown to an iteration 
     */
    public void connect(Iteration parent) {
    	parent.burndowns.add(this);
    	this.iteration = parent;
    }
    
    /**
     * Generic query helper for entity Burndown with id Long
     */
    public static Model.Finder<Long,Burndown> find = new Model.Finder<Long,Burndown>(Long.class, Burndown.class);

    /**
     * Static query methods
     */
    
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

    public static Burndown getById(long id) {
    	return find.byId(id);
    }
}

