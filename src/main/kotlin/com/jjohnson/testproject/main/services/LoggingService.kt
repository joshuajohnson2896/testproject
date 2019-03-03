package com.jjohnson.testproject.main.services

import com.google.cloud.MonitoredResource
import com.google.cloud.logging.LogEntry
import com.google.cloud.logging.Logging
import com.google.cloud.logging.Payload
import com.google.cloud.logging.Severity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoggingService(
    val logging: Logging
) {
    @Value("\${cloud.google.projectId}")
    private lateinit var projectId: String

    fun log(message: String) {
        logEntry(
            message = message,
            severity = Severity.INFO
        )
    }

    fun error(message: String) {
        logEntry(
            message = message,
            severity = Severity.ERROR
        )
    }

    private fun logEntry(message: String, severity: Severity) {
        val logEntry = LogEntry.newBuilder(Payload.StringPayload.of(message))
            .setSeverity(severity)
            .setLogName(projectId)
            .setResource(MonitoredResource.newBuilder("global").build())
            .build()
        logging.write(Collections.singleton(logEntry))
    }
}
