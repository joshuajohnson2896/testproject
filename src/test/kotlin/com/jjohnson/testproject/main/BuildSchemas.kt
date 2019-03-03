package com.jjohnson.testproject.main

import com.jjohnson.testproject.main.util.SchemaBuilderUtil
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class BuildSchemas {

    @Autowired
    private val builder: SchemaBuilderUtil = SchemaBuilderUtil()

    @Ignore
    @Test
    fun initSchemas() { builder.buildSchemas() }

    @Ignore
    @Test
    fun cleanSchemas() { builder.cleanSchemas() }
}
