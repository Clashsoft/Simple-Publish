package de.clashsoft.simplepublish

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class FunctionalTest extends Specification {
	@Rule
	TemporaryFolder testProjectDir = new TemporaryFolder()

	def setup() {
		testProjectDir.newFile('settings.gradle') << """
		rootProject.name = 'test'
		"""

		testProjectDir.newFile('build.gradle') << """
		plugins {
		    id 'java'
		    id 'maven-publish'
		    id 'com.jfrog.bintray' version '1.8.4'
		    id 'de.clashsoft.simple-publish'
		}

		group 'com.example'
		version '1.2.3'
		description 'An example project.'

		publishInfo {
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
		        id 'testus'
		        name 'Testus Maximus'
		        email 'testus@example.com'
		    }
		}
		
		bintray.dryRun = true
		"""
	}

	def "can successfully configure maven and bintray and run bintrayUpload"() {
		when:
		def result = GradleRunner.create()
				.withProjectDir(testProjectDir.root)
				.withArguments('bintrayUpload')
				.withEnvironment(
				BINTRAY_USER: 'testus',
				BINTRAY_KEY: 'a4Kn2HZn1Ub8B',
				BINTRAY_GPG_PASSPHRASE: 'p@55w0rD')
				.withPluginClasspath()
				.build()

		then:
		result.task(":bintrayUpload").outcome == SUCCESS
	}
}
