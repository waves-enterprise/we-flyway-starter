val mavenUser: String by project
val mavenPassword: String by project

val springBootVersion: String by project
val hibernateTypesVersion: String by project
val flywayVersion: String by project

plugins {
    kotlin("plugin.spring")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
}
