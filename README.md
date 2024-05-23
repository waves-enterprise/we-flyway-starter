# we-flyway-starter

Starter that enables shipping Spring Boot Starters with an additional DB schema for it initialized using Flyway.

## Usage.

To add a schema with your starter add a bean of type `FlywaySchema` to your Spring Application Context:

```kotlin
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun txObserverSchemaConfig(): FlywaySchema = object : FlywaySchema {
        override fun getName() = "starter_schema_name"
        override fun getLocation() = "__starter_schema_name"
    }
```

Flyway will look for migrations in the  `src/main/resources/__starter_schema_name` directory.