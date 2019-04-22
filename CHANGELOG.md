# Simple-Publish v0.1.0

+ Added creation of `sourcesJar` and `javadocJar` tasks.
+ Added configuration of publications.
+ Added configuration of bintray.

# Simple-Publish v0.2.0

* The Bintray User and Key and GPG Passphrase can now be configured from project properties.

# Simple-Publish v0.3.0 

+ Added support for setting a custom Bintray organization.

# Simple-Publish v0.4.0

* Artifact tasks (`javadocJar` and `sourcesJar`) are now registered when the plugin is applied.
* Existing bintray configuration no longer gets overridden.
* License and Developer configurations are now delegated to the `publishing` configuration.
* Simplified the internal maven configuration implementation.
* The publication is now configured eagerly by linking the `publishInfo` properties.
* The `PublishInfo` class now uses Gradle Properties.
* The `java` and `maven-publish` plugins are now automatically applied.
* The `vcsUrl`, `websiteUrl` and `issueTrackerUrl` properties can now be inferred from the `githubRepo`.
