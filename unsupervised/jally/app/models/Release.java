package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;


/**
 * release entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Release extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;

    @OneToMany
    public List<Team> teams;
    
    /**
     * Business methods
     */
    
    
    /**
     * Generic query helper for entity Company with id Long
     */
    public static Model.Finder<Long,Release> find = new Model.Finder<Long,Release>(Long.class, Release.class);

    /**
     * Static query methods
     */
    
    
    /**
     * Answer back a list of scrum objects for a given release
     * 
     * @param release
     * @return
     */
    public static List<Release> findByRelease(String release) {
        return find.where().eq("release", release).findList();
    }
}

