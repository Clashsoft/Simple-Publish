package de.clashsoft.simplepublish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

class SimplePublishPlugin implements Plugin<Project> {
	@Override
	void apply(Project target) {
		target.task('sourcesJar', type: Jar) {
			from sourceSets.main.allSource
			classifier = 'sources'
		}

		target.task('javadocJar', type: Jar, dependsOn: 'javadoc') {
			from javadoc.destinationDir
			classifier = 'javadoc'
		}
	}
}
