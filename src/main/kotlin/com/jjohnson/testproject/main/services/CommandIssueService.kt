package com.jjohnson.testproject.main.services

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.core.toOption
import com.google.api.core.ApiFuture
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.appengine.repackaged.com.google.common.util.concurrent.MoreExecutors
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import com.jjohnson.testproject.main.model.CommandMessage
import com.jjohnson.testproject.main.openapi.model.CommandItem
import org.apache.avro.Schema
import org.apache.avro.io.EncoderFactory
import org.apache.avro.reflect.ReflectDatumWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class CommandIssueService(
    val loggingService: LoggingService
) {
    @Autowired
    @Qualifier("commandPublisher")
    lateinit var commandPublisher: Publisher

    fun issueCommand(commandItem: CommandItem?) {
        commandItem.toOption().toEither {
            loggingService.error("Command Was Empty")
        }.map { nonNullCommandItem ->
            createMessage(CommandMessage(command = nonNullCommandItem.command)).fold({ notification ->
                loggingService.error(notification)
            }, { message ->
                ApiFutures.addCallback(commandPublisher.publish(message), object : ApiFutureCallback<String> {
                    override fun onFailure(t: Throwable?) {
                        loggingService.error(t!!.localizedMessage)
                    }

                    override fun onSuccess(result: String?) {
                        loggingService.log("Command Was Successfully Issued")
                        loggingService.log(result!!)
                    }
                }, MoreExecutors.directExecutor())
            })
        }
    }

    private fun createMessage(message: Any): Either<String,PubsubMessage> {
        val schemaName = message.javaClass.simpleName

        this.javaClass.classLoader.getResourceAsStream("schemas/$schemaName.avsc").use { inputStream ->
            return inputStream.toOption().toEither {
                "Schema was Empty"
            }.map {
                val schema = Schema.Parser().parse(inputStream)

                ByteArrayOutputStream().use { baos ->
                    val writer = ReflectDatumWriter<Any>(schema)
                    val encoder = EncoderFactory.get().binaryEncoder(baos,null)
                    writer.write(message,encoder)
                    encoder.flush()
                    val serializedData = ByteString.copyFrom(baos.toByteArray())
                     PubsubMessage.newBuilder().setData(serializedData).build()
                }
            }
        }
    }
}
