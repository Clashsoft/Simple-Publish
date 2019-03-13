package de.clashsoft.simplepublish

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class SimplePublishPluginTest {
	@Test
	void addsSourcesAndJavadocTask() {
		Project project = ProjectBuilder.builder().build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'de.clashsoft.simple-publish'

		assertTrue(project.tasks.sourcesJar instanceof Jar)
		assertTrue(project.tasks.javadocJar instanceof Jar)
	}

	@Test
	void addsPublication() {
		Project project = ProjectBuilder.builder().withName('abc123').build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'maven-publish'
		project.pluginManager.apply 'de.clashsoft.simple-publish'

		project.group = 'com.example'
		project.version = '1.2.3'
		project.description = 'some project'
		project.publishInfo {
			websiteUrl = 'https://github.com/Clashsoft/abc123'
			issueTrackerUrl = 'https://github.com/Clashsoft/abc123/issues'
			vcsUrl = 'https://github.com/Clashsoft/abc123'
			githubRepo = 'Clashsoft/abc123'
			labels = [ 'source', 'generator', 'tree', 'ast' ]

			license {
				shortName 'BSD 3-Clause'
				longName 'BSD 3-Clause "New" or "Revised" License'
				url 'https://opensource.org/licenses/BSD-3-Clause'
			}

			developer {
				id 'Clashsoft'
				name 'Adrian Kunz'
				email 'clashsoft@hotmail.com'
			}
		}
	}
}
