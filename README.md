# Simple-Publish

[![Build Status](https://travis-ci.org/Clashsoft/Simple-Publish.svg?branch=master)](https://travis-ci.org/Clashsoft/Simple-Publish)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/clashsoft/simple-publish/de.clashsoft.simple-publish.gradle.plugin/maven-metadata.xml.svg?colorB=blue&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/de.clashsoft.simple-publish)

A Gradle plugin that simplifies publishing to Maven and Bintray.

## Usage

`~/.gradle/gradle.properties`:
```
# ...
bintray.user=jdoe
bintray.key=a4Kn2HZn1Ub8B
bintray.gpg.passphrase=p@55w0rD
```

`settings.gradle`:
```groovy
rootProject.name = 'test'
```

`build.gradle`:
```groovy
plugins {
	// ...
	id 'java'
	id 'maven-publish'
	id 'com.jfrog.bintray' version '1.8.4'
	id 'de.clashsoft.simple-publish' version '0.2.0'
}

// ...

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
        id 'jdoe'
        name 'John Doe'
        email 'jdoe@example.com'
    }
}

// ...
```
