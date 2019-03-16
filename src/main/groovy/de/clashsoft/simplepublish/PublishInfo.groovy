package de.clashsoft.simplepublish

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomLicense

class PublishInfo {
	private final Project project

	final Property<String> organization
	final ListProperty<String> labels

	final Property<String> websiteUrl
	final Property<String> issueTrackerUrl
	final Property<String> vcsUrl
	final Property<String> githubRepo

	PublishInfo(Project project) {
		this.project = project

		organization = project.objects.property(String)
		labels = project.objects.listProperty(String)

		websiteUrl = project.objects.property(String)
		issueTrackerUrl = project.objects.property(String)
		vcsUrl = project.objects.property(String)
		githubRepo = project.objects.property(String)
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
