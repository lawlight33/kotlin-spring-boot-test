package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@RestController
@RequestMapping(path = ["/"])
class TestController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping
    fun status(): Map<String, String> {
        val curRequest = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val headers = curRequest.headerNames.toList()
        logger.info("Person name is: ${curRequest.getHeader("person-name")}")
        headers.forEach { logger.info("Found header: $it") }
        return mapOf(
                "server" to "Test Controller",
                "headers" to headers.size.toString())
    }
}