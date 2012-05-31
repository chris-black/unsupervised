package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.validation.*;


/**
 * Team entity managed by Ebean
 */
@SuppressWarnings("serial")
@Entity 
public class Team extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;

    @ManyToOne
    public Release release;
    
    @OneToMany(cascade=CascadeType.ALL)
    public List<Iteration> iterations;
    
    /**
     * Generic query helper for entity Company with id Long
     */
    public static Model.Finder<Long,Team> find = new Model.Finder<Long,Team>(Long.class, Team.class);

    /**
     * Static query methods
     */
    
}

