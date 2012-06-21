package models;

import java.util.List;

import javax.persistence.*;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

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
    
    public String objId;
    
    @Constraints.Required
    public String name;

    @ManyToOne
    public Release release;
    
    @OneToMany(cascade=CascadeType.ALL)
    public List<Iteration> iterations;
    
    public Team(serialized.Project project) {
    	this.name = project._refObjectName;
    	this.objId = project._ref;
    	this.iterations = Lists.newArrayList();
    }

    public Team(String name) {
    	this.name = name;
    	this.iterations = Lists.newArrayList();
    }
    
    /**
     * Business methods
     */
    
    public void merge(Team src) {
    	for (Iteration srcIteration : src.iterations) {
    		Iteration exists = matching(srcIteration);
    		if (null != exists) {
    			exists.merge(srcIteration);
    		} else {
    			srcIteration.team = this;
    			iterations.add(srcIteration);
    			srcIteration.save();
    		}
    	}
    }
    
    protected Iteration matching(Iteration src) {
    	for (Iteration dst : iterations) {
    		if (dst.equals(src)) {
    			return dst;
    		}
    	}
    	return null;
    }
    
    @Override
    public boolean equals(Object dst) {
    	if (this == dst) return true;
    	if (null == dst) return false;
    	if (!(dst instanceof Team)) return false;
    	Team dstTeam = (Team)dst;
    	if (!StringUtils.equals(dstTeam.objId, this.objId)) {
    		return false;
    	}
    	return true;
    }
    
    
    /**
     * answer back the iteration matching given day
     * 
     * @param day
     * @return
     */
    public Iteration iterationNamed(String name) {
    	for (Iteration iteration : iterations) {
    		if (StringUtils.equals(iteration.name, name)) {
    			return iteration;
    		}
    	}
    	return null;
    }
    
    /**
     * Generic query helper for entity Team with id Long
     */
    public static Model.Finder<Long,Team> find = new Model.Finder<Long,Team>(Long.class, Team.class);

    /**
     * Static query methods
     */
    
    /**
     * Answer back team of given name
     * 
     * @param release
     * @return
     */
    public static Team getByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
}

