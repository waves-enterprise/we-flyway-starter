import io.gitlab.arturbosch.detekt.Detekt
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val weMavenUser: String? by project
val weMavenPassword: String? by project

val weMavenBasePath = "https://artifacts.wavesenterprise.com/repository/"

val sonaTypeMavenUser: String? by project
val sonaTypeMavenPassword: String? by project

val kotlinVersion: String by project
val springBootVersion: String by project
val flywayVersion: String by project

val kotlinCoroutinesVersion: String by project

val sonaTypeBasePath = "https://s01.oss.sonatype.org"

val gitHubProject = "waves-enterprise/we-flyway-starter"
val githubUrl = "https://github.com/$gitHubProject"

plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    `maven-publish`
    signing
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("io.gitlab.arturbosch.detekt") apply false
    id("com.gorylenko.gradle-git-properties") apply false
    id("org.jlleitschuh.gradle.ktlint") apply false
    id("com.palantir.git-version") apply false
    id("io.codearte.nexus-staging")
    id("fr.brouillard.oss.gradle.jgitver")
    id("org.jetbrains.dokka")
    id("jacoco")
}

jgitver {
    strategy = fr.brouillard.oss.jgitver.Strategies.PATTERN
    versionPattern = "\${M}.\${m}.\${meta.COMMIT_DISTANCE}-\${meta.GIT_SHA1_8}-SNAPSHOT"
    nonQualifierBranches = "master"
}

nexusStaging {
    serverUrl = "$sonaTypeBasePath/service/local/"
    username = sonaTypeMavenUser
    password = sonaTypeMavenPassword
}

jgitver {
    strategy = fr.brouillard.oss.jgitver.Strategies.PATTERN
    versionPattern =
        "\${M}.\${m}.\${meta.COMMIT_DISTANCE}-\${meta.GIT_SHA1_8}\${-~meta.QUALIFIED_BRANCH_NAME}-SNAPSHOT"
    nonQualifierBranches = "master,dev,main"
}

allprojects {
    group = "com.wavesenterprise"
    version = "-" // set by jgitver

    repositories {
        mavenCentral()
        mavenLocal()
        if (weMavenUser != null && weMavenPassword != null) {
            maven {
                name = "we-snapshots"
                url = uri("https://artifacts.wavesenterprise.com/repository/maven-snapshots/")
                mavenContent {
                    snapshotsOnly()
                }
                credentials {
                    username = weMavenUser
                    password = weMavenPassword
                }
            }
        }
    }
}



subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")

    val jacocoCoverageFile = "$buildDir/jacocoReports/test/jacocoTestReport.xml"

    tasks.withType<JacocoReport> {
        reports {
            xml.apply {
                required.set(true)
                outputLocation.set(file(jacocoCoverageFile))
            }
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events = setOf(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
        finalizedBy("jacocoTestReport")
    }

    val detektConfigFilePath = "$rootDir/gradle/detekt-config.yml"

    tasks.withType<Detekt> {
        exclude("resources/")
        exclude("build/")
        config.setFrom(detektConfigFilePath)
        buildUponDefaultConfig = true
    }

    val sourcesJar by tasks.creating(Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles sources JAR"
        archiveClassifier.set("sources")
        from(project.the<SourceSetContainer>()["main"].allSource)
    }

    val dokkaJavadoc by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
    val javadocJar by tasks.creating(Jar::class) {
        dependsOn(dokkaJavadoc)
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        description = "Assembles javadoc JAR"
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc.outputDirectory)
    }

    publishing {
        repositories {
            if (weMavenUser != null && weMavenPassword != null) {
                maven {
                    name = "WE-artifacts"
                    afterEvaluate {
                        url = uri("$weMavenBasePath${
                            if (project.version.toString()
                                    .endsWith("-SNAPSHOT")
                            ) "maven-snapshots" else "maven-releases"
                        }")
                    }
                    credentials {
                        username = weMavenUser
                        password = weMavenPassword
                    }
                }
            }

            if (sonaTypeMavenPassword != null && sonaTypeMavenUser != null) {
                maven {
                    name = "SonaType-maven-central-staging"
                    val releasesUrl = uri("$sonaTypeBasePath/service/local/staging/deploy/maven2/")
                    afterEvaluate {
                        url = if (version.toString()
                                .endsWith("SNAPSHOT")
                        ) throw kotlin.Exception("shouldn't publish snapshot") else releasesUrl
                    }
                    credentials {
                        username = sonaTypeMavenUser
                        password = sonaTypeMavenPassword
                    }
                }
            }
        }

        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                versionMapping {
                    allVariants {
                        fromResolutionResult()
                    }
                }
                afterEvaluate {
                    artifact(sourcesJar)
                    artifact(javadocJar)
                }
                pom {
                    packaging = "jar"
                    name.set(project.name)
                    url.set(githubUrl)
                    description.set("WE Node Client for Java/Kotlin")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    scm {
                        connection.set("scm:$githubUrl")
                        developerConnection.set("scm:git@github.com:$gitHubProject.git")
                        url.set(githubUrl)
                    }

                    developers {
                        developer {
                            id.set("kt3")
                            name.set("Stepan Kashintsev")
                            email.set("kpote3@gmail.com")
                        }
                        developer {
                            id.set("donyfutura")
                            name.set("Daniil Georgiev")
                            email.set("donyfutura@gmail.com")
                        }
                    }
                }
            }
        }
    }

    signing {
        afterEvaluate {
            if (!project.version.toString().endsWith("SNAPSHOT")) {
                sign(publishing.publications["mavenJava"])
            }
        }
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion") {
                bomProperty("kotlin.version", kotlinVersion)
            }
            dependencies {
                dependency("org.flywaydb:flyway-core:$flywayVersion")
                dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinCoroutinesVersion")
            }
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}
