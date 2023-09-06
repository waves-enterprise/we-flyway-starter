plugins {
    kotlin("plugin.spring")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.flywaydb:flyway-core")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
}
