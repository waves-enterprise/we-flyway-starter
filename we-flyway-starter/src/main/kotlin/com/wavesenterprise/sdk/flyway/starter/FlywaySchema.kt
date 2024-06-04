package com.wavesenterprise.sdk.flyway.starter

interface FlywaySchema {
    fun getName(): String
    fun getLocation(): String
}
