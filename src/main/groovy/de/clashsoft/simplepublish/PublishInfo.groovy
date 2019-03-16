package de.clashsoft.simplepublish

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomLicense

class PublishInfo {
	private final Project project

	String organization
	String[] labels

	String websiteUrl
	String issueTrackerUrl
	String vcsUrl
	String githubRepo

	PublishInfo(Project project) {
		this.project = project
	}

	void license(Action<? super MavenPomLicense> action) {
		project.publishing.publications[project.name].pom.licenses {
			license(action)
		}
	}

	void developer(Action<? super MavenPomDeveloper> action) {
		project.publishing.publications[project.name].pom.developers {
			developer(action)
		}
	}
}
