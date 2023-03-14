package com.example.demo.events

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class Test2EventListener : PrintHandler {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        logger.info("Hello from ${this::class.simpleName}")
    }

    override fun print(msg: String) {
        println("${this::class.simpleName} handled PRINT event: $msg")
    }
}