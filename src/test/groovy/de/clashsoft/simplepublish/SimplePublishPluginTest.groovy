package de.clashsoft.simplepublish

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.hamcrest.CoreMatchers.instanceOf
import static org.junit.Assert.assertThat

class SimplePublishPluginTest {
	@Test
	void supportsPublishInfo() {
		Project project = ProjectBuilder.builder().withName('test').build()
		project.pluginManager.apply 'de.clashsoft.simple-publish'

		project.group = 'com.example'
		project.version = '1.2.3'
		project.description = 'some project'
		project.publishInfo {
			organization = 'suborg'
			websiteUrl = 'http://example.com'
			issueTrackerUrl = 'http://example.com/issues'
			vcsUrl = 'http://example.com/vcs'
			githubRepo = 'com.example/example'
			labels = [ 'example', 'test', 'project' ]

			license {
				shortName 'BSD 3-Clause'
				longName 'BSD 3-Clause "New" or "Revised" License'
				url 'https://opensource.org/licenses/BSD-3-Clause'
			}

			developer {
				id 'jdoe'
				name 'John Doe'
				email 'jdoe@example.com'
			}
		}
	}

	@Test
	void supportsJavadocAndSourcesJar() {
		Project project = ProjectBuilder.builder().withName('test').build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'de.clashsoft.simple-publish'

		assertThat(project.javadocJar, instanceOf(Jar))
		assertThat(project.sourcesJar, instanceOf(Jar))
	}
}
