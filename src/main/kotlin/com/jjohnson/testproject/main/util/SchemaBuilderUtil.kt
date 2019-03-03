package com.jjohnson.testproject.main.util

import org.apache.avro.reflect.ReflectData
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

@Component
class SchemaBuilderUtil {
    @Value("\${projectPaths.models.package}")
    private lateinit var objSource: String

    @Value("\${projectPaths.models.files}")
    private lateinit var models: Array<String>

    @Value("\${projectPaths.schemas.path}")
    private lateinit var writePath: String

    fun buildSchemas() {
        models.forEach {
            val obj = Class.forName("$objSource.$it").newInstance()
            val schema = ReflectData.get().getSchema(obj.javaClass)
            val writer = FileWriter(File("${fullWritePath()}$it.avsc"))

            writer.use { out -> out.write(schema.toString()) }
            writer.close()
        }
    }

    fun cleanSchemas() {
        File(fullWritePath()).list().forEach {
            if (!models.contains(it.substring(0..it.lastIndexOf('.')))) {
                Files.delete(Paths.get(fullWritePath() + it))
                println("File Cleaned: $it")
            }
        }
    }

    fun fullWritePath() = System.getProperty("user.dir") + writePath


}
