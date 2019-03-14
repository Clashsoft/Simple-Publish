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
		""" << getClass().getResourceAsStream('config.gradle') << """
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
