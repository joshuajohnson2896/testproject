package com.jjohnson.testproject.main.config

import com.google.cloud.logging.LoggingOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun logging() = LoggingOptions.getDefaultInstance().getService()
}
