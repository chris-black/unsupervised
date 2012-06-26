package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;


/**
 * Service entity managed by Ebean
 * %complete, avg throughput per month
 */
@SuppressWarnings("serial")
@Entity 
public class Service extends Model {

    @Id
    public Long id;
    
    public Date month;
    
    public int percentComplete;
    
    public int percentGoal;
    
    public int avgThroughput;
    
    public int throughputGoal;
    
    public Service() {
    }
    
    
    /**
     * Generic query helper for entity Service with id Long
     */
    public static Model.Finder<Long,Service> find = new Model.Finder<Long,Service>(Long.class, Service.class);

    /**
     * Static query methods
     */
    public static List<Service> all() {
    	return find.order().asc("month").findList();
    }
    
    public static Service findById(Long id) {
    	return find.byId(id);
    }
}

