package jobs;

import com.avaje.ebean.Ebean;

import play.Logger;
import models.Release;

public class Reconcile {
	
	/**
	 * walk given model, save entities as needed
	 * 
	 * @param release
	 */
	public void reconcileModel(Release release) {
		// check release
		Release existingRelease = Release.getByName(release.name);
		if (null != existingRelease) {
			existingRelease.merge(release);
			Ebean.save(existingRelease);
		} else {
			// simple- cascade save
			Ebean.save(release);
			Logger.info("Saved new release:"+release.name);
		}
	}
}
