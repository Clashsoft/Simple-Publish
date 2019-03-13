package de.clashsoft.simplepublish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar

class SimplePublishPlugin implements Plugin<Project> {
	@Override
	void apply(Project target) {
		if (target.pluginManager.hasPlugin('java')) {
			configureJava(target)
		}
	}

	private void configureJava(Project project) {
		project.task('sourcesJar', type: Jar) {
			from project.sourceSets.main.allSource
			classifier = 'sources'
		}

		project.task('javadocJar', type: Jar, dependsOn: 'javadoc') {
			from project.javadoc.destinationDir
			classifier = 'javadoc'
		}
	}
}
