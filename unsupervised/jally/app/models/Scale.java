package models;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import play.db.ebean.*;


/**
 * Scale entity managed by Ebean
 * portal goal/month, actual/month
 * han goal/month, actual/month
 */
@SuppressWarnings("serial")
@Entity 
public class Scale extends Model {

    @Id
    public Long id;
    
    public Date month;
    
    public int portalGoal;
    
    public int portalActual;
    
    public int hanGoal;
    
    public int hanActual;
    
    public Scale() {
    }
    
    
    /**
     * Generic query helper for entity Scale with id Long
     */
    public static Model.Finder<Long,Scale> find = new Model.Finder<Long,Scale>(Long.class, Scale.class);

    /**
     * Static query methods
     */
    public static List<Scale> all() {
    	return find.order().asc("month").findList();
    }
    
    public static Scale findById(Long id) {
    	return find.byId(id);
    }
}

