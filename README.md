# Simple-Publish

[![Build Status](https://travis-ci.org/Clashsoft/Simple-Publish.svg?branch=master)](https://travis-ci.org/Clashsoft/Simple-Publish)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/clashsoft/simple-publish/de.clashsoft.simple-publish.gradle.plugin/maven-metadata.xml.svg?colorB=blue&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/de.clashsoft.simple-publish)

A Gradle plugin that simplifies publishing to Maven and Bintray.

## Usage

The plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/de.clashsoft.simple-publish)
and can be installed via the `plugins` DSL in `build.gradle`:

```groovy
plugins {
	// ...
	id 'java'
	id 'maven-publish'
	id 'com.jfrog.bintray' version '1.8.4'
	id 'de.clashsoft.simple-publish' version '0.6.0'
	// ...
}

// ...
```

See [config.gradle](src/functionalTest/resources/config.gradle) for a configuration example.

To publish to Bintray, you need to configure your username and API key.
If you have a custom GPG key in your profile, you also need to set the passphrase.
The properties should be placed in `~/.gradle/gradle.properties`:

```
# ...
bintray.user=jdoe
bintray.key=a4Kn2HZn1Ub8B
bintray.gpg.passphrase=p@55w0rD
```

Alternatively, the plugin looks can read these from environment variables:

```
BINTRAY_USER=jdoe
BINTRAY_KEY=a4Kn2HZn1Ub8B
BINTRAY_GPG_PASSPHRASE=p@55w0rD
```

Don't forget to set the project name in `settings.gradle`:

```groovy
rootProject.name = 'test'
```

After configuring the Bintray repository, the plugin and the properties, you can upload with the `bintrayUpload` task.
Don't forget to publish your uploaded artifacts on the Bintray website!
