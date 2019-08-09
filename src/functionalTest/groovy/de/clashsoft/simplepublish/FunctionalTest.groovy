package de.clashsoft.simplepublish

import groovy.io.FileType
import groovy.transform.CompileStatic
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class FunctionalTest extends Specification {
	static final String TEST_FILES_ROOT = 'src/functionalTest/resources'
	static final String[] TEST_FILES = [
			'build.gradle',
			'config.gradle',
			'settings.gradle',
	]

	@Rule
	TemporaryFolder testProjectDir = new TemporaryFolder()

	@CompileStatic
	void setup() {
		final Path rootPath = testProjectDir.root.toPath()
		for (final String fileName : TEST_FILES) {
			final Path source = Paths.get(TEST_FILES_ROOT, fileName)
			final Path target = rootPath.resolve(fileName)

			Files.createDirectories(target.parent)

			try {
				Files.createLink(target, source)
			}
			catch (UnsupportedOperationException ignored) {
				Files.copy(source, target)
			}
		}
	}

	@CompileStatic
	BuildResult run(GradleRunner runner) {
		try {
			final BuildResult result = runner.build()

			println "-" * 30 + " Gradle Output " + "-" * 30
			println result.output
			println "-" * 30 + " Project Files " + "-" * 30
			return result
		}
		finally {
			testProjectDir.root.eachFileRecurse(FileType.FILES) {
				println it
			}
			println "-" * 75
		}
	}

	def "can successfully configure maven and bintray and run bintrayUpload"() {
		when:
		def result = run(GradleRunner.create()
				.withProjectDir(testProjectDir.root)
				.withArguments('bintrayUpload')
				.withEnvironment(
						BINTRAY_USER: 'testus',
						BINTRAY_KEY: 'a4Kn2HZn1Ub8B',
						BINTRAY_GPG_PASSPHRASE: 'p@55w0rD'
				)
				.withPluginClasspath())

		then:
		result.task(":bintrayUpload").outcome == SUCCESS
	}
}
