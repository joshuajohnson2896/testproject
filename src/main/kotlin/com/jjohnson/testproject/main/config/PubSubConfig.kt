package com.jjohnson.testproject.main.config

import com.google.cloud.ServiceOptions
import com.google.cloud.pubsub.v1.Publisher
import com.google.pubsub.v1.ProjectTopicName
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PubSubConfig {
    @Value("\${cloud.google.pubsub.command-topic}")
    private val commandTopic: String? = null

    @Bean(name = ["commandPublisher"])
    fun getCommandPublisher(): Publisher =
        Publisher.newBuilder(ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), commandTopic))
           //.setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.getApplicationDefault()))
            .build()
}
