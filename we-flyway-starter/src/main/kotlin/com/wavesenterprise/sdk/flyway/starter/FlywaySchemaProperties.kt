package com.wavesenterprise.sdk.flyway.starter

import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties(prefix = "spring.flyway")
@ConstructorBinding
data class FlywaySchemaProperties(
    @DefaultValue("false")
    val mainAppFirst: Boolean,
    val configurations: Map<String, FlywayProperties> = mutableMapOf(),
)
