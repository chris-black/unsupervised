package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;


/**
 * Scrum entity managed by Ebean
 */
@SuppressWarnings("serial")
public class Scrum extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;

    public String release;
    
    public String currentIteration;
    
    
    /**
     * Generic query helper for entity Scrum with id Long
     */
    public static Model.Finder<Long,Scrum> find = new Model.Finder<Long,Scrum>(Long.class, Scrum.class);

    /**
     * Static query methods
     */
    
    
    /**
     * Answer back a list of scrum objects for a given release
     * 
     * @param release
     * @return
     */
    public static List<Scrum> findByRelease(String release) {
        return find.where().eq("release", release).findList();
    }
}

