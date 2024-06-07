package com.wavesenterprise.sdk.flyway.starter

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(
    classes = [
        FlywaySchemaConfiguration::class,
    ]
)
class ContextTest {

    @Test
    @Suppress("EmptyFunctionBlock")
    fun contextLoads() {}
}
