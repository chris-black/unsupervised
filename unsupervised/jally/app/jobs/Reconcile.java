package jobs;

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
			existingRelease.save();
		} else {
			// simple- cascade save
			release.save();
		}
	}
}
