package de.clashsoft.simplepublish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

class SimplePublishPlugin implements Plugin<Project> {
	@Override
	void apply(Project target) {
		target.plugins.apply('java')
		target.plugins.apply('maven-publish')

		def publishInfo = target.extensions.create('publishInfo', PublishInfo, target)

		configureArtifactTasks(target)

		target.publishing.publications.create(target.name, MavenPublication) {
			from target.components.java
			artifact target.tasks.sourcesJar
			artifact target.tasks.javadocJar

			pom {
				name = target.provider { target.name }
				description = target.provider { target.description }
				url = publishInfo.websiteUrl
				scm {
					url = publishInfo.vcsUrl
				}
			}
		}

		target.afterEvaluate {
			target.publishing.publications."$target.name" {
				// neither Project nor Publication uses the Property API for these
				groupId = groupId ?: target.group
				artifactId = artifactId ?: target.name
				version = version ?: target.version
			}

			if (target.pluginManager.hasPlugin('com.jfrog.bintray')) {
				configureBintray(target, publishInfo)
			}
		}
	}

	private void configureArtifactTasks(Project project) {
		project.tasks.register('sourcesJar', Jar) {
			it.from project.sourceSets.main.allSource
			it.classifier = 'sources'
		}

		project.tasks.register('javadocJar', Jar) {
			it.dependsOn 'javadoc'
			it.from project.javadoc.destinationDir
			it.classifier = 'javadoc'
		}
	}

	private void configureBintray(Project project, PublishInfo info) {
		project.bintray {
			user = user ?: project.findProperty('bintray.user') ?: System.getenv('BINTRAY_USER')
			key = key ?: project.findProperty('bintray.key') ?: System.getenv('BINTRAY_KEY')
			publications = publications ?: [ project.name ]
			// dryRun = true
			// publish = true
			// override = true

			pkg {
				repo = repo ?: 'maven'
				name = name ?: project.name
				userOrg = userOrg ?: info.organization ?: null
				desc = desc ?: project.description
				websiteUrl = websiteUrl ?: info.websiteUrl
				issueTrackerUrl = issueTrackerUrl ?: info.issueTrackerUrl
				vcsUrl = vcsUrl ?: info.vcsUrl
				licenses = licenses ?: project.publishing.publications[project.name].pom.licenses*.name
				labels = labels ?: info.labels
				publicDownloadNumbers = true
				// attributes = []

				githubRepo = githubRepo ?: info.githubRepo
				githubReleaseNotesFile = githubReleaseNotesFile ?: 'CHANGELOG.md'

				version {
					name = name ?: project.version
					desc = desc ?: "$project.name v$project.version"
					released = released ?: new Date()
					vcsTag = vcsTag ?: "v$project.version"
					// attributes = []

					gpg {
						sign = true
						passphrase = passphrase ?: project.findProperty('bintray.gpg.passphrase')
								?: System.getenv('BINTRAY_GPG_PASSPHRASE')
					}
				}
			}
		}
	}
}
