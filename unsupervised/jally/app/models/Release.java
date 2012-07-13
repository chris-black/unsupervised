package models;

import java.util.List;

import javax.persistence.*;
import com.google.common.collect.Lists;

import play.Logger;
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

    @OneToMany(cascade=CascadeType.ALL)
    public List<Team> teams;
    
    public Release(serialized.Release release) {
    	this.name = release._refObjectName;
    	this.teams = Lists.newArrayList();
    }
    
    public Release(String name) {
    	this.name = name;
    	this.teams = Lists.newArrayList();
    }
    
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append(name+"\n");
    	for (Team team : teams) {
    		buf.append(team).append("\n");
    	}
    	return buf.toString();
    }
    /**
     * Business methods
     */
    public Release addTeam(Team src) {
    	teams.add(src);
    	src.release = this;
    	return this;
    }
    
    /**
     * Merge in source graph to this graph
     * @param src
     */
    public void merge(Release src) {
    	for (Team srcTeam : src.teams) {
    		Team exists = matching(srcTeam);
    		if (null != exists) {
    			exists.merge(srcTeam);
    		} else {
    			addTeam(srcTeam);
    			Logger.info("  Team did not exist, added to release:"+srcTeam.name);
    		}
    	}
    }
    
    protected Team matching(Team src) {
    	for (Team dst : teams) {
    		if (dst.equals(src)) {
    			return dst;
    		}
    	}
    	return null;
    }
    
    /**
     * Generic query helper for entity Release with id Long
     */
    public static Model.Finder<Long,Release> find = new Model.Finder<Long,Release>(Long.class, Release.class);

    /**
     * Static query methods
     */
    
    
    /**
     * Answer back a list of release objects for a given release
     * 
     * @param release
     * @return
     */
    public static Release getByName(String name) {
        return find.where().eq("name", name).findUnique();
    }
}

