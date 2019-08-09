package de.clashsoft.simplepublish

import com.jfrog.bintray.gradle.BintrayExtension
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomScm
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

@CompileStatic
class SimplePublishPlugin implements Plugin<Project> {
	@Override
	void apply(Project project) {
		project.plugins.apply('java')
		project.plugins.apply('maven-publish')

		createArtifactTasks(project)

		final PublishInfo publishInfo = project.extensions.create('publishInfo', PublishInfo, project)

		final PublishingExtension publishing = project.extensions.getByType(PublishingExtension)

		publishing.publications.create(project.name, MavenPublication) { MavenPublication pub ->
			configureMaven(project, publishInfo, pub)
		}

		project.afterEvaluate {
			publishing.publications.getByName(project.name) { MavenPublication pub ->
				configureMavenAfter(project, pub)
			}

			if (project.pluginManager.hasPlugin('com.jfrog.bintray')) {
				project.extensions.configure(BintrayExtension) { BintrayExtension bt ->
					configureBintrayAfter(bt, project, publishInfo)
				}
			}
		}
	}

	private static void createArtifactTasks(Project project) {
		project.tasks.register('sourcesJar', Jar) {
			final SourceSetContainer sourceSets = project.convention.getPlugin(JavaPluginConvention).sourceSets

			it.from sourceSets.getByName('main').allSource
			it.archiveClassifier.set 'sources'
		}

		project.tasks.register('javadocJar', Jar) {
			final Javadoc javadoc = project.tasks.getByName('javadoc') as Javadoc

			it.dependsOn javadoc
			it.from javadoc.destinationDir
			it.archiveClassifier.set 'javadoc'
		}
	}

	private static void configureMaven(Project project, PublishInfo publishInfo, MavenPublication pub) {
		pub.from project.components.getByName('java')
		pub.artifact project.tasks.getByName('sourcesJar')
		pub.artifact project.tasks.getByName('javadocJar')

		final MavenPom pom = pub.pom

		pom.name.set project.provider { project.name }
		pom.description.set project.provider { project.description }
		pom.url.set publishInfo.websiteUrl
		pom.scm { MavenPomScm scm ->
			scm.url.set publishInfo.vcsUrl
		}
	}

	private static void configureMavenAfter(Project project, MavenPublication pub) {
		// neither Project nor Publication uses the Property API for these
		pub.groupId = pub.groupId ?: project.group
		pub.artifactId = pub.artifactId ?: project.name
		pub.version = pub.version ?: project.version
	}


	private static void configureBintrayAfter(BintrayExtension bt, Project project, PublishInfo info) {
		final PublishingExtension publishing = project.extensions.getByType(PublishingExtension)
		final DefaultMavenPublication publication = publishing.publications[project.name] as DefaultMavenPublication

		bt.user = bt.user ?: project.findProperty('bintray.user') ?: System.getenv('BINTRAY_USER')
		bt.key = bt.key ?: project.findProperty('bintray.key') ?: System.getenv('BINTRAY_KEY')
		bt.publications = bt.publications ?: [ project.name ] as String[]
		// bt.dryRun = true
		// bt.publish = true
		// bt.override = true

		final BintrayExtension.PackageConfig pkg = bt.pkg
		pkg.repo = pkg.repo ?: 'maven'
		pkg.name = pkg.name ?: project.name
		pkg.userOrg = pkg.userOrg ?: info.organization.get() ?: null
		pkg.desc = pkg.desc ?: project.description
		pkg.websiteUrl = pkg.websiteUrl ?: info.websiteUrl.get()
		pkg.issueTrackerUrl = pkg.issueTrackerUrl ?: info.issueTrackerUrl.get()
		pkg.vcsUrl = pkg.vcsUrl ?: info.vcsUrl.get()
		pkg.licenses = pkg.licenses ?: publication.pom.licenses*.name as String[]
		pkg.labels = pkg.labels ?: info.labels.get() as String[]
		pkg.publicDownloadNumbers = true
		// pkg.attributes = []

		pkg.githubRepo = pkg.githubRepo ?: info.githubRepo.get()
		pkg.githubReleaseNotesFile = pkg.githubReleaseNotesFile ?: 'CHANGELOG.md'

		final BintrayExtension.VersionConfig ver = pkg.version

		ver.name = ver.name ?: project.version
		ver.desc = ver.desc ?: "$project.name v$project.version"
		ver.released = ver.released ?: new Date()
		ver.vcsTag = ver.vcsTag ?: "v$project.version"
		// ver.attributes = []

		final BintrayExtension.GpgConfig gpg = ver.gpg
		gpg.sign = true
		gpg.passphrase = gpg.passphrase ?: project.findProperty('bintray.gpg.passphrase')
				?: System.getenv('BINTRAY_GPG_PASSPHRASE')
	}
}
