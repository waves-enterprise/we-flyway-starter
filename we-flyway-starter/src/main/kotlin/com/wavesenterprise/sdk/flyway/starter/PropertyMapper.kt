package com.wavesenterprise.sdk.flyway.starter

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayProperties

object PropertyMapper {

    fun FluentConfiguration.configuration(properties: FlywayProperties?) =
        also {
            if (properties != null) {
                configureProperties(this, properties)
            }
        }

    // copied from
    // org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.FlywayConfiguration.configureProperties
    private fun configureProperties(configuration: FluentConfiguration, properties: FlywayProperties) {
        with(properties) {
            locations?.let { locations ->
                configuration.locations(*locations.toTypedArray())
            }
            schemas?.let { schemas ->
                configuration.schemas(*schemas.toTypedArray())
            }
            sqlMigrationSuffixes?.let { sqlMigrationSuffixes ->
                configuration.sqlMigrationSuffixes(*sqlMigrationSuffixes.toTypedArray())
            }

            encoding.let(configuration::encoding)
            connectRetries?.let(configuration::connectRetries)
            table?.let(configuration::table)
            tablespace?.let(configuration::tablespace)
            baselineDescription?.let(configuration::baselineDescription)
            baselineVersion?.let(configuration::baselineVersion)
            installedBy?.let(configuration::installedBy)
            placeholders?.let(configuration::placeholders)
            placeholderPrefix.let(configuration::placeholderPrefix)
            placeholderSuffix.let(configuration::placeholderSuffix)
            isPlaceholderReplacement.let(configuration::placeholderReplacement)
            sqlMigrationPrefix.let(configuration::sqlMigrationPrefix)
            sqlMigrationSeparator.let(configuration::sqlMigrationSeparator)
            repeatableSqlMigrationPrefix?.let(configuration::repeatableSqlMigrationPrefix)
            target?.let(configuration::target)
            isBaselineOnMigrate?.let(configuration::baselineOnMigrate)
            isCleanDisabled?.let(configuration::cleanDisabled)
            isCleanOnValidationError?.let(configuration::cleanOnValidationError)
            isGroup?.let(configuration::group)
            ignoreMigrationPatterns?.let { ignoreMigrationPatterns: List<String> ->
                configuration
                    .ignoreMigrationPatterns(*ignoreMigrationPatterns.toTypedArray<String>())
            }
            isMixed?.let(configuration::mixed)
            isOutOfOrder?.let(configuration::outOfOrder)
            isSkipDefaultCallbacks?.let(configuration::skipDefaultCallbacks)
            isSkipDefaultResolvers?.let(configuration::skipDefaultResolvers)
            isValidateOnMigrate?.let(configuration::validateOnMigrate)
            batch?.let(configuration::batch)
            dryRunOutput?.let(configuration::dryRunOutput)
            errorOverrides?.let(configuration::errorOverrides)
            licenseKey?.let(configuration::licenseKey)
            oracleSqlplus?.let(configuration::oracleSqlplus)
            oracleSqlplusWarn?.let(configuration::oracleSqlplusWarn)
            stream?.let(configuration::stream)
            undoSqlMigrationPrefix?.let(configuration::undoSqlMigrationPrefix)
        }
    }
}
