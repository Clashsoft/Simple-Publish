package de.clashsoft.simplepublish

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPomDeveloper
import org.gradle.api.publish.maven.MavenPomLicense

@CompileStatic
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

		// default values
		websiteUrl.set(githubRepo.map { 'https://github.com/' + it })
		issueTrackerUrl.set(githubRepo.map { 'https://github.com/' + it + '/issues' })
		vcsUrl.set(githubRepo.map { 'https://github.com/' + it })
	}

	void license(Action<? super MavenPomLicense> action) {
		SimplePublishPlugin.getDefaultPublication(project).pom.licenses {
			it.license(action)
		}
	}

	void developer(Action<? super MavenPomDeveloper> action) {
		SimplePublishPlugin.getDefaultPublication(project).pom.developers {
			it.developer(action)
		}
	}
}
