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
}
