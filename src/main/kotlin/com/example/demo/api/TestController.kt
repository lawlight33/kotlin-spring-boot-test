package com.example.demo.api

import com.example.demo.events.PrintHandler
import com.example.demo.events.SubscribeHandler
import com.example.demo.gcp.CloudSQLConnector
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@RestController
class TestController {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var printHandlers: List<PrintHandler>

    @Autowired
    private lateinit var subscribeHandlers: List<SubscribeHandler>

    @Autowired
    private lateinit var cloudSQLConnector: CloudSQLConnector

    @RequestMapping(path = ["/headers"])
    fun headers(): Map<String, String> {
        val curRequest = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val headers = curRequest.headerNames.toList()
        logger.info("Person name is: ${curRequest.getHeader("person-name")}")
        headers.forEach { logger.info("Found header: $it") }
        return mapOf(
                "server" to "Test Controller",
                "headers" to headers.toString(),
                "headersCount" to headers.size.toString())
    }

    @RequestMapping(path = ["/events"])
    fun events(): ResponseEntity<String> {
        printHandlers.forEach { it.print(System.currentTimeMillis().toString()) }
        subscribeHandlers.forEach { it.subscribe(UUID.randomUUID().toString()) }
        return ResponseEntity.ok().body("OK")
    }

    @RequestMapping(path = ["/create_db"])
    fun createCloudSqlTable() {
        cloudSQLConnector.connect()
    }

    @RequestMapping(path = ["/echo"])
    fun echo(@RequestParam text: String): String {
        return text;
    }
}