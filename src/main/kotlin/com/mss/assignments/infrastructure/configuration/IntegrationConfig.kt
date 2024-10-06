package com.mss.assignments.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.messaging.MessageChannel
import org.springframework.integration.channel.DirectChannel

@Configuration
@EnableIntegration
class IntegrationConfig {

    @Bean
    fun inputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun integrationFlow(): IntegrationFlow {
        return IntegrationFlow.from(inputChannel())
            .handle { message ->
                println("Received: ${message.payload}")
            }
            .get()
    }
}
