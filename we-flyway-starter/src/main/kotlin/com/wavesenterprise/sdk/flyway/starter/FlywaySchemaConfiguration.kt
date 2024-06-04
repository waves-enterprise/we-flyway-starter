package com.wavesenterprise.sdk.flyway.starter

import com.wavesenterprise.sdk.flyway.starter.PropertyMapper.configuration
import org.flywaydb.core.Flyway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(FlywaySchemaProperties::class)
class FlywaySchemaConfiguration {

    private val log = LoggerFactory.getLogger(FlywaySchemaConfiguration::class.java)

    @Autowired
    lateinit var flywaySchemaProperties: FlywaySchemaProperties

    @Bean
    fun flywayMigrationStrategy(additionalSchemes: List<FlywaySchema>): FlywayMigrationStrategy {
        return FlywayMigrationStrategy { flyway: Flyway ->
            val migrateMainAppSchema = {
                log.info("Starting main application schema migration")
                flyway.migrate()
            }
            val migrateAdditionalSchemes = {
                additionalSchemes.forEach { config ->
                    log.info("Starting ${config.getName()} schema migration")
                    createFlywayInstance(flyway.configuration.dataSource, config).migrate()
                }
            }
            if (flywaySchemaProperties.mainAppFirst) {
                migrateMainAppSchema()
                migrateAdditionalSchemes()
            } else {
                migrateAdditionalSchemes()
                migrateMainAppSchema()
            }
        }
    }

    private fun createFlywayInstance(dataSource: DataSource, config: FlywaySchema): Flyway =
        Flyway.configure()
            .configuration(flywaySchemaProperties.configurations[config.getName()])
            .dataSource(dataSource)
            .schemas(config.getName())
            .locations(config.getLocation())
            .load()
}
