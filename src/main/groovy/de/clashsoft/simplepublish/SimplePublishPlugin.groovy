package de.clashsoft.simplepublish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar

class SimplePublishPlugin implements Plugin<Project> {
	@Override
	void apply(Project target) {
		if (target.pluginManager.hasPlugin('java')) {
			configureJava(target)
		}

		def publishInfo = target.extensions.create('publishInfo', PublishInfo)

		target.afterEvaluate {
			if (target.pluginManager.hasPlugin('maven-publish')) {
				configureMaven(target, publishInfo)
			}

			if (target.pluginManager.hasPlugin('com.jfrog.bintray')) {
				configureBintray(target, publishInfo)
			}
		}
	}

	private void complete(PublishInfo publishInfo) {
		/*
		if (publishInfo.githubRepo) {
			publishInfo.issueTrackerUrl ?= "https://github.com/$publishInfo.githubRepo/issues/"
			publishInfo.websiteUrl ?= "https://github.com/$publishInfo.githubRepo"
			publishInfo.vcsUrl ?= "https://github.com/$publishInfo.githubRepo"
		}
		*/
	}

	private void configureJava(Project project) {
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

	private void configureMaven(Project project, PublishInfo info) {
		def pomConfig = {
			licenses {
				license {
					name info.license.longName
					url info.license.url
				}
			}

			developers {
				developer {
					id info.developer.id
					name info.developer.name
					email info.developer.email
				}
			}

			scm {
				url info.vcsUrl
			}
		}

		project.publishing {
			publications {
				"$project.name"(MavenPublication) {
					from project.components.java
					artifact project.tasks.sourcesJar
					artifact project.tasks.javadocJar
					groupId project.group
					artifactId project.name
					version project.version

					pom.withXml {
						def root = asNode()
						root.appendNode('description', project.description)
						root.appendNode('name', project.name)
						root.appendNode('url', info.websiteUrl)
						root.children().last() + pomConfig
					}
				}
			}
		}
	}

	private void configureBintray(Project project, PublishInfo info) {
		project.bintray {
			user = System.getenv('BINTRAY_USER')
			key = System.getenv('BINTRAY_KEY')
			publications = [ project.name ]
			// dryRun = true
			// publish = true
			// override = true

			pkg {
				repo = 'maven'
				name = project.name
				// userOrg = ''
				desc = project.description
				websiteUrl = info.websiteUrl
				issueTrackerUrl = info.issueTrackerUrl
				vcsUrl = info.vcsUrl
				licenses = [ info.license.shortName ]
				labels = info.labels
				publicDownloadNumbers = true
				// attributes = []

				githubRepo = info.githubRepo
				githubReleaseNotesFile = 'CHANGELOG.md'

				version {
					name = project.version
					desc = "$project.name v$project.version"
					released = new Date()
					vcsTag = "v$project.version"
					// attributes = []

					gpg {
						sign = true
						passphrase = System.getenv('BINTRAY_GPG_PASSPHRASE')
					}
				}
			}
		}
	}
}
