pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val detektVersion: String by settings
    val gitPropertiesVersion: String by settings
    val palantirGitVersion: String by settings
    val jGitVerVersion: String by settings
    val dokkaVersion: String by settings
    val nexusPublishVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        `maven-publish`
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        id("io.gitlab.arturbosch.detekt") version detektVersion apply false
        id("com.palantir.git-version") version palantirGitVersion apply false
        id("com.gorylenko.gradle-git-properties") version gitPropertiesVersion apply false
        id("jacoco")
        id("fr.brouillard.oss.gradle.jgitver") version jGitVerVersion
        id("org.jetbrains.dokka") version dokkaVersion
        id("io.github.gradle-nexus.publish-plugin") version nexusPublishVersion apply false
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "we-flyway-starter"
include("we-flyway-starter")
