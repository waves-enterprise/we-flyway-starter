package com.wavesenterprise.we.flyway.starter

interface FlywaySchema {
    fun getName(): String
    fun getLocation(): String
}
