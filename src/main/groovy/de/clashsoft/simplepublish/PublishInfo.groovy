package de.clashsoft.simplepublish

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

class PublishInfo {
	String[] labels

	String websiteUrl
	String issueTrackerUrl
	String vcsUrl
	String githubRepo

	License license
	Developer developer

	@javax.inject.Inject
	PublishInfo(ObjectFactory objectFactory) {
		license = objectFactory.newInstance(License)
		developer = objectFactory.newInstance(Developer)
	}

	void license(Action<? super License> action) {
		action.execute(license)
	}

	void developer(Action<? super Developer> action) {
		action.execute(developer)
	}
}

class License {
	String shortName
	String longName
	String url
}

class Developer {
	String id
	String name
	String email
}
